package cryptoapp.base;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
    Wraps scene management. Scene root is loaded from FXML.
 **/
public abstract class Activity<T> {

    private String title;
    private Scene scene;
    private Parent parent;
    private T controller;

    public Activity(String fxml, String title) throws Exception {

        this.title = title;
        var loader = new FXMLLoader(getClass().getResource(fxml));
        parent = loader.load();
        controller = loader.getController();
        scene = new Scene(parent);
    }

    public void setOnStage(Stage primaryStage) throws Exception {
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        onStart();
    }

    public void onStart() throws Exception { }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public T getController() {
        return controller;
    }

    public void setController(T controller) {
        this.controller = controller;
    }
}
