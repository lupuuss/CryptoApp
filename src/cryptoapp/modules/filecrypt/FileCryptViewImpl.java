package cryptoapp.modules.filecrypt;

import cryptoapp.base.ActivityChild;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

public class FileCryptViewImpl extends ActivityChild implements FileCryptView {

    @FXML
    private Button cryptFileChooserButton;

    @FXML
    private Button encryptButton;

    @FXML
    private Button decryptButton;

    @FXML
    private Button keyFileChooserButton;

    @FXML
    private Button outputFolderChooserButton;

    @FXML
    private ProgressIndicator encryptionProgressIndicator;

    @FXML
    private ProgressIndicator decryptionProgressIndicator;

    private String defaultCryptFileChooserText;
    private String defaultKeyFileChooserText;
    private String defaultOutputDirectoryChooserText;


    private FileCryptPresenter presenter;

    private File lastPickedDir;

    private File chooseFile() {
        var fileChooser = new FileChooser();

        if (lastPickedDir != null) {
            fileChooser.setInitialDirectory(lastPickedDir);
        }

        var file = fileChooser.showOpenDialog(parentActivity.getCurrentStage());
        lastPickedDir = file.getParentFile();

        return file;
    }

    @FXML
    public void onCryptFileChooserButtonClick() {

        presenter.setCryptFile(chooseFile());
    }

    @FXML
    public void onKeyFileChooserButtonClick() {

        presenter.setKeyFile(chooseFile());
    }

    @FXML
    public void onOutputDirectoryChooserClick() {

        var chooser = new DirectoryChooser();

        if (lastPickedDir != null) {
            chooser.setInitialDirectory(lastPickedDir);
        }

        var dir = chooser.showDialog(parentActivity.getCurrentStage());

        lastPickedDir = dir.getParentFile();
        presenter.setOutputDirectory(dir);
    }

    @Override
    public void onStart() {

        presenter = new FileCryptPresenter();
        presenter.inject(this);

        defaultCryptFileChooserText = cryptFileChooserButton.getText();
        defaultKeyFileChooserText = keyFileChooserButton.getText();
        defaultOutputDirectoryChooserText = outputFolderChooserButton.getText();
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
}
