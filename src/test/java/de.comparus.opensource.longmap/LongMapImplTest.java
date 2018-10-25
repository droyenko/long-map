package de.comparus.opensource.longmap;

import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class LongMapImplTest {

    private LongMap map = new LongMapImpl();

    @Before
    public void setUp() {
        IntStream.range(1,101).forEach(i -> map.put(i, "value_" + i ));
    }

    @Test
    public void testPut() {
        assertEquals(100, map.size());
        map.put(101, "value_101");
        assertEquals(101, map.size());
        map.put(50, "duplicate");
        assertEquals(101, map.size());
        assertEquals("duplicate", map.get(50));
        map.put(500, "collision");
        assertEquals(102, map.size());

    }

    @Test
    public void testGet() {
        assertEquals("value_44", map.get(44));
        assertNull(map.get(200));
        map.put(500, "collision");
        assertEquals("collision", map.get(500));
    }

    @Test
    public void testRemove() {
        assertEquals("value_50", map.remove(50));
        assertEquals(99, map.size());
        assertNull(map.remove(500));

    }

    @Test
    public void testIsEmpty() {
        assertFalse(map.isEmpty());
        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testContainsKey() {
        assertTrue(map.containsKey(100));
        assertFalse(map.containsKey(101));
    }

    @Test
    public void testContainsValue() {
        assertTrue(map.containsValue("value_100"));
        assertFalse(map.containsValue("value_101"));
    }

    @Test
    public void testKeys() {
        long[] expected = new long[100];
        IntStream.range(1,101).forEach(i -> expected[i-1]=i);
        assertArrayEquals(expected, map.keys());
    }

    @Test
    public void testValues() {
        String[] expected = new String[100];
        IntStream.range(1,101).forEach(i -> expected[i-1]="value_" + i);
        assertArrayEquals(expected, map.values());
    }
}
