package cryptoapp.model.crypt.onetimepad;

import cryptoapp.base.Decrypter;
import cryptoapp.model.crypt.Crypt;

import java.io.InputStream;
import java.io.OutputStream;

@SuppressWarnings("DuplicatedCode")
public class OneTimePadDecrypter implements Decrypter {

    @Override
    public void decrypt(InputStream in, OutputStream out, InputStream key) throws Exception {

        byte [] inputBlock;
        byte [] keyBlock;

        do {

            inputBlock = in.readNBytes(Crypt.BLOCK_SIZE);
            keyBlock = key.readNBytes(Crypt.BLOCK_SIZE);

            out.write(decrypt(inputBlock, keyBlock));

        } while (inputBlock.length == Crypt.BLOCK_SIZE);

        in.close();
        out.close();
        key.close();
    }

    @SuppressWarnings("ManualArrayCopy")
    @Override
    public byte[] decrypt(byte[] bytes, byte[] key) {

        byte[] decrypted = new byte[bytes.length];

        for (int i = 0; i < bytes.length && i < key.length; i++) {

            decrypted[i] = (byte)(bytes[i] ^ key[i]);
        }

        for (int i = key.length; i < bytes.length; i++) {

            decrypted[i] = bytes[i];
        }

        return decrypted;
    }

}
