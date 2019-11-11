package cryptoapp.base;

import java.io.File;

/**
 * Gives an activity's child access to it's parent
 */
public abstract class ActivityChild implements View {

    protected Activity parentActivity;

    public void onStartChild(Activity activity) {

        parentActivity = activity;
        onStart();
    }

    @Override
    public void openFileDesktop(File file) {
        parentActivity.openFileDesktop(file);
    }
}
