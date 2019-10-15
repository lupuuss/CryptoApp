package cryptoapp.model.crypt;

import cryptoapp.base.Decrypter;
import cryptoapp.base.Encrypter;
import cryptoapp.base.KeyGenerator;
import cryptoapp.model.crypt.onetimepad.OneTimePadDecrypter;
import cryptoapp.model.crypt.onetimepad.OneTimePadEncrypter;


public class Crypt {

    public static Decrypter getOneTimePadDecrypter() {

        return new OneTimePadDecrypter();
    }

    public static Encrypter getOneTimePadEncrypter() {

        return new OneTimePadEncrypter();
    }

    public static KeyGenerator getKeyGenerator() {

        return new BasicKeyGenerator();
    }
}
