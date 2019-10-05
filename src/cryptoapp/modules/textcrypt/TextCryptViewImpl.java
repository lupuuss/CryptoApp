package cryptoapp.modules.textcrypt;

import cryptoapp.base.ActivityChild;
import cryptoapp.model.crypt.Crypt;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * View associated with textcrypt.fxml
 */
public class TextCryptViewImpl extends ActivityChild implements TextCryptView {

    @FXML
    private TextArea encryptTextArea;

    @FXML
    private TextArea decryptTextArea;

    @FXML
    private Button cryptButton;

    @FXML
    private ProgressIndicator cryptIndicator;

    @FXML
    private TextField keyTextField;

    @FXML
    private Label errorMsgLabel;

    private TextCryptPresenter presenter;

    @FXML
    public void onEncryptTextAreaClick() {

        presenter.changeCryptMode(Mode.ENCRYPT);
    }

    @FXML
    public void onCryptButtonClick() {

        presenter.crypt();
    }

    @FXML
    public void onDecryptTextAreaClick() {

        presenter.changeCryptMode(Mode.DECRYPT);
    }

    @Override
    public void onStart() {

        presenter = new TextCryptPresenter(
                Crypt.getOneTimePadEncrypter(),
                Crypt.getOneTimePadDecrypter(),
                Crypt.getKeyGenerator()
        );
        presenter.inject(this);
    }

    @Override
    public void setCryptProgressIndicatorVisibility(boolean isVisible) {

        cryptIndicator.setVisible(isVisible);
    }

    @Override
    public void setCryptButtonMode(Mode mode) {

        if (mode == Mode.ENCRYPT) {

            cryptButton.setText("Encrypt");
        } else {

            cryptButton.setText("Decrypt");
        }
    }

    @Override
    public void setEncryptAreaText(String text) {
        encryptTextArea.setText(text);
    }

    @Override
    public void setDecryptAreaText(String text) {
        decryptTextArea.setText(text);
    }

    @Override
    public String getEncryptText() {

        return encryptTextArea.getText();
    }

    @Override
    public String getDecryptText() {

        return decryptTextArea.getText();
    }

    @Override
    public void setKeyText(String key) {
        keyTextField.setText(key);
    }

    @Override
    public String getKeyText() {

        return keyTextField.getText();
    }

    @Override
    public void setUiAvailability(boolean isUiAvailable) {

        cryptButton.setDisable(!isUiAvailable);
        encryptTextArea.setDisable(!isUiAvailable);
        decryptTextArea.setDisable(!isUiAvailable);
        keyTextField.setDisable(!isUiAvailable);
    }

    @Override
    public void setErrorMsg(String message) {

        errorMsgLabel.setText(message);
    }
}
