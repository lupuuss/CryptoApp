package cryptoapp.model.crypt;

import cryptoapp.base.Cryptosystem;
import cryptoapp.model.crypt.number.PrimeNumberGenerator;
import cryptoapp.model.crypt.onetimepad.OneTimePad;
import cryptoapp.model.crypt.rabin.RabinCryptosystem;


public class Crypt {

    public enum System {
        OneTimePad, Rabin
    }

    public static final int BLOCK_SIZE = 1024 * 1024 * 10;

    private static final PrimeNumberGenerator primeNumberGenerator = new PrimeNumberGenerator();
    private static final OneTimePad oneTimePad = new OneTimePad();
    private static final RabinCryptosystem rabin = new RabinCryptosystem(primeNumberGenerator);

    public static Cryptosystem getOneTimePad() {
        return oneTimePad;
    }

    public static Cryptosystem getRabinCryptosystem() {
        return rabin;
    }
}
