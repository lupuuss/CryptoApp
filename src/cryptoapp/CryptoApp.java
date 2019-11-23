package cryptoapp;

import cryptoapp.activities.MainActivity;
import cryptoapp.base.Cryptosystem;
import cryptoapp.model.crypt.Crypt;
import cryptoapp.model.crypt.number.Operations;
import cryptoapp.model.crypt.number.PrimeNumberGenerator;
import javafx.application.Application;
import javafx.stage.Stage;

public class CryptoApp extends Application {

    public static void main(String[] args) {

        var c = Crypt.getRabinCryptosystem();

        var key = c.getKeyGenerator().generate(null, 32);

        var array = PrimeNumberGenerator.getNBitsOddInteger(32);

        c.getEncrypter().encrypt(Operations.intArrayToByte(array), key);

        // Application.launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setMinWidth(550);
        new MainActivity().setOnStage(primaryStage);
        primaryStage.show();
    }
}