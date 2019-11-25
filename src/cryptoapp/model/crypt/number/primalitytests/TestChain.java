package cryptoapp.model.crypt.number.primalitytests;

import cryptoapp.model.crypt.number.BigNumber;

import java.util.ArrayList;
import java.util.List;

public class TestChain {

    private List<NumberTest> testList = new ArrayList<>();
    private BigNumber number;
    private NumberTest.TimeDebug defaultState = null;

    public TestChain(NumberTest initial, BigNumber number, NumberTest.TimeDebug state) {
        testList.add(initial);
        this.number = number;
        defaultState = state;
    }

    public TestChain(NumberTest initial, BigNumber number) {
        testList.add(initial);
        this.number = number;
    }

    public TestChain applyTest(NumberTest numberTest) {
        testList.add(numberTest);
        return this;
    }

    public boolean execute() {

        for (NumberTest test : testList) {

            if (defaultState != null) {
                test.setState(defaultState);
            }

            if (!test.isValid(number)) {
                return false;
            }
        }

        return true;
    }
}
