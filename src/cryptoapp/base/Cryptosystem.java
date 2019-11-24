package cryptoapp.base;

public interface Cryptosystem {

    boolean isNoGeneratedKeyAllowed();
    boolean isKeyLengthConst();

    String getKeyLengthMeasure();
    int getDefaultKeyLength();

    Encrypter getEncrypter();
    Decrypter getDecrypter();
    KeyGenerator getKeyGenerator();
}
