package cryptoapp.model.crypt.number.primalitytests;

import cryptoapp.model.crypt.number.BigNumber;
import cryptoapp.model.crypt.number.Comparison;
import cryptoapp.model.crypt.number.Operations;
import cryptoapp.model.crypt.number.PrimeNumberGenerator;

public class MillerRabinTest extends PrimalityTest {

    private int k;

    public MillerRabinTest(int k) {
        this.k = k;
    }

    public MillerRabinTest(int k, TimeDebug state) {
        super(state);
        this.k = k;
    }

    @Override
    public boolean isPrime(BigNumber n) {

        BigNumber d = n.subtract(BigNumber.ONE);
        BigNumber nMinusOne = n.subtract(BigNumber.ONE);
        int s = 0;

        while (d.mod2() == 0) {
            s++;
            d = d.shiftRight(1);
        }

        for (int i = 0; i < k; i++) {

            BigNumber a;

            do {

                a = new BigNumber(PrimeNumberGenerator.getNBitsOddInteger(n.getIntegersCount()));

            } while(Comparison.compareNumbers(BigNumber.ONE, n).isLeftBigger()
                    || Comparison.compareNumbers(n, a).isLeftBigger());


            BigNumber x = Operations.modularExponentiation(a, d, n);
            if (x.isOne()) {
                continue;
            }

            int r = 0;

            while (r <= s - 1 && !Comparison.compareNumbers(x, nMinusOne).areEqual()) {

                x = Operations.modularExponentiation(x, BigNumber.TWO, n);
                if (x.isOne()) {
                    return false;
                }

                r++;
            }

            if (r > s - 1) {
                return false;
            }
        }

        return true;
    }
}
