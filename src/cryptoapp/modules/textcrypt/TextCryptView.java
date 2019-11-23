package cryptoapp.modules.textcrypt;

import cryptoapp.base.View;

public interface TextCryptView extends View {

    enum Mode {
        ENCRYPT, DECRYPT
    }

    void setCryptProgressIndicatorVisibility(boolean isVisible);

    void setKeyFieldAvailability(boolean b);

    void setCryptButtonMode(Mode mode);

    void setEncryptBytes(byte[] bytes);

    void setDecryptBytes(byte[] bytes);

    byte[] getEncryptBytes();

    byte[] getDecryptBytes();

    void setKeyBytes(byte[] key);

    byte[] getKeyBytes();

    void setUiAvailability(boolean isUiAvailable);

    void setErrorMsg(String message);
}
