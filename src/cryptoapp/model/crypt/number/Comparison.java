package cryptoapp.model.crypt.number;

public class Comparison {

    public static int compareBitsRange(int[] a, int startA, int[] b, int startB, int intsCount) {

        for (int i = intsCount - 1; i >= 0; i--) {

            if (u_long(a[startA + i]) > u_long(b[startB + i])) {
                System.out.println("Compare: 1");
                return 1;
            } else if (u_long(b[startB + i]) > u_long(a[startA + i])) {
                System.out.println("Compare: -1");
                return -1;
            }
        }

        System.out.println("Compare: 0");
        return 0;
    }

    public static class Result {
        private BigNumber bigger;
        private BigNumber smaller;
        private boolean equal;
        private boolean isLeftBigger;

        public Result(BigNumber bigger, BigNumber smaller, boolean equal, boolean isLeftBigger) {
            this.bigger = bigger;
            this.smaller = smaller;
            this.equal = equal;
            this.isLeftBigger = isLeftBigger;
        }

        public BigNumber getBigger() {
            return bigger;
        }

        public BigNumber getSmaller() {
            return smaller;
        }

        public boolean areEqual() {
            return equal;
        }

        public boolean isLeftBigger() {
            return isLeftBigger;
        }
    }

    private static long u_long(int val) {
        return (long) val & 0xffffffffL;
    }

    public static Result compareNumbers(BigNumber a, BigNumber b) {

        if (a.realLength > b.realLength) {
            return new Result(a, b, false, true);
        } else if (b.realLength > a.realLength) {
            return new Result(b, a, false, false);
        }

        for (int i = a.realLength - 1; i >= 0; i--) {

            if (u_long(a.bits[i]) > u_long(b.bits[i])) {

                return new Result(a, b, false, true);

            } else if (u_long(b.bits[i]) > u_long(a.bits[i])) {

                return new Result(b, a, false, false);
            }
        }

        return new Result(a, b, true, false);
    }

}
