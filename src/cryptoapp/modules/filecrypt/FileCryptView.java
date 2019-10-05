package cryptoapp.modules.filecrypt;

import cryptoapp.base.View;

public interface FileCryptView extends View {

    void setCryptFileButtonText(String text);

    void setKeyFileButtonText(String text);

    void setOutputDirectoryButtonText(String text);

    void setEncryptProgressIndicatorVisibility(boolean isVisible);

    void setDecryptProgressIndicatorVisibility(boolean isVisible);

    void setUiAvailability(boolean isAvailable);
}
