package cryptoapp.base;

import java.io.File;

public interface KeyGenerator {

    String generate(int keyLength);
    void generateFile(long length, File destinationFile) throws Exception;
}
