package cryptoapp.model.crypt.number;

import cryptoapp.model.crypt.number.primalitytests.ConstPrimesPrimalityTest;
import cryptoapp.model.crypt.number.primalitytests.FermatBase2PrimalityTest;
import cryptoapp.model.crypt.number.primalitytests.MillerRabinTest;
import cryptoapp.model.crypt.number.primalitytests.NumberTest;

import java.util.*;

@SuppressWarnings("WeakerAccess")
public class PrimeNumberGenerator {

    private final List<BigNumber> primes;

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

        boolean isPrime;

        int tested = 0;

        long time = System.currentTimeMillis();

        do {

            tested++;

            iHopeIsPrime = new BigNumber(getNBitsOddInteger(n));

            isPrime = iHopeIsPrime
                    .applyTest(new ConstPrimesPrimalityTest(primes))
                    .applyTest(new FermatBase2PrimalityTest(NumberTest.TimeDebug.ONLY_PASSED))
                    .applyTest(validator)
                    .applyTest(new MillerRabinTest(10, NumberTest.TimeDebug.ALL))
                    .execute();

        } while (!isPrime);

        System.out.println("Following number required to test "
                + tested + " numbers in " + ((System.currentTimeMillis() - time) / 1000.0) + "s");
        System.out.println(iHopeIsPrime.toBinaryString(false));
        return iHopeIsPrime;
    }

    public static int[] getNBitsOddInteger(int n) {

        Random rand = new Random();
        int[] result = new int[n];

        for (int i = 0; i < n; i++) {

            result[i] = rand.nextInt();
        }

        result[n - 1] |= 0x80000000;

        // makes int odd
        result[0] |= 1;

        return result;
    }
}
