package cryptoapp.base;

import java.io.InputStream;
import java.io.OutputStream;

public interface Decrypter {

    String decrypt(String text, String key);
    void decrypt(InputStream in, OutputStream out, InputStream key);
}