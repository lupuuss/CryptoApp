package cryptoapp.base;

/**
 * Gives an activity's child access to it's parent
 */
public abstract class ActivityChild implements View {

    protected Activity parentActivity;

    public void onStartChild(Activity activity) {

        parentActivity = activity;
        onStart();
    }
}
