package cryptoapp.model.crypt.number;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

public class Operations {

    private static class KnuthResult {
        private int[] quotient;
        private int[] reminder;

        KnuthResult(int[] quotient, int[] reminder) {
            this.quotient = quotient;
            this.reminder = reminder;
        }

        int[] getQuotient() {
            return quotient;
        }

        int[] getReminder() {
            return reminder;
        }
    }

    public static long u_long(int val) {
        return (long) val & 0xffffffffL;
    }

    public static BigNumber add(BigNumber a, BigNumber b) {
        if (a.sign != b.sign && a.sign == 1) {

            return a.subtract(b.negate());

        } else if (a.sign != b.sign && a.sign == -1) {

            return b.subtract(a.negate());
        }

        BigNumber bigger = a.realLength > b.realLength ? a : b;
        BigNumber smaller = a.realLength > b.realLength ? b : a;
        int[] result = new int[bigger.realLength + 1];

        long carry = 0;

        for (int i = 0; i < smaller.realLength; i++) {

            carry +=  u_long(bigger.bits[i]) + u_long(smaller.bits[i]);
            result[i] = (int) carry;

            carry >>>= 32;
        }

        for (int i = smaller.realLength; i < bigger.realLength; i++) {

            carry +=  u_long(bigger.bits[i]);
            result[i] = (int) carry;

            carry >>>=32;
        }

        result[bigger.realLength] = (int) carry;

        return new BigNumber(result, a.sign);
    }

    public static BigNumber multiply(BigNumber a, BigNumber b) {

        if (a.isZero() || b.isZero()) {

            return BigNumber.ZERO;
        }

        if (a.isOne()) {
            return b;
        }

        if (b.isOne()) {
            return a;
        }

        int[] result = new int[a.realLength + b.realLength];

        for (int i = 0; i < a.realLength; i++) {
            long uA = u_long(a.bits[i]);
            long carry = 0;

            for (int j = 0; j < b.realLength; j++) {

                carry += uA * u_long(b.bits[j]) + u_long(result[i + j]);
                result[i + j] = (int) carry;
                carry >>>= 32;
            }
            result[i + b.realLength] = (int) carry;
        }

        return new BigNumber(result, a.sign * b.sign);
    }

    public static BigNumber subtract(BigNumber a, BigNumber b) {
        if (a.sign != b.sign) {
            return  a.add(b.negate());
        }

        var comparison = Comparison.compareNumbers(a, b);

        if (comparison.areEqual()) {
            return BigNumber.ZERO;
        }

        BigNumber bigger = comparison.getBigger();
        BigNumber smaller = comparison.getSmaller();
        int[] result = new int[bigger.realLength];

        long carry = 0;

        for (int i = 0; i < smaller.realLength; i++) {

            carry += u_long(bigger.bits[i]) - u_long(smaller.bits[i]);

            result[i] = (int) carry;

            carry >>>= 32;

            if (carry != 0) {
                carry = -1;
            }
        }

        for (int i = smaller.realLength; i < bigger.realLength; i++) {

            carry +=  u_long(bigger.bits[i]);
            result[i] = (int) carry;

            carry >>>=32;
            if (carry != 0) {
                carry = -1;
            }
        }

        if(comparison.isLeftBigger()) {
            return new BigNumber(result, a.sign);
        } else {
            return new BigNumber(result, -a.sign);
        }
    }

    public static void leftShiftIntArray(int[] array, int size, int n) {

        if (n > 31) {
            throw new IllegalArgumentException("Shift with value " + n + " is not supported!");
        }

        long carry = 0;
        int temp = 0;

        for (int i = 0; i < size; i++) {
            carry = u_long(array[i]) << n;
            array[i] = (int) carry + temp;
            carry >>>= 32;
            temp = (int) carry;
        }
    }

    public static void rightShiftIntArray(int[] array, int size, int n) {

        if (n > 31) {
            throw new IllegalArgumentException("Shift with value " + n + " is not supported!");
        }

        long carry;
        int temp = 0;

        for (int i = size - 1; i >= 0; i--) {
            carry = u_long(array[i]) << 32 - n;
            array[i] = (int)(carry >> 32) + temp;
            temp = (int) carry;
        }
    }

