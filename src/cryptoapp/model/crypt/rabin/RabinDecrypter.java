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

@SuppressWarnings("WeakerAccess")
public class RabinDecrypter implements Decrypter {

    private final byte[] headerConst;

    public RabinDecrypter(byte[] headerConst) {
        this.headerConst = headerConst;
    }

    @Override
    public byte[] decrypt(byte[] bytes, byte[] key) {

        var decryptionPackage = new DecryptionPackage(key);

        if (bytes.length % key.length != 0) {
            throw new IllegalStateException("Key length doesn't match message");
        } else if (bytes.length == key.length) {

            return rawDecrypt(bytes,decryptionPackage);
        } else {

            byte[] temp;

            List<byte[]> decryptedParts = new ArrayList<>();

            for (int i = 0; i < bytes.length / key.length; i++) {
                temp = Arrays.copyOfRange(bytes, i * key.length,(i + 1) * key.length);
                decryptedParts.add(rawDecrypt(temp, decryptionPackage));
            }

            return RabinCryptosystem.concatParts(decryptedParts);
        }

    }

    private byte[] rawDecrypt(byte[] bytes, DecryptionPackage decPck) {

        BigNumber c = new BigNumber(bytes);

        BigNumber mP = Operations.modularExponentiation(
                c, decPck.pPlus1Divide4, decPck.p
        );

        BigNumber mQ = Operations.modularExponentiation(
                c, decPck.qPlus1Divide4, decPck.q
        );

        BigNumber part1 = decPck.yPTimesP.times(mQ);
        BigNumber part2 = decPck.yQTimesQ.times(mP);

        BigNumber[] squareRoots = new BigNumber[4];

        squareRoots[0] = part1.add(part2).divide(decPck.n).getRemainder();
        squareRoots[1] = decPck.n.subtract(squareRoots[0]);
        squareRoots[2] = part1.subtract(part2).divide(decPck.n).getRemainder();
        squareRoots[3] = decPck.n.subtract(squareRoots[2]);

        byte[] decrypted = findSolution(squareRoots);

        int padding = readPadding(decrypted);

        decrypted = Arrays.copyOf(decrypted, decrypted.length - padding - RabinCryptosystem.HEADER_PADDING_SIZE);

        return decrypted;
    }

    private byte[] findSolution(BigNumber[] squareRoots) {
        byte[] temp;

        boolean isOk;

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

        DecryptionPackage decryptionPackage = new DecryptionPackage(keyBytes);

        long time = System.currentTimeMillis();
        do {

            inputBlock = in.readNBytes(Crypt.BLOCK_SIZE);
            out.write(optimizedFileDecrypt(inputBlock, keyBytes.length, decryptionPackage));

        } while (inputBlock.length == Crypt.BLOCK_SIZE);

        System.out.println("Decryption time: " + ((System.currentTimeMillis() - time) / 1000.0) + "s");

        in.close();
        out.close();
        key.close();
    }

    private byte[] optimizedFileDecrypt(byte[] bytes, int keyLength, DecryptionPackage decPck) {

        if (bytes.length % keyLength != 0) {
            throw new IllegalStateException("Key length doesn't match message");
        } else if (bytes.length == keyLength) {

            return rawDecrypt(bytes, decPck);
        } else {

            byte[] temp;

            List<byte[]> decryptedParts = new ArrayList<>();

            for (int i = 0; i < bytes.length / keyLength; i++) {
                temp = Arrays.copyOfRange(bytes, i * keyLength,(i + 1) * keyLength);
                decryptedParts.add(rawDecrypt(temp, decPck));
            }

            return RabinCryptosystem.concatParts(decryptedParts);
        }
    }

    private static class DecryptionPackage {

        BigNumber p;
        BigNumber q;
        BigNumber n;
        BigNumber yPTimesP;
        BigNumber yQTimesQ;
        BigNumber pPlus1Divide4;
        BigNumber qPlus1Divide4;

        private DecryptionPackage(byte[] key) {

            int[] keyPair = Operations.byteArrayToInt(key);
            int[] pArray = Arrays.copyOfRange(keyPair, 0, keyPair.length / 2);
            int[] qArray = Arrays.copyOfRange(keyPair, keyPair.length / 2, keyPair.length);

            p = new BigNumber(pArray);
            q = new BigNumber(qArray);
             n = p.times(q);
            EuclideanResult result = Operations.extendedEuclidean(p, q);
            yPTimesP = result.getYp().times(p);
            yQTimesQ = result.getYq().times(q);
            pPlus1Divide4= p.add(BigNumber.ONE).divide(new BigNumber(4)).getQuotient();
            qPlus1Divide4 = q.add(BigNumber.ONE).divide(new BigNumber(4)).getQuotient();
        }

    }
}
