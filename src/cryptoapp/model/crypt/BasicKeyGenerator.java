package cryptoapp.model.crypt;

import cryptoapp.base.KeyGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class BasicKeyGenerator implements KeyGenerator {

    private Random rand = new Random();
    @SuppressWarnings("FieldCanBeLocal")
    private final int blockSize = 1024 * 1024 * 10;

    @Override
    public void generateFile(long keyLength, File destinationFile) throws Exception {

        var out = new FileOutputStream(destinationFile);

        for (long i = 0; i < keyLength; i+= blockSize) {

            if (keyLength - i < blockSize) {
                out.write(generate((int)(keyLength - i)));
            } else {
                out.write(generate(blockSize));
            }
        }

        out.close();
    }

    @Override
    public byte[] generate(int n) {

        byte[] block = new byte[n];

        for (int i = 0; i < n; i++) {
            block[i] = (byte)rand.nextInt(256);
        }

        return block;
    }
}
