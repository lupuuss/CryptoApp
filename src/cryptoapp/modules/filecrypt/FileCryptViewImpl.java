package cryptoapp.modules.filecrypt;

import cryptoapp.base.ActivityChild;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

public class FileCryptViewImpl extends ActivityChild implements FileCryptView {

    @FXML
    private Button fileChooserButton;

    @FXML
    private Button encryptButton;

    @FXML
    private Button decryptButton;

    @FXML
    private TextField keyText;

    @FXML
    private Button keyFileChooserButton;

    @FXML
    private Button outputFolderChooserButton;

    @FXML
    private ProgressIndicator encryptionProgressIndicator;

    @FXML
    private ProgressIndicator decryptionProgressIndicator;

    @Override
    public void onStart() {

    }
}
