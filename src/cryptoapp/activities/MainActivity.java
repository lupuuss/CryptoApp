package cryptoapp.activities;

import cryptoapp.base.Activity;
import cryptoapp.modules.main.MainView;

public class MainActivity extends Activity<MainView> {

    public MainActivity() throws Exception {
        super("/cryptoapp/fxml/main.fxml", "OneTimePad");
    }

    @Override
    public void onStart() {

        getView().onStartChild(this);
    }
}

