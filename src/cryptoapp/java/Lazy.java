package cryptoapp.java;

/**
 * Implements lazy initialization pattern.
 */
public class Lazy <T> {

    public interface Producer<L> {

         L produce();
    }

    private T value;

    private final Producer<T> lazyProducer;

    public Lazy(Producer<T> lazyProducer) {
        this.lazyProducer = lazyProducer;
    }

    public T getValue() {

        if (value == null) {

            value = lazyProducer.produce();
        }

        return value;
    }

    public boolean isInitialized() {
        return value != null;
    }
}
