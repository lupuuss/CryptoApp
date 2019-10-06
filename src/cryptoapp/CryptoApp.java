package cryptoapp;

import cryptoapp.activities.MainActivity;
import javafx.application.Application;
import javafx.stage.Stage;

public class CryptoApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setMinWidth(550);
        new MainActivity().setOnStage(primaryStage);
        primaryStage.show();
    }
}