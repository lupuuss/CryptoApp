package cryptoapp.modules.filecrypt;

import cryptoapp.base.*;
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
    private Cryptosystem cryptosystem;

    FileCryptPresenter(Cryptosystem cryptosystem) {
        this.cryptosystem = cryptosystem;
    }

    @Override
    public void inject(FileCryptView view) {
        super.inject(view);

        changeCryptosystem(cryptosystem);
    }

    void changeCryptosystem(Cryptosystem cryptosystem) {
        this.encrypter = cryptosystem.getEncrypter();
        this.decrypter = cryptosystem.getDecrypter();
        this.keyGenerator = cryptosystem.getKeyGenerator();
        this.cryptosystem = cryptosystem;

        if (cryptosystem.isKeyLengthConst()) {
            view.setKeyLength(String.valueOf(cryptosystem.getDefaultKeyLength()));
        } else {
            view.setKeyLength("");
        }

        view.setKeyLengthAvailability(cryptosystem.isKeyLengthConst());
    }

    void setCryptFile(File file) {

        currentCryptFile = file;

        if (file != null) {
            view.setCryptFileButtonText(file.getAbsolutePath());
        } else {
            view.setCryptFileButtonTextDefault();
        }
    }

    void setKeyFile(File file) {

        currentKeyFile = file;

        if (file != null) {

            view.setKeyFileButtonText(file.getAbsolutePath());
        } else {

            view.setKeyFileButtonTextDefault();
        }
    }

    void setOutputDirectory(File dir) {

        currentOutputDirectory = dir;

        if (dir != null) {

            view.setOutputDirectoryButtonText(dir.getAbsolutePath());

        } else {
            view.setOutputDirectoryButtonTextDefault();
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

                int keyLength = 0;

                if (cryptosystem.isKeyLengthConst()) {
                    keyLength = Integer.parseInt(view.getKeyLength());
                    if (keyLength % 32 != 0 || keyLength < 64) {
                        throw new IllegalStateException("Key length must be divisible by 32 and at least 64");
                    }
                }

                if (currentKeyFile == null || !cryptosystem.isNoGeneratedKeyAllowed()) {

                    File keyFile = new File(currentOutputDirectory, createKeyName(currentCryptFile.getName()));
                    keyGenerator.generateFile(keyLength, currentCryptFile, keyFile);
                    keyStream = new FileInputStream(keyFile);

                } else {

                    keyStream = new FileInputStream(currentKeyFile);
                }

                var outputFile = new File(currentOutputDirectory, createEncryptName(currentCryptFile.getName()));

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
            } else {

                view.openFileDesktop(currentOutputDirectory);
                cleanUiAfterCrypt();
            }

        }));
    }

    private String createKeyName(String currentName) {

        return currentName + ".key";
    }

    private String createEncryptName(String currentName) {
        return currentName + ".encrypted";
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

                var outputFile = new File(currentOutputDirectory, createDecryptName(currentCryptFile.getName()));

                decrypter.decrypt(
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
            } else {

                view.openFileDesktop(currentOutputDirectory);
                cleanUiAfterCrypt();
            }
        }));
    }

    private String createDecryptName(String currentName) {

        if (currentName.endsWith(".encrypted")) {

            currentName = currentName.substring(0, currentName.length() - 10);

        }

        int lastIndex = currentName.lastIndexOf('.');
        currentName = currentName.substring(0, lastIndex) + "_decrypted" + currentName.substring(lastIndex);

        return currentName;
    }

    private void cleanUiAfterCrypt() {
        currentOutputDirectory = null;
        currentKeyFile = null;
        currentCryptFile = null;

        view.setOutputDirectoryButtonTextDefault();
        view.setCryptFileButtonTextDefault();
        view.setKeyFileButtonTextDefault();
    }

    private static class NoKeyFileException extends CompletionException {

        NoKeyFileException() {
            super("You have to choose file with a decryption key!");
        }
    }
}
