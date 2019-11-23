package cryptoapp.model.crypt.rabin;

import cryptoapp.base.Encrypter;
import cryptoapp.model.crypt.number.BigNumber;
import cryptoapp.model.crypt.number.Operations;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class RabinEncrypter implements Encrypter {

    private final byte[] headerConst;

    public RabinEncrypter(byte[] headerConst) {
        this.headerConst = headerConst;
    }

    private byte[] createHeader(int byteLength) {

        int i = byteLength;
        byte[] header = new byte[byteLength];

        while (--i >= 0) {
            header[i] = headerConst[i % 4];
        }

        return header;
    }

    @Override
    public byte[] encrypt(byte[] bytes, byte[] key) {

        int[] keyPair = Operations.byteArrayToInt(key);
        int[] pArray = Arrays.copyOfRange(keyPair, 0, keyPair.length / 2);
        int[] qArray = Arrays.copyOfRange(keyPair, keyPair.length / 2, keyPair.length);

        BigNumber n = new BigNumber(pArray).times(new BigNumber(qArray));
        byte[] encrypted = new byte[bytes.length + 8];

        rawEncrypt(bytes, n, encrypted, 0);

        return encrypted;
    }

    private void rawEncrypt(byte[] bytes, BigNumber n, byte[] encrypted, int index) {
        if (bytes.length - 8 < n.getIntegersCount()) {
            throw new IllegalStateException("m > n");
        }

        byte[] header = createHeader(8);

        int[] messageArray = new int[(bytes.length + 8) / 4];

        System.arraycopy(Operations.byteArrayToInt(header), 0, messageArray, 0, header.length / 4);
        System.arraycopy(Operations.byteArrayToInt(bytes), 0, messageArray, header.length / 4, bytes.length / 4);

        BigNumber m = new BigNumber(messageArray);
        BigNumber m1 = new BigNumber(messageArray);

        BigNumber c = m.times(m1).divide(n).getRemainder();

        System.arraycopy(c.toByteArray(), 0, encrypted, index, c.getIntegersCount() * 4);
    }

    @Override
    public void encrypt(InputStream in, OutputStream out, InputStream key) throws Exception {

    }
}
