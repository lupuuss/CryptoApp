package cryptoapp.java;

public class Lazy <T> {

    public interface Producer<L> {

         L produce();
    }

    private T value;

    private Producer<T> lazyProducer;

    public Lazy(Producer<T> lazyProducer) {
        this.lazyProducer = lazyProducer;
    }

    public T getValue() {

        if (value == null) {

            value = lazyProducer.produce();
        }

        return value;
    }
}
