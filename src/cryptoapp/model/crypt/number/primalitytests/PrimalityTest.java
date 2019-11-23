package cryptoapp.model.crypt.number.primalitytests;

import cryptoapp.model.crypt.number.BigNumber;

public interface PrimalityTest {
    boolean isPrime(BigNumber a);
}
