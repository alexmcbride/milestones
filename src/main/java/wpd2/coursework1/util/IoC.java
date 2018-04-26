package wpd2.coursework1.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/*
 * Inversion of Control container singleton: https://martinfowler.com/articles/injection.html
 */
public class IoC {
    private static IoC singleInstance;

    public static synchronized IoC get() {
        if (singleInstance == null) {
            singleInstance = new IoC();
        }
        return singleInstance;
    }

    // Use concurrent hash map make thread-safe.
    private final Map<Class, Object> instances = new ConcurrentHashMap<>();

    /**
     * Creates a new IoC container.
     */
    private IoC() {
        // Blank
    }

    /**
     * Registers an instance of a class with the container.
     *
     * @param cls the class to register
     * @param instance the instance of the object to return
     */
    public void registerInstance(Class cls, Object instance) {
        instances.put(cls, instance);
    }

    /**
     * Gets a registered instance of a class.
     *
     * @param cls the class to get.
     * @return the registered instance.
     */
    public Object getInstance(Class cls) {
        return instances.get(cls);
    }

    /**
     * Unregister an instance of a class.
     *
     * @param cls the class to unregister.
     */
    public void unregisterInstance(Class cls) {
        instances.remove(cls);
    }
}
