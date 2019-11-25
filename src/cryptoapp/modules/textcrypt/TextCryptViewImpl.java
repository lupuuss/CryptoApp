package cryptoapp.modules.textcrypt;

import cryptoapp.base.ActivityChild;
import cryptoapp.base.Cryptosystem;
import cryptoapp.model.crypt.Crypt;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * View associated with textcrypt.fxml
 */
@SuppressWarnings("FieldCanBeLocal")
public class TextCryptViewImpl extends ActivityChild implements TextCryptView {


    @FXML private TextArea encryptTextArea;
    private byte[] encryptBytes;

    @FXML private TextArea decryptTextArea;
    private byte[] decryptBytes;

    @FXML private TextField keyTextField;
    private byte[] keyBytes;

    @FXML private TextField keyLength;

    @FXML private Button cryptButton;
    @FXML private ProgressIndicator cryptIndicator;
    @FXML private Label errorMsgLabel;

    private String defaultKeyLengthMessage;
    private final String keyLengthUnavailable = "Key length cannot be set...";

    private TextCryptPresenter presenter;

    @Override
    public void onStart() {

        presenter = new TextCryptPresenter(
                Crypt.getOneTimePad()
        );
        presenter.inject(this);

        encryptTextArea.textProperty().addListener((observableValue, oldStr, newStr) -> encryptBytes = newStr.getBytes());

        decryptTextArea.textProperty().addListener((observableValue, oldStr, newStr) -> decryptBytes = newStr.getBytes());

        keyTextField.textProperty().addListener((observableValue, oldStr, newStr) -> keyBytes = newStr.getBytes());

        defaultKeyLengthMessage = keyLength.getPromptText();
    }

    public void changeCryptosystem(Cryptosystem cryptosystem) {

        presenter.changeCryptosystem(cryptosystem);
    }

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
    public void setEncryptBytes(byte[] bytes) {
        encryptTextArea.setText(new String(bytes));
        encryptBytes = bytes;
    }

    @Override
    public void setDecryptBytes(byte[] bytes) {
        decryptTextArea.setText(new String(bytes));
        decryptBytes = bytes;
    }

    @Override
    public byte[] getEncryptBytes() {
        return encryptBytes;
    }

    @Override
    public byte[] getDecryptBytes() {
        return  decryptBytes;
    }

    @Override
    public void setKeyBytes(byte[] key) {

        keyTextField.setText(new String(key));
        keyBytes = key;
    }

    @Override
    public byte[] getKeyBytes() {
        return keyBytes;
    }

    @Override
    public void setUiAvailability(boolean isUiAvailable) {

        cryptButton.setDisable(!isUiAvailable);
        encryptTextArea.setDisable(!isUiAvailable);
        decryptTextArea.setDisable(!isUiAvailable);
        keyTextField.setDisable(!isUiAvailable);
        keyLength.setDisable(!isUiAvailable);
    }

    @Override
    public void setErrorMsg(String message) {

        errorMsgLabel.setText(message);
    }

    @Override
    public void setKeyFieldAvailability(boolean b) {
        keyTextField.setDisable(!b);
    }

    @Override
    public String getKeyLengthString() {
        return keyLength.getText();
    }

    @Override
    public void setKeyLengthFieldAvailability(boolean isKeyLengthAvailable) {
        if (isKeyLengthAvailable) {
            keyLength.setPromptText(defaultKeyLengthMessage);
        } else {
            keyLength.setPromptText(keyLengthUnavailable);
        }
        keyLength.setDisable(!isKeyLengthAvailable);
    }
}
