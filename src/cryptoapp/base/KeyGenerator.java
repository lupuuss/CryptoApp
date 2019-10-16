package cryptoapp.base;

import java.io.File;

public interface KeyGenerator {

    byte[] generate(int n);
    void generateFile(long length, File destinationFile) throws Exception;
}
