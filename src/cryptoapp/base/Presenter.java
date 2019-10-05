package cryptoapp.base;

public abstract class Presenter<ViewType extends View> {

    protected ViewType view;

    public void inject(ViewType view) {

        this.view = view;
    }
}
