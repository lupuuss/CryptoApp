package cryptoapp.modules.filecrypt;

import cryptoapp.base.Decrypter;
import cryptoapp.base.Encrypter;
import cryptoapp.base.KeyGenerator;
import cryptoapp.base.Presenter;
import cryptoapp.java.FxBiConsumer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

class FileCryptPresenter extends Presenter<FileCryptView> {

    private final String noCryptFileMsg = "You have to choose file to encryption/decryption and output directory!";

    private File currentCryptFile;
    private File currentKeyFile;
    private File currentOutputDirectory;

    private Encrypter encrypter;
    private Decrypter decrypter;
    private KeyGenerator keyGenerator;

    FileCryptPresenter(Encrypter encrypter, Decrypter decrypter, KeyGenerator keyGenerator) {
        this.encrypter = encrypter;
        this.decrypter = decrypter;
        this.keyGenerator = keyGenerator;
    }

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

    void encrypt() {

        if (currentCryptFile == null || currentOutputDirectory == null) {
            view.setErrorMsg(noCryptFileMsg);
            return;
        }

        view.setErrorMsg("");
        view.setUiAvailability(false);
        view.setEncryptProgressIndicatorVisibility(true);

        CompletableFuture.runAsync(() -> {

            try {

                InputStream keyStream;

                if (currentKeyFile == null) {

                    keyStream = new FileInputStream(
                            keyGenerator.generateFile(currentCryptFile.length())
                    );

                } else {
                    keyStream = new FileInputStream(currentKeyFile);
                }

                var outputFile = new File(currentOutputDirectory, currentCryptFile.getName() + "_encrypted");

                encrypter.encrypt(
                        new FileInputStream(currentCryptFile),
                        new FileOutputStream(outputFile),
                        keyStream
                );

            } catch (Exception e) {

                e.printStackTrace();
                throw new CompletionException(e);
            }

        }).whenComplete(new FxBiConsumer<>((o, exception) -> {

            view.setUiAvailability(true);
            view.setEncryptProgressIndicatorVisibility(false);

            if (exception != null) {

                view.setErrorMsg(exception.getMessage());
            }

        }));
    }

    void decrypt() {

        if (currentCryptFile == null || currentOutputDirectory == null) {
            view.setErrorMsg(noCryptFileMsg);
            return;
        }

        view.setErrorMsg("");
        view.setUiAvailability(false);
        view.setDecryptProgressIndicatorVisibility(true);

        CompletableFuture.runAsync(() -> {
            if (currentKeyFile == null) {
                throw new NoKeyFileException();
            }

            try {

                var outputFile = new File(currentOutputDirectory, currentCryptFile.getName() + "_decrypted");

                encrypter.encrypt(
                        new FileInputStream(currentCryptFile),
                        new FileOutputStream(outputFile),
                        new FileInputStream(currentKeyFile)
                );

            } catch (Exception e) {

                e.printStackTrace();
                throw new CompletionException(e);
            }

        }).whenComplete(new FxBiConsumer<>((o, exception) -> {

            view.setDecryptProgressIndicatorVisibility(false);
            view.setUiAvailability(true);

            if (exception != null) {

                view.setErrorMsg(exception.getMessage());
            }
        }));
    }

    private class NoKeyFileException extends CompletionException {

        NoKeyFileException() {
            super("You have to choose file with a decryption key!");
        }
    }
}
