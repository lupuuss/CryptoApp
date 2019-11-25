package cryptoapp.model.crypt.number;

@SuppressWarnings("WeakerAccess")
public class DivisionResult {

    private final BigNumber quotient;
    private final BigNumber remainder;

    public DivisionResult(BigNumber quotient, BigNumber remainder) {
        this.quotient = quotient;
        this.remainder = remainder;
    }

    public BigNumber getQuotient() {
        return quotient;
    }

    public BigNumber getRemainder() {
        return remainder;
    }
}
