package cryptoapp.model.crypt.number;

import cryptoapp.model.crypt.number.primalitytests.ConstPrimesPrimalityTest;
import cryptoapp.model.crypt.number.primalitytests.FermatBase2PrimalityTest;
import cryptoapp.model.crypt.number.primalitytests.NumberTest;

import java.util.*;

public class PrimeNumberGenerator {

    private List<BigNumber> primes;

    public PrimeNumberGenerator() {
        this.primes = getAllPrimesToN(3000);
    }

    public List<BigNumber> getAllPrimesToN(int n) {

        List<BigNumber> primes = new ArrayList<>();

        for (int i = 3; i < n; i += 2) {

            primes.add(new BigNumber(i));
        }

        BigNumber prime;
        BigNumber maybePrime;

        int skip = 0;

        do {
            var it = primes.iterator();
            for (int i = 0; i < skip; i++) it.next();

            prime = it.next();

            while (it.hasNext()) {
                maybePrime = it.next();
                if (maybePrime.bits[0] % prime.bits[0] == 0) {
                    it.remove();
                }
            }

            skip++;

        } while(prime.bits[0] < Math.sqrt(n));

        return primes;
    }

    public BigNumber generatePrime(int n, NumberTest validator) {

        BigNumber iHopeIsPrime;

        boolean isPrime = true;
        boolean isValid = true;

        var constTest = new ConstPrimesPrimalityTest(primes);

        int tested = 0;

        do {

            tested++;

            iHopeIsPrime = new BigNumber(getNBitsOddInteger(n));

            isPrime = constTest.isPrime(iHopeIsPrime);

            if (!isPrime) {
                continue;
            }

            isValid = validator.isValid(iHopeIsPrime);

            if (!isValid) {
                continue;
            }

            isPrime = new FermatBase2PrimalityTest().isPrime(iHopeIsPrime);

            if (isPrime) {
                break;
            }
        } while (true);

        System.out.println("Following number required to test " + tested + " numbers");
        return iHopeIsPrime;
    }

    public static int[] getNBitsOddInteger(int n) {

        Random rand = new Random();
        int[] result = new int[n];

        for (int i = 0; i < n; i++) {

            result[i] = rand.nextInt();
        }

        // makes int odd
        result[0] |= 1;

        return result;
    }
}
