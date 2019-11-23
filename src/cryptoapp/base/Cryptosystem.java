package cryptoapp.base;

public interface Cryptosystem {

    boolean isNoGeneratedKeyAllowed();
    boolean isKeyLengthConst();

    String getKeyLengthMeasure();

    Encrypter getEncrypter();
    Decrypter getDecrypter();
    KeyGenerator getKeyGenerator();
}
