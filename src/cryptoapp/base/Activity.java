package cryptoapp.base;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Wraps scene management. Scene root is loaded from FXML.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class Activity<T> {

    protected String title;
    protected Scene scene;
    protected Parent parent;
    protected T view;

    private Stage currentStage;

    public Activity(String fxml, String title) throws Exception {

        this.title = title;
        var loader = new FXMLLoader(getClass().getResource(fxml));
        parent = loader.load();
        view = loader.getController();
        scene = new Scene(parent);
    }

    public void setOnStage(Stage primaryStage) {
        currentStage = primaryStage;
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        onStart();
    }

    public abstract void onStart();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Scene getScene() {
        return scene;
    }

    public Parent getParent() {
        return parent;
    }

    public T getView() {
        return view;
    }

    public Stage getCurrentStage() {
        return currentStage;
    }

    public void openFileDesktop(File file) {

        try {

            Desktop.getDesktop().open(file);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
