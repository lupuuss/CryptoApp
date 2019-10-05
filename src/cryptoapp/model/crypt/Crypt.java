package cryptoapp.model.crypt;

import cryptoapp.base.Decrypter;
import cryptoapp.base.Encrypter;
import cryptoapp.base.KeyGenerator;

import java.io.InputStream;
import java.io.OutputStream;

public class Crypt {

    public static Decrypter getOneTimePadDecrypter() {

        return new Decrypter() {
            @Override
            public String decrypt(String text, String key) {
                return null;
            }

            @Override
            public void decrypt(InputStream in, OutputStream out, InputStream key) {

            }
        };
    }

    public static Encrypter getOneTimePadEncrypter() {

        return new Encrypter() {
            @Override
            public String encrypt(String text, String key) {
                return null;
            }

            @Override
            public void encrypt(InputStream in, OutputStream out, InputStream key) {

            }
        };
    }

    public static KeyGenerator getKeyGenerator() {

        return new KeyGenerator() {
            @Override
            public String generate(int keyLength) {
                return "XDD";
            }
        };
    }
}
