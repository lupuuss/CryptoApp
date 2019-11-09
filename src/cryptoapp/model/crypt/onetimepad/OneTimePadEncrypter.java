package cryptoapp.model.crypt.onetimepad;

import cryptoapp.base.Encrypter;

import java.io.InputStream;
import java.io.OutputStream;

@SuppressWarnings({ "DuplicatedCode", "ManualArrayCopy", "FieldCanBeLocal"})
public class OneTimePadEncrypter implements Encrypter {

    private final int blockSize = 1024 * 1024 * 10;

    @Override
    public void encrypt(InputStream in, OutputStream out, InputStream key) throws Exception {

        byte[] inputBlock;
        byte[] keyBlock;

        do {

            inputBlock = in.readNBytes(blockSize);
            keyBlock = key.readNBytes(blockSize);

            out.write(encrypt(inputBlock, keyBlock));

        } while (inputBlock.length == blockSize);

        in.close();
        out.close();
        key.close();
    }

    @Override
    public byte[] encrypt(byte[] bytes, byte[] key) {

        byte[] encrypted = new byte[bytes.length];

        for (int i = 0; i < bytes.length && i < key.length; i++) {

            encrypted[i] = (byte)(bytes[i] ^ key[i]);
        }

        for (int i = key.length; i < bytes.length; i++) {

            encrypted[i] = bytes[i];
        }

        return encrypted;
    }
}
