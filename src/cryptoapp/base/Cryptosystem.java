package cryptoapp.base;

public interface Cryptosystem {

    boolean isNoGeneratedKeyAllowed();
    boolean isKeyLengthConst();

    int getDefaultKeyLength();

    Encrypter getEncrypter();
    Decrypter getDecrypter();
    KeyGenerator getKeyGenerator();
}
