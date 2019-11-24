package cryptoapp.model.crypt.rabin;

import cryptoapp.base.Decrypter;
import cryptoapp.model.crypt.Crypt;
import cryptoapp.model.crypt.number.BigNumber;
import cryptoapp.model.crypt.number.EuclideanResult;
import cryptoapp.model.crypt.number.Operations;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RabinDecrypter implements Decrypter {

    private final byte[] headerConst;

    public RabinDecrypter(byte[] headerConst) {
        this.headerConst = headerConst;
    }

    @Override
    public byte[] decrypt(byte[] bytes, byte[] key) {

        int[] keyPair = Operations.byteArrayToInt(key);
        int[] pArray = Arrays.copyOfRange(keyPair, 0, keyPair.length / 2);
        int[] qArray = Arrays.copyOfRange(keyPair, keyPair.length / 2, keyPair.length);

        BigNumber p = new BigNumber(pArray);
        BigNumber q = new BigNumber(qArray);
        BigNumber n = p.times(q);

        if (bytes.length % key.length != 0) {
            throw new IllegalStateException("Key length doesn't match message");
        } else if (bytes.length / key.length == 1) {

            return rawDecrypt(bytes, p, q, n);
        } else {

            byte[] temp;

            List<byte[]> decryptedParts = new ArrayList<>();

            for (int i = 0; i < bytes.length / key.length; i++) {
                temp = Arrays.copyOfRange(bytes, i * key.length,(i + 1) * key.length);
                decryptedParts.add(rawDecrypt(temp, p, q, n));
            }

            return RabinCryptosystem.concatParts(decryptedParts);
        }

    }

    private byte[] rawDecrypt(byte[] bytes, BigNumber p, BigNumber q, BigNumber n) {

        BigNumber c = new BigNumber(bytes);

        BigNumber mP = Operations.modularExponentiation(
                c, p.add(BigNumber.ONE).divide(new BigNumber(4)).getQuotient(), p
        );

        BigNumber mQ = Operations.modularExponentiation(
                c, q.add(BigNumber.ONE).divide(new BigNumber(4)).getQuotient(), q
        );

        EuclideanResult result = Operations.extendedEuclidean(p, q);

        BigNumber part1 = result.getYp().times(p).times(mQ);
        BigNumber part2 = result.getYq().times(q).times(mP);

        BigNumber[] squareRoots = new BigNumber[4];

        squareRoots[0] = part1.add(part2).divide(n).getRemainder();
        squareRoots[1] = n.subtract(squareRoots[0]);
        squareRoots[2] = part1.subtract(part2).divide(n).getRemainder();
        squareRoots[3] = n.subtract(squareRoots[2]);

        byte[] decrypted = findSolution(squareRoots);

        int padding = readPadding(decrypted);

        decrypted = Arrays.copyOf(decrypted, decrypted.length - padding - 8);

        return decrypted;
    }

    private byte[] findSolution(BigNumber[] squareRoots) {
        byte[] temp;

        boolean isOk = false;

        for (BigNumber root: squareRoots) {
            temp = root.toByteArray();

            isOk = false;
            for (int i = 0; i < headerConst.length; i++) {

                if (temp[temp.length - 1 - i] == headerConst[3 - i]) {
                    isOk = true;
                } else {

                    isOk = false;
                    break;
                }
            }

            if (isOk) {
                return temp;
            }
        }

        throw new IllegalStateException("Message couldn't be decrypted");
    }

    private int readPadding(byte[] message) {
        byte[] paddingSize = new byte[4];

        System.arraycopy(message, message.length - 8, paddingSize, 0, 4);

        int[] size = Operations.byteArrayToInt(paddingSize);
        return size[0];
    }

    @Override
    public void decrypt(InputStream in, OutputStream out, InputStream key) throws Exception {

        byte[] keyBytes = key.readAllBytes();
        byte [] inputBlock;

        do {

            inputBlock = in.readNBytes(Crypt.BLOCK_SIZE);
            out.write(decrypt(inputBlock, keyBytes));

        } while (inputBlock.length == Crypt.BLOCK_SIZE);

        in.close();
        out.close();
        key.close();
    }
}
