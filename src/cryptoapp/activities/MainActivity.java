package cryptoapp.activities;

import cryptoapp.base.Activity;
import cryptoapp.modules.main.MainView;

public class MainActivity extends Activity<MainView> {

    public MainActivity() throws Exception {
        super("..\\modules\\main\\main.fxml", "Zadanie 1");
    }

    @Override
    public void onStart() throws Exception {
        super.onStart();
        getController().onStartChild(this);
    }
}

