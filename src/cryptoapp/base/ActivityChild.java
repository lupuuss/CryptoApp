package cryptoapp.base;

public abstract class ActivityChild implements View {

    protected Activity parentActivity;

    public void onStartChild(Activity activity) {

        parentActivity = activity;
        onStart();
    }
}
