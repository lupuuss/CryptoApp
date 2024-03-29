package cryptoapp.java;

import javafx.application.Platform;

import java.util.function.BiConsumer;

/**
 * Runs BiConsumer action on JavaFx UI Thread.
 */
public class FxBiConsumer<T, U> implements BiConsumer<T, U> {

    private final BiConsumer<T, U> task;

    public FxBiConsumer(BiConsumer<T, U> task) {

        this.task = task;
    }

    @Override
    public void accept(T obj, U obj2) {

        Platform.runLater(() -> task.accept(obj, obj2));
    }
}
