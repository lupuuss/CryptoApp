package cryptoapp.model.crypt.rabin;

import cryptoapp.base.Encrypter;
import cryptoapp.model.crypt.Crypt;
import cryptoapp.model.crypt.number.BigNumber;
import cryptoapp.model.crypt.number.Operations;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SuppressWarnings("WeakerAccess")
public class RabinEncrypter implements Encrypter {

    private final byte[] headerConst;
    private final Random random = new Random();

    public RabinEncrypter(byte[] headerConst) {
        this.headerConst = headerConst;
    }

    private byte[] createPadding(int bytesLength) {

        byte[] padding = new byte[bytesLength + RabinCryptosystem.HEADER_PADDING_SIZE];

        for (int i = 0; i < bytesLength; i++) {
            padding[i] = (byte)random.nextInt();
        }

        int[] singleItemArray = new int[1];
        singleItemArray[0] = bytesLength;
        byte[] paddingSize = Operations.intArrayToByte(singleItemArray);

        System.arraycopy(paddingSize, 0, padding, bytesLength, RabinCryptosystem.PADDING_INT_SIZE);

        System.arraycopy(headerConst, 0, padding,
                bytesLength + RabinCryptosystem.PADDING_INT_SIZE, headerConst.length);


        return padding;
    }

    @Override
    public byte[] encrypt(byte[] bytes, byte[] key) {

        int[] keyPair = Operations.byteArrayToInt(key);
        int[] pArray = Arrays.copyOfRange(keyPair, 0, keyPair.length / 2);
        int[] qArray = Arrays.copyOfRange(keyPair, keyPair.length / 2, keyPair.length);

        BigNumber n = new BigNumber(pArray).times(new BigNumber(qArray));

        int diff = key.length - bytes.length - RabinCryptosystem.HEADER_PADDING_SIZE;

        if (diff >= 0) {

            return rawEncrypt(bytes, n, diff);
        } else {

            int partSize = key.length - RabinCryptosystem.HEADER_PADDING_SIZE;

            List<byte[]> encryptedParts = new ArrayList<>();

            byte[] temp;

            for (int i = 0; i < bytes.length / partSize; i++) {
                temp = Arrays.copyOfRange(bytes, i * partSize, (i + 1) * partSize);
                encryptedParts.add(rawEncrypt(temp, n, 0));
            }

            int rest = bytes.length % partSize;

            if (rest != 0) {
                temp = Arrays.copyOfRange(bytes, partSize * (bytes.length / partSize), bytes.length);

                encryptedParts.add(rawEncrypt(temp, n, key.length - RabinCryptosystem.HEADER_PADDING_SIZE - rest));
            }

            return RabinCryptosystem.concatParts(encryptedParts);

        }
    }


    private byte[] rawEncrypt(byte[] bytes, BigNumber n, int padding) {

        byte[] paddingBytes = createPadding(padding);

        byte[] messageBytes = new byte[bytes.length + padding + RabinCryptosystem.HEADER_PADDING_SIZE];

        System.arraycopy(bytes, 0, messageBytes, 0, bytes.length);
        System.arraycopy(paddingBytes, 0, messageBytes,
                bytes.length, padding + RabinCryptosystem.HEADER_PADDING_SIZE);

        int[] messageInts = Operations.byteArrayToInt(messageBytes);

        BigNumber m = new BigNumber(messageInts);
        BigNumber c = m.times(m).divide(n).getRemainder(); // C = M^2 mod n

        return c.toByteArray();
    }

    @Override
    public void encrypt(InputStream in, OutputStream out, InputStream key) throws Exception {

        byte[] inputBlock;
        byte[] keyBytes = key.readAllBytes();

        do {

            inputBlock = in.readNBytes(Crypt.BLOCK_SIZE);

            out.write(encrypt(inputBlock, keyBytes));

        } while (inputBlock.length == Crypt.BLOCK_SIZE);

        in.close();
        out.close();
        key.close();
    }
}
