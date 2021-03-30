import java.lang.reflect.Array;

public class Store<T extends Queryable> {
    public T[] store;

    public Store(Class<T> cls) {
        this.store = (T[])Array.newInstance(cls, 0);
    }
}
