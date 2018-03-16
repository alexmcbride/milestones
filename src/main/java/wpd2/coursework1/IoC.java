package wpd2.coursework1;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/*
 * Inversion of Control container singleton.
 */
public class IoC {
    private static IoC singleInstance;

    public static synchronized IoC get() {
        if (singleInstance == null) {
            singleInstance = new IoC();
        }
        return singleInstance;
    }

    // Use concurrent hash map to make this thread-safe.
    private final Map<Class, Object> instances = new ConcurrentHashMap<>();

    private IoC() {
        // Blank
    }

    public void registerInstance(Class cls, Object instance) {
        instances.put(cls, instance);
    }

    public Object getInstance(Class cls) {
        return instances.get(cls);
    }

    public void unregisterInstance(Class cls) {
        instances.remove(cls);
    }
}
