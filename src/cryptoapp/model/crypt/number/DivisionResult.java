package cryptoapp.model.crypt.number;

public class DivisionResult {

    private BigNumber quotient;
    private BigNumber remainder;

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
