package cryptoapp.model.crypt.onetimepad;

import cryptoapp.base.KeyGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class OneTimePadKeyGenerator implements KeyGenerator {

    private final Random rand = new Random();
    @SuppressWarnings("FieldCanBeLocal")
    private final int blockSize = 1024 * 1024 * 10;

    @Override
    public void generateFile(long constKeyLength, File message, File destinationFile) throws Exception {

        var out = new FileOutputStream(destinationFile);

        long keyLength = message.length();

        for (long i = 0; i < keyLength; i+= blockSize) {

            if (keyLength - i < blockSize) {
                out.write(generate(null, (int)(keyLength - i)));
            } else {
                out.write(generate(null, blockSize));
            }
        }

        out.close();
    }

    @Override
    public byte[] generate(byte[] message, int constKeyLength) {

        int keyLength = message == null ? constKeyLength : message.length;

        byte[] block = new byte[keyLength];

        for (int i = 0; i < keyLength; i++) {
            block[i] = (byte)rand.nextInt(256);
        }

        return block;
    }
}
