package cryptoapp.modules.main;

import cryptoapp.base.ActivityChild;
import cryptoapp.java.Lazy;
import cryptoapp.modules.filecrypt.FileCryptViewImpl;
import cryptoapp.modules.textcrypt.TextCryptViewImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


public class MainView extends ActivityChild {

    @FXML
    private GridPane root;
    @FXML
    private ToggleButton fileToggle;
    @FXML
    private ToggleButton textToggle;
    @FXML
    private VBox currentParentContainer;

    private final Lazy<Parent> textCryptView = new Lazy<>(() -> {

        try {

            var fxml = new FXMLLoader(getClass().getResource("..\\textcrypt\\textcrypt.fxml"));
            var parent = fxml.<Parent>load();
            fxml.<TextCryptViewImpl>getController().onStartChild(parentActivity);

            return parent;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    });

    private final Lazy<Parent> fileCryptView = new Lazy<>(() -> {

        try {

            var fxml = new FXMLLoader(getClass().getResource("..\\filecrypt\\filecrypt.fxml"));
            var parent = fxml.<Parent>load();
            fxml.<FileCryptViewImpl>getController().onStartChild(parentActivity);

            return parent;

        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException();
        }
    });

    @Override
    public void onStart() {

        textToggle.setSelected(true);
        currentParentContainer.getChildren().add(textCryptView.getValue());

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

}
