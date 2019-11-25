package cryptoapp.model.crypt.number;

@SuppressWarnings("WeakerAccess")
public class EuclideanResult {

    private final BigNumber gcd;
    private final BigNumber yP;
    private final BigNumber yQ;

    public EuclideanResult(final BigNumber gcd, final BigNumber yP, final BigNumber yQ) {
        this.gcd = gcd;
        this.yP = yP;
        this.yQ = yQ;
    }

    public BigNumber getYp() {
        return yP;
    }

    public BigNumber getYq() {
        return yQ;
    }

    public BigNumber getGcd() {
        return gcd;
    }
}
