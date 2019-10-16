package cryptoapp.base;

import java.io.InputStream;
import java.io.OutputStream;

public interface Decrypter {

    byte[] decrypt(byte[] bytes, byte[] key);
    void decrypt(InputStream in, OutputStream out, InputStream key) throws Exception;
}