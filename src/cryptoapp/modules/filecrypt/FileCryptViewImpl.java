package cryptoapp.modules.filecrypt;

import cryptoapp.base.ActivityChild;
import cryptoapp.model.crypt.Crypt;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * View associated with filecrypt.fxml file
 */
public class FileCryptViewImpl extends ActivityChild implements FileCryptView {

    @FXML private Button cryptFileChooserButton;
    @FXML private Button encryptionButton;
    @FXML private Button decryptionButton;
    @FXML private Button keyFileChooserButton;
    @FXML private Button outputFolderChooserButton;
    @FXML private ProgressIndicator encryptionProgressIndicator;
    @FXML private ProgressIndicator decryptionProgressIndicator;
    @FXML private Label errorMsgLabel;

    private String defaultCryptFileChooserText;
    private String defaultKeyFileChooserText;
    private String defaultOutputDirectoryChooserText;

    private FileCryptPresenter presenter;

    private File lastPickedDir;

    @Override
    public void onStart() {

        presenter = new FileCryptPresenter(
                Crypt.getOneTimePadEncrypter(),
                Crypt.getOneTimePadDecrypter(),
                Crypt.getKeyGenerator()
        );

        presenter.inject(this);

        defaultCryptFileChooserText = cryptFileChooserButton.getText();
        defaultKeyFileChooserText = keyFileChooserButton.getText();
        defaultOutputDirectoryChooserText = outputFolderChooserButton.getText();
    }

    private File chooseFile(String windowName) {
        var fileChooser = new FileChooser();

        fileChooser.setTitle(windowName);

        if (lastPickedDir != null) {
            fileChooser.setInitialDirectory(lastPickedDir);
        }

        var file = fileChooser.showOpenDialog(parentActivity.getCurrentStage());

        if (file != null && file.getParentFile() != null) {
            lastPickedDir = file.getParentFile();
        }

        return file;
    }

    @FXML
    public void onCryptFileChooserButtonClick() {

        presenter.setCryptFile(chooseFile(defaultCryptFileChooserText));
    }

    @FXML
    public void onKeyFileChooserButtonClick() {

        presenter.setKeyFile(chooseFile(defaultKeyFileChooserText));
    }

    @FXML
    public void onOutputDirectoryChooserClick() {

        var chooser = new DirectoryChooser();

        chooser.setTitle(defaultOutputDirectoryChooserText);

        if (lastPickedDir != null) {
            chooser.setInitialDirectory(lastPickedDir);
        }

        var dir = chooser.showDialog(parentActivity.getCurrentStage());

        if (dir != null) {
            lastPickedDir = dir;
        }

        presenter.setOutputDirectory(dir);
    }

    @FXML
    public void onEncryptionButtonClick() {

        presenter.encrypt();
    }

    @FXML
    public void onDecryptionButtonClick() {

        presenter.decrypt();
    }

    @Override
    public void setCryptFileButtonText(String text) {

        cryptFileChooserButton.setText(text);
    }

    @Override
    public void setKeyFileButtonText(String text) {
        keyFileChooserButton.setText(text);
    }

    @Override
    public void setOutputDirectoryButtonText(String text) {
        outputFolderChooserButton.setText(text);
    }

    @Override
    public void setEncryptProgressIndicatorVisibility(boolean isVisible) {
        encryptionProgressIndicator.setVisible(isVisible);
    }

    @Override
    public void setDecryptProgressIndicatorVisibility(boolean isVisible) {
        decryptionProgressIndicator.setVisible(isVisible);
    }

    @Override
    public void setUiAvailability(boolean isAvailable) {

        cryptFileChooserButton.setDisable(!isAvailable);
        keyFileChooserButton.setDisable(!isAvailable);
        outputFolderChooserButton.setDisable(!isAvailable);
        encryptionButton.setDisable(!isAvailable);
        decryptionButton.setDisable(!isAvailable);
    }

    @Override
    public void setErrorMsg(String message) {

        errorMsgLabel.setText(message);
    }

    @Override
    public void setCryptFileButtonTextDefault() {
        cryptFileChooserButton.setText(defaultCryptFileChooserText);
    }

    @Override
    public void setKeyFileButtonTextDefault() {
        keyFileChooserButton.setText(defaultKeyFileChooserText);
    }

    @Override
    public void setOutputDirectoryButtonTextDefault() {
        outputFolderChooserButton.setText(defaultOutputDirectoryChooserText);
    }
}
