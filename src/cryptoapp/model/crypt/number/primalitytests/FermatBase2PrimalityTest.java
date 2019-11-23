package cryptoapp.model.crypt.number.primalitytests;

import cryptoapp.model.crypt.number.BigNumber;
import cryptoapp.model.crypt.number.Operations;

public class FermatBase2PrimalityTest implements PrimalityTest {

    @Override
    public boolean isPrime(BigNumber a) {

        return Operations.modularExponentiation(BigNumber.TWO, a.subtract(BigNumber.ONE), a).isOne();
    }
}
