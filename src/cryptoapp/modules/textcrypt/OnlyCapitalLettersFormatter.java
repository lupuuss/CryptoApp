package cryptoapp.modules.textcrypt;

import javafx.scene.control.TextFormatter;

class OnlyCapitalLettersFormatter<T> extends TextFormatter<T> {

    OnlyCapitalLettersFormatter() {
        super((change -> {

            var text = change.getText().toUpperCase();
            var newText = new StringBuilder();

            text.chars().forEach((c) -> {

                if (c >= 65 && c < 91) {

                    newText.append((char)c);
                }
            });

            change.setText(newText.toString());
            return change;
        }));
    }

}
