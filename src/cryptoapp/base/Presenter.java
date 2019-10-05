package cryptoapp.base;

/**
 * Provides injection method for every Presenter
 */
public abstract class Presenter<ViewType extends View> {

    protected ViewType view;

    public void inject(ViewType view) {

        this.view = view;
    }
}
