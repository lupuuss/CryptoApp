package cryptoapp.model.crypt.onetimepad;

import cryptoapp.base.Cryptosystem;
import cryptoapp.base.Decrypter;
import cryptoapp.base.Encrypter;
import cryptoapp.base.KeyGenerator;

public class OneTimePad implements Cryptosystem {

    private final OneTimePadEncrypter encrypter = new OneTimePadEncrypter();
    private final OneTimePadDecrypter decrypter = new OneTimePadDecrypter();
    private final OneTimePadKeyGenerator keyGenerator = new OneTimePadKeyGenerator();

    @Override
    public boolean isNoGeneratedKeyAllowed() {
        return true;
    }

    @Override
    public boolean isKeyLengthConst() {
        return false;
    }

    @Override
    public int getDefaultKeyLength() {
        return 0;
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
        return keyGenerator;
    }
}
