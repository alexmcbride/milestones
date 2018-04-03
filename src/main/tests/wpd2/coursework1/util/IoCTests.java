package wpd2.coursework1.util;

import org.junit.Test;
import wpd2.coursework1.util.IoC;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class IoCTests {
    @Test
    public void testGet() {

    }

    @Test
    public void testRegisterInstance() {
        Object object = new Object();
        IoC.get().registerInstance(Object.class, object);

        Object result = IoC.get().getInstance(Object.class);

        assertEquals(object, result);
    }

    @Test
    public void testGetInstance() {
        Object object = new Object();
        IoC.get().registerInstance(Object.class, object);

        Object result = IoC.get().getInstance(Object.class);

        assertEquals(object, result);
    }

    @Test
    public void testUnregisterInstance() {
        IoC.get().registerInstance(Object.class, new Object());
        IoC.get().unregisterInstance(Object.class);

        Object result = IoC.get().getInstance(Object.class);

        assertNull(result);
    }
}
