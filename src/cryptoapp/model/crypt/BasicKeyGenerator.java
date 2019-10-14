package cryptoapp.model.crypt;

import cryptoapp.base.KeyGenerator;

import java.io.File;
import java.util.Random;

public class BasicKeyGenerator implements KeyGenerator {
    @Override
    public String generate(int keyLength) {
        StringBuilder key = new StringBuilder();
        Random rand = new Random();

        for (int i = 0; i < keyLength; i++) {
            key.append((char)(rand.nextInt(26) + 65));
        }

        return key.toString();
    }

    @Override
    public File generateFile(long keyLength) throws Exception {
        return null;
    }
}
