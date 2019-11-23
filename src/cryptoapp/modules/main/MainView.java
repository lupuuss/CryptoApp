package cryptoapp.modules.main;

import cryptoapp.base.ActivityChild;
import cryptoapp.base.Cryptosystem;
import cryptoapp.java.Lazy;
import cryptoapp.model.crypt.Crypt;
import cryptoapp.modules.filecrypt.FileCryptViewImpl;
import cryptoapp.modules.textcrypt.TextCryptViewImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

/**
 * View associated with main.fxml
 */
public class MainView extends ActivityChild {


    @FXML
    private ToggleButton fileToggle;
    @FXML
    private ToggleButton textToggle;
    @FXML
    private VBox currentParentContainer;

    @FXML
    private ComboBox<String> comboBox;

    private Crypt.System current = Crypt.System.OneTimePad;
    private TextCryptViewImpl text;
    private FileCryptViewImpl file;

    private final Lazy<Parent> textCryptView = new Lazy<>(() -> {

        try {

            var fxml = new FXMLLoader(getClass().getResource("/cryptoapp/fxml/textcrypt.fxml"));
            var parent = fxml.<Parent>load();
            text = fxml.getController();
            text.onStartChild(parentActivity);
            text.changeCryptosystem(enumToCryotosystem(current));
            return parent;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    });

    private final Lazy<Parent> fileCryptView = new Lazy<>(() -> {

        try {

            var fxml = new FXMLLoader(getClass().getResource("/cryptoapp/fxml/filecrypt.fxml"));
            var parent = fxml.<Parent>load();

            file = fxml.getController();
            file.onStartChild(parentActivity);
            file.changeCryptosystem(enumToCryotosystem(current));

            return parent;

        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException();
        }
    });

    @Override
    public void onStart() {

        comboBox.getItems().addAll("OneTimePad", "Rabin");
        comboBox.setValue("OneTimePad");

        comboBox.valueProperty().addListener((observableValue, s, t1) -> {
            current = Crypt.System.valueOf(observableValue.getValue());
            if (fileCryptView.isInitialized()) {
                file.changeCryptosystem(enumToCryotosystem(current));
            }

            if (textCryptView.isInitialized()) {
                text.changeCryptosystem(enumToCryotosystem(current));
            }
        });

        fileToggle.setSelected(true);
        currentParentContainer.getChildren().add(fileCryptView.getValue());

        textToggle.setOnMouseClicked( mouseEvent ->  {

            if (textToggle.isSelected()) {

                currentParentContainer.getChildren().set(0, textCryptView.getValue());
                fileToggle.setSelected(false);

            } else {

                textToggle.setSelected(true);
            }
        });

        fileToggle.setOnMouseClicked(mouseEvent -> {

            if (fileToggle.isSelected()) {

                currentParentContainer.getChildren().set(0, fileCryptView.getValue());
                textToggle.setSelected(false);
            } else {

                fileToggle.setSelected(true);
            }
        });
    }

    private Cryptosystem enumToCryotosystem(Crypt.System system) {
        switch (system) {

            case OneTimePad:
                return Crypt.getOneTimePad();
            case Rabin:
                return Crypt.getRabinCryptosystem();
            default:
                return null;
        }
    }
}
