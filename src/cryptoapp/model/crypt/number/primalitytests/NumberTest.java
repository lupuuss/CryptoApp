package cryptoapp.model.crypt.number.primalitytests;
import cryptoapp.model.crypt.number.BigNumber;

@SuppressWarnings("WeakerAccess")
public abstract class NumberTest {

    public enum TimeDebug {
        ALL, ONLY_PASSED, NEVER
    }

    private TimeDebug state = TimeDebug.NEVER;

    NumberTest(){

    }

    NumberTest(TimeDebug state) {
        this.state = state;
    }

    public boolean isValid(BigNumber a) {
        long lastTime = System.currentTimeMillis();
        var result = validation(a);
        if (state == TimeDebug.ALL || (state == TimeDebug.ONLY_PASSED && result)) {

            System.out.println("Test " + this.getClass().getSimpleName() + " "
                    + (result ? "passed" : "failed") + " in " + ((System.currentTimeMillis() - lastTime) / 1000.0) + " s");
        }
        return result;
    }

    abstract protected boolean validation(BigNumber a);

    @SuppressWarnings("unused")
    public TimeDebug getState() {
        return state;
    }

    public void setState(TimeDebug state) {
        this.state = state;
    }
}