    public static DivisionResult divide(BigNumber a, BigNumber b) {
        if (b.isZero()) {
            throw new IllegalArgumentException("Divison by 0!");
        }

        if (a.isZero()) {
            return new DivisionResult(BigNumber.ZERO, BigNumber.ZERO);
        }

        if (a.sign == -1 || b.sign == -1) {
            throw new IllegalArgumentException("Only non negative division is available!");
        }

        if (b.isOne()) {
            return new DivisionResult(a, BigNumber.ZERO);
        }

        a.reduceZeros();
        b.reduceZeros();

        var comparison = Comparison.compareNumbers(a, b);

        if (comparison.areEqual()) {

            return new DivisionResult(BigNumber.ONE, BigNumber.ZERO);
        }

        if (!comparison.isLeftBigger()) {

            return new DivisionResult(BigNumber.ZERO, a);
        }

        if (a.getIntegersCount() == 1 && b.getIntegersCount() == 1) {

            return new DivisionResult(new BigNumber((int)(u_long(a.bits[0]) / u_long(b.bits[0]))), new BigNumber((int)(u_long(a.bits[0]) % u_long(b.bits[0]))));
        } else if (b.getIntegersCount() == 1) {

            return divisionByUnsignedInt(a.bits, a.realLength, b.bits[0]);
        }

        int[] dividend;
        int[] divisor = Arrays.copyOf(b.bits, b.realLength);
        int norm = normalizeValue(divisor, divisor.length);

        dividend = Arrays.copyOf(a.bits, a.realLength + 3);

        if (norm != 0) {
            leftShiftIntArray(divisor, divisor.length, norm);
            leftShiftIntArray(dividend, dividend.length, norm);
        }

        divide(dividend, dividend.length - 1, divisor, divisor.length);

        System.arraycopy(dividend, 0, divisor, 0, divisor.length);

        if (norm != 0) {
            rightShiftIntArray(divisor, divisor.length, norm);
        }

        int quotientSize = dividend.length - divisor.length;
        int[] quotient = new int[quotientSize];

        for (int i = 1; i < quotientSize - 1; i++) {
            quotient[i - 1] = dividend[i + divisor.length - 1];
        }

        return new DivisionResult(new BigNumber(quotient), new BigNumber(divisor));
    }

    private static int normalizeValue(int[] norm, int size) {

        int x = norm[size - 1];

        if (x == 0) {
            throw new IllegalArgumentException("Most significant integer of normalized array cannot be 0!");
        }

        int i = -1;
        int res = 0;
        int mask = 0x80000000;
        do {

            res = x & mask;
            mask >>>= 1;
            i++;
        } while (res == 0);

        return i;
    }

    public static KnuthResult division(int[] dividend, int m, int[] divisor, int n) {

        long guess = 0;
        int[] quotient = new int[m - n + 1];
        int[] remainder = new int[n + 1];

        System.arraycopy(dividend, m - n, remainder, 0, n);

        System.out.println(intArrayToBinaryString(remainder, remainder.length, true));

        for (int i = m - n; i >= 0; i--) {

            int compare;

            System.out.println("1] [" + i + "]" + intArrayToBinaryString(remainder, remainder.length, true));
            System.out.println("1] [" + i + "]" + intArrayToBinaryString(divisor, divisor.length, true));

            if (remainder[n] == 0) {
                compare = Comparison.compareBitsRange(remainder, 0, divisor, 0, n);
            } else {

                compare = 1;
                System.out.println("Compare default 1");
            }

            if (compare < 0 && i == 0) {
                break;
            } else if (compare == 0) {
                quotient[i] = 1;
                continue;

            }  else if (compare < 0) {
                quotient[i - 1] = 0;
                shiftOneIntLeft(remainder, remainder.length);
                remainder[0] = dividend[i - 1];
                continue;
            }

            guess = Long.divideUnsigned((u_long(remainder[n]) << 32) + u_long(remainder[n - 1]), divisor[n - 1]);

            System.out.println(guess);
            quotient[i] = 0;

            do {

                long carry = 0;

                System.out.println("2R] [" + i + "]" + intArrayToBinaryString(remainder, remainder.length, true));
                System.out.println("2D] [" + i + "]" + intArrayToBinaryString(divisor, divisor.length, true));

                for (int j = 0; j < n; j++) {

                    carry += u_long(remainder[j]) - guess * u_long(divisor[j]);
                    remainder[j] = (int) carry;

                    carry >>>= 32;

                    if (carry != 0) {
                        carry = -1;
                    }
                    System.out.println("2R] [" + j + "]" + intArrayToBinaryString(remainder, remainder.length, true));
                }

                System.out.println(carry);
                System.out.println(remainder[n]);
                remainder[n] += carry;
                System.out.println("2] [" + i + "]" + intArrayToBinaryString(remainder, remainder.length, true));

                quotient[i] += guess;

                guess = 1;

            } while (remainder[n] != 0 || Comparison.compareBitsRange(remainder, 0, divisor, 0, n) >= 0);


            if (i != 0) {
                shiftOneIntLeft(remainder, remainder.length);
                remainder[0] = dividend[i - 1];

            } else {
                break;
            }
            System.out.println("3] [" + i + "]" + intArrayToBinaryString(remainder, remainder.length, true));
            System.out.println("3Q] [" + i + "]" + intArrayToBinaryString(quotient, quotient.length, true));

        }

        return new KnuthResult(quotient, remainder);
    }

