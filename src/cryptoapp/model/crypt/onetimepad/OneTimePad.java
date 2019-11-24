package cryptoapp.model.crypt.onetimepad;

import cryptoapp.base.Cryptosystem;
import cryptoapp.base.Decrypter;
import cryptoapp.base.Encrypter;
import cryptoapp.base.KeyGenerator;

public class OneTimePad implements Cryptosystem {

    private OneTimePadEncrypter encrypter = new OneTimePadEncrypter();
    private OneTimePadDecrypter decrypter = new OneTimePadDecrypter();
    private OneTimePadKeyGenerator keyGenerator = new OneTimePadKeyGenerator();

    @Override
    public boolean isNoGeneratedKeyAllowed() {
        return true;
    }

    @Override
    public boolean isKeyLengthConst() {
        return false;
    }

    @Override
    public String getKeyLengthMeasure() {
        return "";
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
