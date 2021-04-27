import java.io.*;
import java.lang.reflect.*;
import java.text.*;
import java.util.*;
import java.util.stream.Stream;

import errors.ObjectNotFound;

public abstract class Queryable implements Comparable<Queryable> {

    static Hashtable<Class, Store> _stores = new Hashtable<Class, Store>();
    static <T extends Queryable> void addNewStore(Class<T> cls) {
        if (!_stores.containsKey(cls)) {
            _stores.put(cls, new Store<T>());
        }
    }

    public Integer id;

    @Override
    public int compareTo(Queryable ob) { // just sorting by id... Basically by who was added first.
        return this.id.compareTo(ob.id);
    }

    static protected Scanner sc = new Scanner(System.in); 

    public static <T extends Queryable> void insert(T ob) {
        Class<T> cls = (Class<T>)ob.getClass();
        Store<T> _store = (Store<T>)_stores.get(cls);
        _store.getStore().add(ob);
    }

    public static <T extends Queryable> T get(Integer id, Class<T> cls) {
        Store<T> _store = (Store<T>)_stores.get(cls);

        if (_store != null) {
            for(var ob : _store.getStore()) {
                if (ob.id == id) {
                    return ob;
                }
            }
        }
        throw new ObjectNotFound("No object with id " + id);
    }

    public static <T extends Queryable> void update(T new_object) {
        Class<T> cls = (Class<T>)new_object.getClass();
        Store<T> _store = (Store<T>)_stores.get(cls);

        if (_store != null) {
            for(var ob : _store.getStore()) {
                if (ob.id == new_object.id) {
                    ob = new_object;
                }
            }
            throw new ObjectNotFound("No object with id " + new_object.id + ". Try inserting the object.");
        }
    }

    public static <T extends Queryable> void delete(T ob) {
        Class<T> cls = (Class<T>)ob.getClass();
        Store<T> _store = _stores.get(cls);

        if (_store != null) {
            _store.getStore().remove(ob);
        }
    }

    public static <T extends Queryable> T[] getAll(Class<T> cls) {
        Store<T> _store = (Store<T>)_stores.get(cls);

        if (_store != null) {
            return _store.getStoreAsArray(cls);
        }

        return (T[])Array.newInstance(cls, 0);
    }


    // turn object into comma separated string
    public static <T extends Queryable> String serialize(T ob) {

        Class<T> cls = (Class<T>)ob.getClass();
        ArrayList<Field> allFields = new ArrayList<Field>();
        try {
            allFields.add(Queryable.class.getDeclaredField("id"));
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        }
        allFields.addAll(new ArrayList<Field>(Arrays.asList(cls.getDeclaredFields())));
        


        String[] allValues = allFields.stream()
                                .filter(f -> (f.getModifiers() != Modifier.STATIC))
                                .map(f -> {
            try {
                f.setAccessible(true);

                if (f.getType() == Date.class) {
                    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    return formatter.format(f.get(ob));
                }

                return f.get(ob).toString();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return "";
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return "";
            }
        }).toArray(String[]::new);

        String str = String.join(",", allValues);

        return str;
    }

    // turn comma separated string into object. If the object id > the lastId of the class, update that to
    public static <T extends Queryable> T deserialize(Class<T> cls, String s) throws IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException {
        ArrayList<Field> allFields = new ArrayList<Field>();
        try {
            allFields.add(Queryable.class.getDeclaredField("id"));
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        }
        allFields.addAll(new ArrayList<Field>(Arrays.asList(cls.getDeclaredFields())));

        Field[] fields = allFields.stream().filter(f -> (f.getModifiers() != Modifier.STATIC)).toArray(Field[]::new);


        String[] allValues = s.split(",");

        assert(fields.length == allValues.length) : "Different number of fields for instance of " + cls.getSimpleName();

        T ob = cls.getDeclaredConstructor().newInstance();

        for(Integer i = 0; i < fields.length; ++i) {
            Field f = fields[i];
            f.setAccessible(true);

            switch (f.getType().getSimpleName())
            {
                case "String":
                f.set(ob, allValues[i]);
                break;

                case "Double":
                Double d = Double.valueOf(allValues[i]);
                f.set(ob, d);
                break;

                case "Integer":
                Integer integer = Integer.valueOf(allValues[i]);
                f.set(ob, integer);
                break;

                case "Date":
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                try {
                    f.set(ob, formatter.parse(allValues[i]));
                } catch (Exception e) {
                    f.set(ob, null);
                }
                break;
            }            

        }

        try {
            Field f = cls.getDeclaredField("lastId");
            if (ob.id >= (Integer)f.get(ob)) {
                f.set(ob, ob.id + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ob;
    }


    private static void write_out(File f, String buff, Boolean overwrite) {
        
        try (FileWriter fw = new FileWriter(f, !overwrite)) {
            fw.write(buff);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static <T extends Queryable> void saveAll(Class<T> cls) {
        T[] arr = T.getAll(cls);

        File f = new File(System.getProperty("user.dir") + "/data/" + cls.getSimpleName() + ".csv");
        
        if (!f.exists()) {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            write_out(f, "", true);
        } else {
            write_out(f, "", true);
        }
        for(T el : arr) {
            String s = T.serialize(el);
            write_out(f, s + "\n", false);
        }
    }

    public static <T extends Queryable> void readAll(Class<T> cls) throws Exception {
        File f = new File(System.getProperty("user.dir") + "/data/" + cls.getSimpleName() + ".csv");

        if (f.exists()) {
            try {
                Scanner fileScanner = new Scanner(f);

                while(fileScanner.hasNextLine()) {
                    String s = fileScanner.nextLine();
                    T ob = T.deserialize(cls, s);
                    T.insert(ob);
                }

                fileScanner.close();
                fileScanner = null;
            } catch (Exception e) {
                throw e;
            }
        }
    }
}
