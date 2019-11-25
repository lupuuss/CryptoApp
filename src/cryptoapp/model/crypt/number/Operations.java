package cryptoapp.model.crypt.number;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

@SuppressWarnings({"WeakerAccess", "SuspiciousNameCombination", "unused"})
public class Operations {

    public static long u_long(int val) {
        return (long) val & 0xffffffffL;
    }

    public static BigNumber add(BigNumber a, BigNumber b) {
        if (a.sign != b.sign && a.sign == 1) {

            return a.subtract(b.negate());

        } else if (a.sign != b.sign && a.sign == -1) {

            return b.subtract(a.negate());
        }

        if (a.isZero()) {
            return b;
        }

        if (b.isZero()) {
            return a;
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
            return new BigNumber(b.bits, a.sign * b.sign);
        }

        if (b.isOne()) {
            return new BigNumber(a.bits, a.sign * b.sign);
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
            return a.add(b.negate());
        }

        if (a.isZero()) {
            return new BigNumber(b.bits, b.sign * (-1));
        }

        if (b.isZero()) {
            return a;
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
            return new BigNumber(result, -1 * a.sign);
        }
    }

    public static void leftShiftIntArray(int[] array, int size, int n) {

        if (n > 31) {
            throw new IllegalArgumentException("Shift with value " + n + " is not supported!");
        }

        long carry;
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
            throw new IllegalArgumentException("Division by 0!");
        }

        if (a.isZero()) {
            return new DivisionResult(BigNumber.ZERO, BigNumber.ZERO);
        }

        if (b.isOne()) {
            return new DivisionResult(a, BigNumber.ZERO);
        }

        a.reduceZeros();
        b.reduceZeros();

        var comparison = Comparison.compareNumbers(a, b);

        if (comparison.areEqual()) {

            return new DivisionResult(new BigNumber(1, a.sign * b.sign), BigNumber.ZERO);
        }

        if (!comparison.isLeftBigger()) {

            return new DivisionResult(BigNumber.ZERO, a);
        }

        if (b.getIntegersCount() == 1) {

            return divisionByUnsignedInt(a.bits, a.realLength, b.bits[0], a.sign, b.sign);
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

        if (quotientSize - 1 - 1 >= 0)
            System.arraycopy(dividend, 1 + divisor.length - 1, quotient, 0, quotientSize - 1 - 1);

        return new DivisionResult(new BigNumber(quotient, a.sign * b.sign), new BigNumber(divisor));
    }

    private static int normalizeValue(int[] norm, int size) {

        int x = norm[size - 1];

        if (x == 0) {
            throw new IllegalArgumentException("Most significant integer of normalized array cannot be 0!");
        }

        int i = -1;
        int res;
        int mask = 0x80000000;
        do {

            res = x & mask;
            mask >>>= 1;
            i++;
        } while (res == 0);

        return i;
    }

    public static int subtractAndMultiply(int[] dest, int offset, int[] x, int len, int y) {
        long yl = u_long(y);
        int carry = 0;
        int j = 0;
        do {
            long prod = u_long(x[j]) * yl;
            int prod_low = (int) prod;
            int prod_high = (int) (prod >> 32);
            prod_low += carry;

            carry = ((prod_low ^ 0x80000000) < (carry ^ 0x80000000) ? 1 : 0)
                    + prod_high;
            int x_j = dest[offset+j];
            prod_low = x_j - prod_low;
            if ((prod_low ^ 0x80000000) > (x_j ^ 0x80000000))
                carry++;
            dest[offset+j] = prod_low;
        } while (++j < len);
        return carry;
    }

    public static void divide (int[] zds, int nx, int[] y, int ny) {
        int j = nx;
        do {
            int qhat;
            if (zds[j]==y[ny-1]) {
                qhat = -1;
            } else  {
                long w = (((long)(zds[j])) << 32) + u_long(zds[j-1]);
                qhat = (int) Long.divideUnsigned(w, u_long(y[ny-1]));
            }

            if (qhat != 0) {

                int borrow = subtractAndMultiply(zds, j - ny, y, ny, qhat);
                int save = zds[j];
                long num = u_long(save) - u_long(borrow);
                while (num != 0) {
                    qhat--;
                    long carry = 0;
                    for (int i = 0;  i < ny; i++) {
                        carry += u_long(zds[j-ny+i]) + u_long(y[i]);
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

        if (size - 1 >= 0) System.arraycopy(array, 0, array, 1, size - 1);
    }

    public static DivisionResult divisionByUnsignedInt (int[] dividend, int size, int divisor, int dividendSign, int divisorSign) {

        int[] quotient = new int[size];

        long k;
        long base = 0x100000000L;
        k = 0;
        for (int j = size - 1; j >= 0; j--) {
            quotient[j] = (int) Long.divideUnsigned(k * base + u_long(dividend[j]), u_long(divisor));
            k = Long.remainderUnsigned(k * base + u_long(dividend[j]), u_long(divisor));
        }

        return new DivisionResult(new BigNumber(quotient, dividendSign * divisorSign), new BigNumber((int) k));
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

    public static EuclideanResult extendedEuclidean(final BigNumber a, final BigNumber b) {
        if (b.isZero()) {
            return new EuclideanResult(a, BigNumber.ONE, BigNumber.ZERO);
        } else {
            final EuclideanResult extension = extendedEuclidean(b, a.divide(b).getRemainder());
            return new EuclideanResult(
                    extension.getGcd(),
                    extension.getYq(),
                    extension.getYp().subtract(a.divide(b).getQuotient().times(extension.getYq())));
        }
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

        builder.append("0".repeat(Math.max(0, 32 - temp.length())));

        builder.append(temp);
    }
}
