package fnlib;

public class Maybe<T, E extends Error> {

    private final T t;
    private final E err;

    public Maybe(T t, E err) {
        this.t = t;
        this.err = err;
    }

    public boolean isErroneous() {
        return err != null;
    }

    public Error getError() {
        return err;
    }

    public boolean isPresent() {
        return t != null;
    }

    public T getValue() {
        return t;
    }
}
