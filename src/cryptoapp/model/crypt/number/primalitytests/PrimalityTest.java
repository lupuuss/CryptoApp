package cryptoapp.model.crypt.number.primalitytests;

import cryptoapp.model.crypt.number.BigNumber;

@SuppressWarnings("WeakerAccess")
public abstract class PrimalityTest extends NumberTest {

    public PrimalityTest() {

    }

    public PrimalityTest(TimeDebug state) {
        super(state);
    }

    abstract boolean isPrime(BigNumber a);

    @Override
    protected boolean validation(BigNumber a) {
        return isPrime(a);
    }
}
