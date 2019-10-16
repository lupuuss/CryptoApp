package cryptoapp.model.crypt.onetimepad;

import cryptoapp.base.Decrypter;

import java.io.InputStream;
import java.io.OutputStream;

public class OneTimePadDecrypter implements Decrypter {

    @SuppressWarnings("FieldCanBeLocal")
    private final int blockSize = 1024 * 1024 * 10;

    @Override
    public String decrypt(String text, String key) {

        StringBuilder decrypted = new StringBuilder();

        for (int i = 0; i < text.length() && i < key.length(); i++) {

            int b = text.charAt(i) - key.charAt(i);
            if (b < 0) {
                b += 256;
            }
            decrypted.append((char)b);
        }

        for (int i = key.length(); i < text.length(); i++) {

            decrypted.append(text.charAt(i));
        }

        return decrypted.toString();
    }

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
    private byte[] decrypt(byte[] encryptedBlock, byte[] keyBlock) {

        byte[] decrypted = new byte[encryptedBlock.length];

        for (int i = 0; i < encryptedBlock.length && i < keyBlock.length; i++) {

            int b = encryptedBlock[i] - keyBlock[i];
            if (b < 0) {
                b += 256;
            }
            decrypted[i] = (byte)b;
        }

        for (int i = keyBlock.length; i < encryptedBlock.length; i++) {

            decrypted[i] = encryptedBlock[i];
        }

        return decrypted;
    }

}
