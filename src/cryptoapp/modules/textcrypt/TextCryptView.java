package cryptoapp.modules.textcrypt;

import cryptoapp.base.View;

public interface TextCryptView extends View {

    enum Mode {
        ENCRYPT, DECRYPT
    }

    void setCryptProgressIndicatorVisibility(boolean isVisible);

    void setCryptButtonMode(Mode mode);

    void setEncryptAreaText(String text);

    void setDecryptAreaText(String text);

    String getEncryptText();

    String getDecryptText();

    void setKeyText(String key);

    String getKeyText();

    void setUiAvailability(boolean isUiAvailable);

    void setErrorMsg(String message);
}
