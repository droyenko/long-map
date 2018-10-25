package de.comparus.opensource.longmap;

import java.util.Arrays;
import java.util.HashMap;

public class LongMapImpl<V> implements LongMap<V> {
    private int size;
    private int DEFAULT_CAPACITY = 16;
    private Entry<V>[] entries = new Entry[DEFAULT_CAPACITY];

    public V put(long key, V value) {
        boolean insert = true;
        for (int i = 0; i < size; i++) {
            if (entries[i].getKey() == key) {
                entries[i].setValue(value);
                insert = false;
            }
        }
        if (insert) {
            ensureCapacity();
            entries[size++] = new Entry<>(key, value);
        }
        return value;
    }

    private void ensureCapacity() {
        if (size == entries.length) {
            int newSize = entries.length * 2;
            entries = Arrays.copyOf(entries, newSize);
        }
    }

    public V get(long key) {
        for (int i = 0; i < size; i++) {
            if (entries[i] != null) {
                if (entries[i].getKey() == key) {
                    return entries[i].getValue();
                }
            }
        }
        return null;
    }

    public V remove(long key) {
        V value = null;
        for (int i = 0; i < size; i++) {
            if (entries[i].getKey() == key) {
                value = entries[i].getValue();
                entries[i] = null;
                size--;
                compressArray(i);
            }
        }
        return value;
    }

    private void compressArray(int start) {
        for (int i = start; i < size; i++) {
            entries[i] = entries[i + 1];
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(long key) {
        for (int i = 0; i < size; i++) {
            if (entries[i].getKey() == key) {
                return true;
            }
        }
        return false;
    }

    public boolean containsValue(V value) {
        for (int i = 0; i < size; i++) {
            if (entries[i].getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    public long[] keys() {
        long[] result = new long[size];
        for (int i = 0; i < size; i++) {
            result[i] = entries[i].getKey();
        }
        return result;
    }

    public V[] values() {
        V[] result = (V[]) new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = entries[i].getValue();
        }
        return result;
    }

    public long size() {
        return size;
    }

    public void clear() {
        size = 0;
        Arrays.fill(entries, null);
    }

    static class Entry<V> {
        private final long key;
        private V value;

        Entry(long key, V value) {
            this.key = key;
            this.value = value;
        }

        long getKey() {
            return key;
        }

        V getValue() {
            return value;
        }

        void setValue(V value) {
            this.value = value;
        }
    }

    HashMap map = new HashMap();
}
