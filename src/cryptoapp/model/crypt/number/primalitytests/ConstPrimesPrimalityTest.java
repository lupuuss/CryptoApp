package cryptoapp.model.crypt.number.primalitytests;

import cryptoapp.model.crypt.number.BigNumber;

import java.util.List;

public class ConstPrimesPrimalityTest implements PrimalityTest {

    private final List<BigNumber> primes;

    public ConstPrimesPrimalityTest(List<BigNumber> primes) {
        this.primes = primes;
    }

    @Override
    public boolean isPrime(BigNumber a) {

        for (BigNumber prime : primes) {

            if (a.divide(prime).getRemainder().isZero()) {

                return false;
            }
        }

        return true;
    }
}
