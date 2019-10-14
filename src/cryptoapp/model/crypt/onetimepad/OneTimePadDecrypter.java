package cryptoapp.model.crypt.onetimepad;

import cryptoapp.base.Decrypter;

import java.io.InputStream;
import java.io.OutputStream;

public class OneTimePadDecrypter implements Decrypter {

    @Override
    public String decrypt(String text, String key) {

        StringBuilder decrypted = new StringBuilder();

        for (int i = 0; i < text.length() && i < key.length(); i++) {

            int l = text.charAt(i) - key.charAt(i);
            decrypted.append(l < 0 ? (char)(l + 91) : (char)(l + 65));
        }

        if (key.length() >= text.length()) {
            return decrypted.toString();
        }

        for (int i = key.length(); i < text.length(); i++) {

            decrypted.append(text.charAt(i));
        }

        return decrypted.toString();
    }

    @Override
    public void decrypt(InputStream in, OutputStream out, InputStream key) throws Exception {

    }
}
