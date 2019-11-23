package cryptoapp.model.crypt;

import cryptoapp.base.KeyGenerator;

import java.io.File;

public class PrimeNumberKeyGenerator implements KeyGenerator {
    @Override
    public byte[] generate(int n) {
        return new byte[0];
    }

    @Override
    public void generateFile(long length, File destinationFile) throws Exception {

    }
}
