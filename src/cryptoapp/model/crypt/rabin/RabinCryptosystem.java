package cryptoapp.model.crypt.rabin;

import cryptoapp.base.Cryptosystem;
import cryptoapp.base.Decrypter;
import cryptoapp.base.Encrypter;
import cryptoapp.base.KeyGenerator;
import cryptoapp.model.crypt.number.PrimeNumberGenerator;

import java.util.List;

public class RabinCryptosystem implements Cryptosystem {

    final static int HEADER_PADDING_SIZE = 8;
    final static int PADDING_INT_SIZE = 4;

    private final byte[] HEADER = { 0, 'R', 'B', 'N' }; // ends with 0 to m < n;

    private RabinEncrypter encrypter = new RabinEncrypter(HEADER);
    private RabinDecrypter decrypter = new RabinDecrypter(HEADER);
    private RabinKeyGenerator rabinKeyGenerator;

    public RabinCryptosystem(PrimeNumberGenerator primeNumberGenerator) {
        rabinKeyGenerator = new RabinKeyGenerator(primeNumberGenerator);
    }

    @Override
    public boolean isNoGeneratedKeyAllowed() {
        return false;
    }

    @Override
    public boolean isKeyLengthConst() {
        return true;
    }

    @Override
    public String getKeyLengthMeasure() {
        return "bits";
    }

    @Override
    public int getDefaultKeyLength() {
        return 1024;
    }

    @Override
    public Encrypter getEncrypter() {
        return encrypter;
    }

    @Override
    public Decrypter getDecrypter() {
        return decrypter;
    }

    @Override
    public KeyGenerator getKeyGenerator() {
        return rabinKeyGenerator;
    }

    static byte[] concatParts(List<byte[]> decryptedParts) {

        int size = 0;
        for(byte[] part : decryptedParts) {
            size += part.length;
        }

        byte[] decrypted = new byte[size];

        int i = 0;
        for (byte[] part : decryptedParts) {
            System.arraycopy(part, 0, decrypted, i, part.length);
            i+= part.length;
        }

        return decrypted;
    }

}
