package cryptoapp.model.crypt.onetimepad;

import cryptoapp.base.Encrypter;

import java.io.InputStream;
import java.io.OutputStream;

public class OneTimePadEncrypter implements Encrypter {

    @SuppressWarnings("FieldCanBeLocal")
    private final int blockSize = 1024 * 1024 * 10;

    @Override
    public String encrypt(String text, String key) {

        StringBuilder encrypted = new StringBuilder();

        for (int i = 0; i < text.length() && i < key.length(); i++) {

            encrypted.append(((char)(text.charAt(i) + key.charAt(i)) % 256));
        }

        if (key.length() >= text.length()) {
            return encrypted.toString();
        }

        for (int i = key.length(); i < text.length(); i++) {

            encrypted.append(text.charAt(i));
        }

        return encrypted.toString();
    }

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

    @SuppressWarnings("ManualArrayCopy")
    private byte[] encrypt(byte[] bytesBlock, byte[] keyBlock) {

        byte[] encrypted = new byte[bytesBlock.length];

        for (int i = 0; i < bytesBlock.length && i < keyBlock.length; i++) {

            encrypted[i] = (byte)((bytesBlock[i] + keyBlock[i]) % 256);
        }

        for (int i = keyBlock.length; i < bytesBlock.length; i++) {

            encrypted[i] = bytesBlock[i];
        }

        return encrypted;
    }
}
