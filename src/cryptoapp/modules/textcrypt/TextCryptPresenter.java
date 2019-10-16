package cryptoapp.modules.textcrypt;

import cryptoapp.base.Decrypter;
import cryptoapp.base.Encrypter;
import cryptoapp.base.KeyGenerator;
import cryptoapp.base.Presenter;
import cryptoapp.java.FxBiConsumer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@SuppressWarnings("FieldCanBeLocal")
class TextCryptPresenter extends Presenter<TextCryptView> {

    private TextCryptView.Mode currentMode = TextCryptView.Mode.ENCRYPT;
    private final Encrypter encrypter;
    private final Decrypter decrypter;
    private final KeyGenerator keyGenerator;

    private byte[] key;

    private final String noDataErrorMsg = "You have to enter message to encryption/decryption!";

    TextCryptPresenter(Encrypter encrypter, Decrypter decrypter, KeyGenerator keyGenerator) {
        this.encrypter = encrypter;
        this.decrypter = decrypter;
        this.keyGenerator = keyGenerator;
    }

    void changeCryptMode(TextCryptView.Mode mode) {

        view.setCryptButtonMode(mode);
        currentMode = mode;
    }

    void crypt() {

        var data = currentMode == TextCryptView.Mode.ENCRYPT
                ? view.getEncryptBytes()
                : view.getDecryptBytes();

        if (data == null || data.length == 0) {
            view.setErrorMsg(noDataErrorMsg);
            return;
        }

        view.setErrorMsg(null);
        view.setUiAvailability(false);
        view.setCryptProgressIndicatorVisibility(true);

        key = view.getKeyBytes();

        new CompletableFuture<byte[]>().completeAsync(() -> {

            if (currentMode == TextCryptView.Mode.DECRYPT && (key == null || key.length == 0)) {

                throw new NoKeyForDecryptionException();

            } else if (currentMode == TextCryptView.Mode.ENCRYPT && (key == null || key.length == 0)) {

                key = keyGenerator.generate(data.length);
            }

            if (currentMode == TextCryptView.Mode.ENCRYPT) {

                return encrypter.encrypt(data, key);

            } else {

                return decrypter.decrypt(data, key);
            }

        }).whenComplete(new FxBiConsumer<>((bytes, exception) -> {

            view.setUiAvailability(true);
            view.setCryptProgressIndicatorVisibility(false);
            view.setKeyBytes(key);

            if (exception != null) {

                view.setErrorMsg(exception.getMessage());

            } else if (currentMode == TextCryptView.Mode.ENCRYPT) {

                view.setDecryptBytes(bytes);

            } else {

                view.setEncryptBytes(bytes);
            }
        }));
    }

    private static class NoKeyForDecryptionException extends CompletionException {

        NoKeyForDecryptionException() {
            super("Key for decryption must be entered!");
        }
    }
}
