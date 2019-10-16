package cryptoapp.model.crypt.onetimepad;

import cryptoapp.base.Decrypter;

import java.io.InputStream;
import java.io.OutputStream;

public class OneTimePadDecrypter implements Decrypter {

    @SuppressWarnings("FieldCanBeLocal")
    private final int blockSize = 1024 * 1024 * 10;

    @Override
    public void decrypt(InputStream in, OutputStream out, InputStream key) throws Exception {

        byte [] inputBlock;
        byte [] keyBlock;

        do {

            inputBlock = in.readNBytes(blockSize);
            keyBlock = key.readNBytes(blockSize);

            out.write(decrypt(inputBlock, keyBlock));

        } while (inputBlock.length == blockSize);

        in.close();
        out.close();
        key.close();
    }

    @SuppressWarnings("ManualArrayCopy")
    @Override
    public byte[] decrypt(byte[] bytes, byte[] key) {

        byte[] decrypted = new byte[bytes.length];

        for (int i = 0; i < bytes.length && i < key.length; i++) {

            int b = bytes[i] - key[i];
            if (b < 0) {
                b += 256;
            }
            decrypted[i] = (byte)b;
        }

        for (int i = key.length; i < bytes.length; i++) {

            decrypted[i] = bytes[i];
        }

        return decrypted;
    }

}
