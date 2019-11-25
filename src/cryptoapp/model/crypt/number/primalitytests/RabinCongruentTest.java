package cryptoapp.model.crypt.number.primalitytests;

import cryptoapp.model.crypt.number.BigNumber;

@SuppressWarnings("unused")
public class RabinCongruentTest extends NumberTest {

    public RabinCongruentTest() {}

    public RabinCongruentTest(TimeDebug state) {
        super(state);
    }

    @Override
    protected boolean validation(BigNumber a) {
        return a.divide(new BigNumber(4)).getRemainder().isInt(3);
    }
}
