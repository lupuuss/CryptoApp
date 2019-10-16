package cryptoapp.modules.filecrypt;

import cryptoapp.base.View;

interface FileCryptView extends View {

    void setCryptFileButtonText(String text);

    void setKeyFileButtonText(String text);

    void setOutputDirectoryButtonText(String text);

    void setEncryptProgressIndicatorVisibility(boolean isVisible);

    void setDecryptProgressIndicatorVisibility(boolean isVisible);

    void setUiAvailability(boolean isAvailable);

    void setErrorMsg(String message);

    void setCryptFileButtonTextDefault();

    void setKeyFileButtonTextDefault();

    void setOutputDirectoryButtonTextDefault();
}
