package cryptoapp.model.crypt.rabin;

import cryptoapp.base.Decrypter;

import java.io.InputStream;
import java.io.OutputStream;

public class RabinDecrypter implements Decrypter {

    private final byte[] header;

    public RabinDecrypter(byte[] header) {
        this.header = header;
    }

    @Override
    public byte[] decrypt(byte[] bytes, byte[] key) {
        return new byte[0];
    }

    @Override
    public void decrypt(InputStream in, OutputStream out, InputStream key) throws Exception {

    }
}
