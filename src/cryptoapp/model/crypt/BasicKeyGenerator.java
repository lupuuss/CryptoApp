package cryptoapp.model.crypt;

import cryptoapp.base.KeyGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class BasicKeyGenerator implements KeyGenerator {

    private Random rand = new Random();

    @Override
    public String generate(int keyLength) {
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < keyLength; i++) {
            key.append((char)(rand.nextInt(26) + 65));
        }

        return key.toString();
    }

    @Override
    public File generateFile(long keyLength, File parent) throws Exception {

        var file = new File(parent,"generated_key");
        var out = new FileOutputStream(file);

        for (long i = 0; i < keyLength; i++) {

            out.write(rand.nextInt(255));
        }

        out.close();

        return file;
    }
}
