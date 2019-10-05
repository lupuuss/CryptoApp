package cryptoapp.java;

import javafx.application.Platform;

import java.util.function.BiConsumer;

public class FxUiRunner<T, U> implements BiConsumer<T, U> {

    private BiConsumer<T, U> task;

    public FxUiRunner(BiConsumer<T, U> task) {

        this.task = task;
    }

    @Override
    public void accept(T obj, U obj2) {

        Platform.runLater(() -> {
            task.accept(obj, obj2);
        });
    }
}
