package cryptoapp.base;

import java.io.InputStream;
import java.io.OutputStream;

public interface Encrypter {

    String encrypt(String text, String key);
    void encrypt(InputStream in, OutputStream out, InputStream key);
}
