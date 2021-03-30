import java.util.*;
import errors.ObjectNotFound;

public abstract class Queryable implements Comparable<Queryable> {

    static Hashtable<Class, Store> _stores = new Hashtable<Class, Store>();
    static <T extends Queryable> void addNewStore(Class<T> cls) {
        if (!_stores.containsKey(cls)) {
            _stores.put(cls, new Store<T>(cls));
        }
    }

    public Integer id;

    @Override
    public int compareTo(Queryable ob) { // just sorting by id... Basically by who was added first.
        return this.id.compareTo(ob.id);
    }

    static protected Scanner sc = new Scanner(System.in); 

    public static <T extends Queryable> void insert(T ob, Class<T> cls) {
        var _store = _stores.get(cls).store;
        if (_store != null) {
            Integer n = _store.length;
            _store = Arrays.copyOf(_store, n + 1);
            _store[n] = ob;
            _stores.get(cls).store = _store;
        } else {
            _store = new Queryable[1];
            _store[0] = ob;
            _stores.get(cls).store = _store;
        }
    }

    public static <T extends Queryable> T get(Integer id, Class<T> cls) {
        var _store = _stores.get(cls).store;
        for(var ob : _store) {
            if (ob.id == id) {
                return (T)ob;
            }
        }
        throw new ObjectNotFound("No object with id " + id);
    }

    public static <T extends Queryable> void update(T new_object, Class<T> cls) {
        var _store = _stores.get(cls).store;
        for(var ob : _store) {
            if (ob.id == new_object.id) {
                ob = new_object;
            }
        }
        throw new ObjectNotFound("No object with id " + new_object.id + ". Try inserting the object.");
    }

    public static <T extends Queryable> void delete(T ob, Class<T> cls) {
        var _store = _stores.get(cls).store;
        Queryable[] new_store = new Queryable[_store.length - 1];

        Integer i = 0;
        for(var o : _store) {
            if (o.id != ob.id) {
                new_store[i] = o;
                i++;
            }
        }

        _stores.get(cls).store = new_store;
    }

    public static <T extends Queryable> T[] getAll(Class<T> cls) {
        var _store = _stores.get(cls).store;
        return (T[])Arrays.copyOf(_store, _store.length);
    }

    // abstract public String serialize();

    // abstract public void save();
}
