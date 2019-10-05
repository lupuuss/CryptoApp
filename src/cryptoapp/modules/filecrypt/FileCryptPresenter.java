package cryptoapp.modules.filecrypt;

import cryptoapp.base.Presenter;

import java.io.File;

class FileCryptPresenter extends Presenter<FileCryptView> {

    private File currentCryptFile;
    private File currentKeyFile;
    private File currentOutputDirectory;

    void setCryptFile(File file) {

        if (file != null) {

            currentCryptFile = file;
            view.setCryptFileButtonText(file.getAbsolutePath());
        }
    }

    void setKeyFile(File file) {

        if (file != null) {

            currentKeyFile = file;
            view.setKeyFileButtonText(file.getAbsolutePath());
        }
    }

    void setOutputDirectory(File dir) {

        if (dir != null) {

            currentOutputDirectory = dir;
            view.setOutputDirectoryButtonText(dir.getAbsolutePath());
        }
    }
}
