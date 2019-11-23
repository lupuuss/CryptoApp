package cryptoapp.model.crypt.number;

import java.util.Arrays;

@SuppressWarnings("WeakerAccess")
public class BigNumber {

    public static BigNumber ZERO = new BigNumber(0);
    public static BigNumber ONE = new BigNumber(1);
    public static BigNumber TWO = new BigNumber(2);

    int sign = 1;

    int[] bits;

    int realLength;

    /**
     * Expects raw unsigned bits and sign to fill internal fields
     * @param bits int array witch represents bits its treated as unsigned
     * @param sign must be -1 (negative number) or 1 (positive number)
     */
    public BigNumber(int[] bits, int sign) {
        this.bits = bits;
        realLength = bits.length;
        reduceZeros();
    }

    public BigNumber(byte[] bits) {

        this.bits = Operations.byteArrayToInt(bits);
    }

    public BigNumber(int[] bits) {
        this(bits, 1);
    }

    public BigNumber(int val) {

        this.bits = new int[1];
        bits[0] = val;

        realLength = 1;
    }

    public boolean isInt(int x) {
        if (bits.length == 1 && bits[0] == x) return true;
        if (bits.length == 0) return false;

        for (int i = bits.length - 1; i > 0; i--) {
            if (bits[i] != 0) {
                return false;
            }
        }

        return bits[0] == x;
    }

    public boolean isZero() {

        for (int bit : bits) {
            if (bit != 0) {
                return false;
            }
        }

        return true;
    }

    public boolean isOne() {
        return isInt(1);
    }

    public boolean isBiggerThanZero() {
        return sign != -1 && !isZero();
    }

    public BigNumber negate() {
        return new BigNumber(bits, -sign);
    }

    public BigNumber add(BigNumber b) {

        return Operations.add(this, b);
    }

    public BigNumber subtract(BigNumber b) {

        return Operations.subtract(this, b);
    }

    public BigNumber times(BigNumber b) {
        return Operations.multiply(this, b);
    }

    public DivisionResult divide(BigNumber b) {

        return Operations.divide(this, b);
    }

    public BigNumber shiftLeft(int n) {

        int[] result = Arrays.copyOf(bits, bits[realLength - 1] != 0 ? realLength + 1 : realLength); // Ensures that shift overflow is safe
        Operations.leftShiftIntArray(result, result.length, n);
        return new BigNumber(result, sign);
    }

    public BigNumber shiftRight(int n) {

        int[] result = Arrays.copyOf(bits, realLength);
        Operations.rightShiftIntArray(result, realLength, n);
        return new BigNumber(result, sign);
    }

    public void reduceZeros() {

        if (bits.length == 0 || bits.length == 1 && bits[0] == 0) return;

        int i = bits.length - 1;

        while (i >= 0 && bits[i] == 0) { i--; }

        realLength = i + 1;
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = bits.length - 1; i >= 0; i--) {
            stringBuilder.append(bits[i]);
            stringBuilder.append(' ');
        }

        return stringBuilder.toString();
    }

    public String toBinaryString(boolean spaceInts) {

        return Operations.intArrayToBinaryString(bits, realLength, spaceInts);
    }

    public byte[] toByteArray() {
        return Operations.intArrayToByte(bits);
    }

    public String toBinaryString() {
        return toBinaryString(true);
    }

    public int getIntegersCount() {
        return bits.length;
    }

    public int mod2() {
        return bits[0] & 1;
    }
}
