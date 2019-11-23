package cryptoapp.model.crypt.number.primalitytests;

import cryptoapp.model.crypt.number.BigNumber;

import java.util.List;

public class ConstPrimesPrimalityTest implements PrimalityTest {

    private List<BigNumber> primes;

    public ConstPrimesPrimalityTest(List<BigNumber> primes) {
        this.primes = primes;
    }

    @Override
    public boolean isPrime(BigNumber a) {

        var it = primes.iterator();

        while (it.hasNext()) {

            if (a.divide(it.next()).getRemainder().isZero()) {

                return false;
            }
        }

        return true;
    }
}
