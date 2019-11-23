package cryptoapp.model.crypt.rabin;

import cryptoapp.base.KeyGenerator;
import cryptoapp.model.crypt.number.BigNumber;
import cryptoapp.model.crypt.number.Operations;
import cryptoapp.model.crypt.number.PrimeNumberGenerator;

import java.io.File;
import java.io.FileOutputStream;

public class RabinKeyGenerator implements KeyGenerator {

    private PrimeNumberGenerator primeNumberGenerator;

    public RabinKeyGenerator(PrimeNumberGenerator primeNumberGenerator) {
        this.primeNumberGenerator = primeNumberGenerator;
    }

    @Override
    public byte[] generate(byte[] message, int constKeyLength) {

        BigNumber p = primeNumberGenerator.generatePrime(
                constKeyLength / 2,
                n -> n.divide(new BigNumber(4)).getRemainder().isInt(3)
        );

        BigNumber q = primeNumberGenerator.generatePrime(
                constKeyLength / 2,
                n -> n.divide(new BigNumber(4)).getRemainder().isInt(3)
        );

        byte[] key = new byte[constKeyLength * 4];

        byte[] pBytes = p.toByteArray();
        byte[] qBytes = q.toByteArray();

        System.arraycopy(pBytes, 0, key, 0, constKeyLength * 2);

        System.arraycopy(qBytes, 0, key, constKeyLength * 2, constKeyLength * 2);

        return key;
    }

    @Override
    public void generateFile(long length, File message, File destinationFile) throws Exception {

        var out = new FileOutputStream(destinationFile);

        out.write(generate(null, (int)length));

        out.close();
    }
}
