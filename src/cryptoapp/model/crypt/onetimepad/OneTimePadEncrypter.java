package cryptoapp.model.crypt.onetimepad;

import cryptoapp.base.Encrypter;

import java.io.InputStream;
import java.io.OutputStream;

public class OneTimePadEncrypter implements Encrypter {
    @Override
    public String encrypt(String text, String key) {

        StringBuilder encrypted = new StringBuilder();

        for (int i = 0; i < text.length() && i < key.length(); i++) {

            encrypted.append(((char)((text.charAt(i) + key.charAt(i)) % 26 + 65)));
        }

        if (key.length() >= text.length()) {
            return encrypted.toString();
        }

        for (int i = key.length(); i < text.length(); i++) {

            encrypted.append(text.charAt(i));
        }

        return encrypted.toString();
    }

    @Override
    public void encrypt(InputStream in, OutputStream out, InputStream key) throws Exception {

    }
}
