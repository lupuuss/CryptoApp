package cryptoapp.base;

import java.io.File;

public interface KeyGenerator {

    byte[] generate(byte[] message, int constKeyLength);
    void generateFile(long length, File message, File destinationFile) throws Exception;
}
