package cryptoapp.base;

import java.io.File;

public interface KeyGenerator {

    String generate(int keyLength);
    File generateFile(long keyLength);
}
