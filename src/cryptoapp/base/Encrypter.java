package cryptoapp.base;

import java.io.InputStream;
import java.io.OutputStream;

public interface Encrypter {

    byte[] encrypt(byte[] bytes, byte[] key);
    void encrypt(InputStream in, OutputStream out, InputStream key) throws  Exception;
}