    public static long divideUnsignedLongByInt(long N, int D) {
        long q, r;
        long a1 = N >>> 32;
        long a0 = N & 0xffffffffL;
        if (D >= 0)
        {
            if (a1 < ((D - a1 - (a0 >>> 31)) & 0xffffffffL))
            {
                /* dividend, divisor, and quotient are nonnegative */
                q = N / D;
                r = N % D;
            }
            else
            {
                /* Compute c1*2^32 + c0 = a1*2^32 + a0 - 2^31*d */
                long c = N - ((long) D << 31);
                /* Divide (c1*2^32 + c0) by d */
                q = c / D;
                r = c % D;
                /* Add 2^31 to quotient */
                q += 1 << 31;
            }
        }
        else
        {
            long b1 = D >>> 1;	/* d/2, between 2^30 and 2^31 - 1 */
            //long c1 = (a1 >> 1); /* A/2 */
            //int c0 = (a1 << 31) + (a0 >> 1);
            long c = N >>> 1;
            if (a1 < b1 || (a1 >> 1) < b1)
            {
                if (a1 < b1)
                {
                    q = c / b1;
                    r = c % b1;
                }
                else /* c1 < b1, so 2^31 <= (A/2)/b1 < 2^32 */
                {
                    c = ~(c - (b1 << 32));
                    q = c / b1;  /* (A/2) / (d/2) */
                    r = c % b1;
                    q = (~q) & 0xffffffffL;    /* (A/2)/b1 */
                    r = (b1 - 1) - r; /* r < b1 => new r >= 0 */
                }
                r = 2 * r + (a0 & 1);
                if ((D & 1) != 0)
                {
                    if (r >= q) {
                        r = r - q;
                    } else if (q - r <= ((long) D & 0xffffffffL)) {
                        r = r - q + D;
                        q -= 1;
                    } else {
                        r = r - q + D + D;
                        q -= 2;
                    }
                }
            }
            else				/* Implies c1 = b1 */
            {				/* Hence a1 = d - 1 = 2*b1 - 1 */
                if (a0 >= ((long)(-D) & 0xffffffffL))
                {
                    q = -1;
                    r = a0 + D;
                }
                else
                {
                    q = -2;
                    r = a0 + D + D;
                }
            }
        }

        return (r << 32) | (q & 0xFFFFFFFFl);
    }

    public static int subtractAndMultiply(int[] dest, int offset, int[] x, int len, int y) {
        long yl = (long) y & 0xffffffffL;
        int carry = 0;
        int j = 0;
        do
        {
            long prod = ((long) x[j] & 0xffffffffL) * yl;
            int prod_low = (int) prod;
            int prod_high = (int) (prod >> 32);
            prod_low += carry;
            // Invert the high-order bit, because: (unsigned) X > (unsigned) Y
            // iff: (int) (X^0x80000000) > (int) (Y^0x80000000).
            carry = ((prod_low ^ 0x80000000) < (carry ^ 0x80000000) ? 1 : 0)
                    + prod_high;
            int x_j = dest[offset+j];
            prod_low = x_j - prod_low;
            if ((prod_low ^ 0x80000000) > (x_j ^ 0x80000000))
                carry++;
            dest[offset+j] = prod_low;
        }
        while (++j < len);
        return carry;
    }

    public static void divide (int[] zds, int nx, int[] y, int ny) {
        int j = nx;
        do
        {
            int qhat;
            if (zds[j]==y[ny-1])
                qhat = -1;
            else
            {
                long w = (((long)(zds[j])) << 32) + ((long)zds[j-1] & 0xffffffffL);
                qhat = (int) divideUnsignedLongByInt(w, y[ny-1]);
            }
            if (qhat != 0)
            {
                int borrow = subtractAndMultiply(zds, j - ny, y, ny, qhat);
                int save = zds[j];
                long num = ((long)save&0xffffffffL) - ((long)borrow&0xffffffffL);
                while (num != 0)
                {
                    qhat--;
                    long carry = 0;
                    for (int i = 0;  i < ny; i++)
                    {
                        carry += ((long) zds[j-ny+i] & 0xffffffffL)
                                + ((long) y[i] & 0xffffffffL);
                        zds[j-ny+i] = (int) carry;
                        carry >>>= 32;
                    }
                    zds[j] += carry;
                    num = carry - 1;
                }
            }
            zds[j] = qhat;
        } while (--j >= ny);
    }

    private static void shiftOneIntLeft(int[] array, int size) {

        for (int i = size - 1; i > 0; i--) {
            array[i] = array[i - 1];
        }
    }

