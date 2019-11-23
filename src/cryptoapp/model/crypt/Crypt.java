package cryptoapp.model.crypt;

import cryptoapp.base.Cryptosystem;
import cryptoapp.model.crypt.number.PrimeNumberGenerator;
import cryptoapp.model.crypt.onetimepad.OneTimePad;
import cryptoapp.model.crypt.rabin.RabinCryptosystem;


public class Crypt {

    public enum System {
        OneTimePad, Rabin
    }

    private static PrimeNumberGenerator primeNumberGenerator = new PrimeNumberGenerator();
    private static OneTimePad oneTimePad = new OneTimePad();
    private static RabinCryptosystem rabin = new RabinCryptosystem(primeNumberGenerator);

    public static Cryptosystem getOneTimePad() {
        return oneTimePad;
    }

    public static Cryptosystem getRabinCryptosystem() {
        return rabin;
    }
}
