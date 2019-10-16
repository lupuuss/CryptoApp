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
    public String generate(int keyLength) {
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < keyLength; i++) {
            key.append((char)(rand.nextInt(256)));
        }

        return key.toString();
    }

    @Override
    public File generateFile(long keyLength, File parent) throws Exception {

        var file = new File(parent,"generated_key");
        var out = new FileOutputStream(file);

        for (long i = 0; i < keyLength; i+= blockSize) {

            if (keyLength - i < blockSize) {
                out.write(generateRandomBytesBlock((int)(keyLength - i)));
            } else {
                out.write(generateRandomBytesBlock(blockSize));
            }
        }

        out.close();

        return file;
    }

    private byte[] generateRandomBytesBlock(int n) {

        byte[] block = new byte[n];

        for (int i = 0; i < n; i++) {
            block[i] = (byte)rand.nextInt(256);
        }

        return block;
    }
}
