package cryptoapp.modules.textcrypt;

import cryptoapp.base.*;
import cryptoapp.java.FxBiConsumer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@SuppressWarnings("FieldCanBeLocal")
class TextCryptPresenter extends Presenter<TextCryptView> {

    private TextCryptView.Mode currentMode = TextCryptView.Mode.ENCRYPT;
    private Encrypter encrypter;
    private Decrypter decrypter;
    private KeyGenerator keyGenerator;
    private Cryptosystem cryptosystem;

    private byte[] key;

    private final String noDataErrorMsg = "You have to enter message to encryption/decryption!";

    TextCryptPresenter(Cryptosystem cryptosystem) {
        this.cryptosystem = cryptosystem;
    }

    @Override
    public void inject(TextCryptView view) {
        super.inject(view);
        changeCryptosystem(cryptosystem);
    }

    void changeCryptosystem(Cryptosystem cryptosystem) {

        this.encrypter = cryptosystem.getEncrypter();
        this.decrypter = cryptosystem.getDecrypter();
        this.keyGenerator = cryptosystem.getKeyGenerator();
        this.cryptosystem = cryptosystem;

        if (currentMode == TextCryptView.Mode.ENCRYPT && !cryptosystem.isNoGeneratedKeyAllowed()) {
            view.setKeyFieldAvailability(false);
        } else {
            view.setKeyFieldAvailability(true);
        }

        view.setKeyLengthFieldAvailability(cryptosystem.isKeyLengthConst());
    }

    void changeCryptMode(TextCryptView.Mode mode) {

        view.setCryptButtonMode(mode);
        currentMode = mode;
        view.setKeyFieldAvailability(cryptosystem.isNoGeneratedKeyAllowed() || mode != TextCryptView.Mode.ENCRYPT);
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

            int keyLength = 0;

            if (cryptosystem.isKeyLengthConst()) {
                keyLength = Integer.parseInt(view.getKeyLengthString());
                if (keyLength % 32 != 0 || keyLength < 64) {
                    throw new IllegalStateException("Key length must be divisible by 32 and at least 64");
                }
            }

            if (currentMode == TextCryptView.Mode.DECRYPT && (key == null || key.length == 0)) {

                throw new NoKeyForDecryptionException();

            } else if (currentMode == TextCryptView.Mode.ENCRYPT
                    && (!cryptosystem.isNoGeneratedKeyAllowed() || key == null || key.length == 0)) {

                key = keyGenerator.generate(data, keyLength);
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
