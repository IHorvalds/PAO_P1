package deprecated;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Store<T extends Queryable> {
    private ArrayList<T> store;

    public Store() {
        this.store = new ArrayList<T>();
    }

    public ArrayList<T> getStore() {
        return store;
    }

    public T[] getStoreAsArray(Class<T> cls) {
        T[] arr = (T[])Array.newInstance(cls, store.size());
        return this.store.toArray(arr);
    }
}
