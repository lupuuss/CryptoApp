package cryptoapp.model.crypt.rabin;

import cryptoapp.base.Cryptosystem;
import cryptoapp.base.Decrypter;
import cryptoapp.base.Encrypter;
import cryptoapp.base.KeyGenerator;
import cryptoapp.model.crypt.number.PrimeNumberGenerator;

public class RabinCryptosystem implements Cryptosystem {

    private final byte[] header = {'H', 'E', 'A', 'D' };

    private RabinEncrypter encrypter = new RabinEncrypter(header);
    private RabinDecrypter decrypter = new RabinDecrypter(header);
    private RabinKeyGenerator rabinKeyGenerator;

    public RabinCryptosystem(PrimeNumberGenerator primeNumberGenerator) {
        rabinKeyGenerator = new RabinKeyGenerator(primeNumberGenerator);
    }

    @Override
    public boolean isNoGeneratedKeyAllowed() {
        return false;
    }

    @Override
    public boolean isKeyLengthConst() {
        return true;
    }

    @Override
    public String getKeyLengthMeasure() {
        return "bits";
    }

    @Override
    public Encrypter getEncrypter() {
        return encrypter;
    }

    @Override
    public Decrypter getDecrypter() {
        return decrypter;
    }

    @Override
    public KeyGenerator getKeyGenerator() {
        return rabinKeyGenerator;
    }
}