    public static DivisionResult divisionByUnsignedInt (int[] dividend, int size, int divisor) {

        int[] quotient = new int[size];

        long k;
        long base = 0x100000000L;
        k = 0;
        for (int j = size - 1; j >= 0; j--) {
            quotient[j] = (int) Long.divideUnsigned(k * base + u_long(dividend[j]), u_long(divisor));
            k = Long.remainderUnsigned(k * base + u_long(dividend[j]), u_long(divisor));
        }

        return new DivisionResult(new BigNumber(quotient), new BigNumber((int) k));
    }

    private static KnuthResult knuthDivision(int[] dividend,int m, int[] divisor, int n) {

        int norm = normalizeValue(divisor, n);
        System.out.println(norm);
        if (norm != 0 && (int)(((u_long(dividend[dividend.length - 1]) << norm) & 0xffffffff00000000L) >> norm) != 0) {
            dividend = Arrays.copyOf(dividend, m + 1);
            m = m + 1;
        } else {
            dividend = Arrays.copyOf(dividend, m);
        }

        divisor = Arrays.copyOf(divisor, n);

        if (norm != 0) {
            leftShiftIntArray(dividend, m, norm);
            leftShiftIntArray(divisor, n, norm);
        }

        int[] quotient = new int[m - n + 1];
        int[] remainder = new int[n];

        long base = 0x100000000L;

        long q, r;

        for (int j = m - n - 1; j >= 0; j--) {
            q = Long.divideUnsigned(
                    u_long(dividend[j + n]) * base + u_long(dividend[j + n - 1]),
                    u_long(divisor[n - 1])
            );
            r = Long.remainderUnsigned(
                    u_long(dividend[j + n]) * base + u_long(dividend[j + n - 1]),
                    u_long(divisor[n - 1])
            );

            while (Long.compareUnsigned(q ,base) == 0
                    || Long.compareUnsigned(q * u_long(divisor[n-2]), base * r + u_long(dividend[j + n - 2])) > 0) {
                q--;
                r += u_long(divisor[n - 1]);


                if (Long.compareUnsigned(r, base) >= 0) break;
            }

            long k = 0, t, p;

            for (int i = 0; i < n; i++) {
                p =  q * u_long(divisor[i]);

                t = u_long(dividend[i + j]) - k - (p & 0xFFFFFFFFL);
                dividend[i + j] = (int)t;

                k = (p >> 32) - (t >> 32);
            }

            t = u_long(dividend[j + n]) - k;
            dividend[j + n] = (int)t;

            quotient[j] = (int)q;


            if (t < 0) {
                quotient[j]--;
                long carry = 0;

                for (int i = 0; i < n; i++) {
                    carry += u_long(dividend[i + j]) + u_long(divisor[i]);
                    dividend[i + j] = (int)carry;
                    carry >>>= 32;
                }

                dividend[j + n] += (int)carry;
            }
        }

        System.arraycopy(dividend, 0, remainder, 0, n);
        if (norm != 0) {
            rightShiftIntArray(remainder, n, norm);
        }

        return new KnuthResult(quotient, remainder);
    }

    public static BigNumber modularExponentiation(BigNumber base, BigNumber exponent, BigNumber modulus) {
        if (modulus.isOne()) return BigNumber.ZERO;

        base = base.divide(modulus).getRemainder();
        base.reduceZeros();
        BigNumber result = BigNumber.ONE;

        while (exponent.isBiggerThanZero()) {

            if (exponent.mod2() == 1) {
                result = result.times(base).divide(modulus).getRemainder();
            }
            exponent = exponent.shiftRight(1);

            base = base.times(base).divide(modulus).getRemainder();
        }
        return result;
    }

    public static String intArrayToBinaryString(int[] bits, int size, boolean spaceInts) {
        if (bits.length == 0) return "NULL_NUMBER";

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = bits.length - 1; i >= 0; i--) {

            appendZerosTo32bits(stringBuilder, Integer.toBinaryString(bits[i]));

            if (spaceInts) stringBuilder.append(' ');
        }

        return stringBuilder.toString();
    }

    public static int[] byteArrayToInt(byte[] bytes) {
        IntBuffer intBuffer = ByteBuffer.wrap(bytes).asIntBuffer();

        int[] ints= new int[intBuffer.remaining()];

        intBuffer.get(ints);

        return ints;
    }

    public static byte[] intArrayToByte(int[] ints) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(ints.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(ints);

        return byteBuffer.array();
    }

    private static void appendZerosTo32bits(StringBuilder builder, String temp) {

        for (int i = 0; i < 32 - temp.length(); i++) {
            builder.append("0");
        }

        builder.append(temp);
    }

}
