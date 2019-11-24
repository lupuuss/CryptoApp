package cryptoapp.model.crypt.onetimepad;

import cryptoapp.base.Encrypter;
import cryptoapp.model.crypt.Crypt;

import java.io.InputStream;
import java.io.OutputStream;

@SuppressWarnings({ "DuplicatedCode", "ManualArrayCopy" })
public class OneTimePadEncrypter implements Encrypter {

    @Override
    public void encrypt(InputStream in, OutputStream out, InputStream key) throws Exception {

        byte[] inputBlock;
        byte[] keyBlock;

        do {

            inputBlock = in.readNBytes(Crypt.BLOCK_SIZE);
            keyBlock = key.readNBytes(Crypt.BLOCK_SIZE);

            out.write(encrypt(inputBlock, keyBlock));

        } while (inputBlock.length == Crypt.BLOCK_SIZE);

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
