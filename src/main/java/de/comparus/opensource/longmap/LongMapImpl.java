package de.comparus.opensource.longmap;

import java.util.Arrays;

public class LongMapImpl<V> implements LongMap<V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Entry<V>[] entries;

    private int size;
    private int treshold;
    private final float loadFactor;

    public LongMapImpl() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        treshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        entries = new Entry[DEFAULT_INITIAL_CAPACITY];
    }

    public V put(long key, V value) {
        int bucket = indexFor(key, entries.length);
        for (Entry<V> e = entries[bucket]; e != null; e = e.next) {
            if (e.key == key) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        addEntry(key, value, bucket);
        return null;
    }

    private int indexFor(long key, int length) {
        return (int) (key < length ? key : key % length);
    }

    public V get(long key) {
        int bucket = indexFor(key, entries.length);
        for (Entry<V> e = entries[bucket]; e != null; e = e.next) {
            if (e.key == key)
                return e.value;
        }
        return null;
    }

    public V remove(long key) {
        int bucket = indexFor(key, entries.length);
        Entry<V> prev = entries[bucket];
        Entry<V> e = prev;

        while (e != null) {
            Entry<V> next = e.next;
            if (e.key == key) {
                size--;
                if (prev == e)
                    entries[bucket] = next;
                else
                    prev.next = next;
                return e.value;
            }
            prev = e;
            e = next;
        }
        return null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(long key) {
        int bucket = indexFor(key, entries.length);
        for (Entry<V> e = entries[bucket]; e != null; e = e.next) {
            if (e.key == key)
                return true;
        }
        return false;
    }

    public boolean containsValue(V value) {
        Entry[] ent = entries;
        for (Entry anEnt : ent) {
            for (Entry e = anEnt; e != null; e = e.next) {
                if (value.equals(e.value))
                    return true;
            }
        }
        return false;
    }

    public long[] keys() {
        long[] result = new long[size];
        Entry[] ent = entries;
        int position = 0;
        for (Entry anEnt : ent) {
            for (Entry e = anEnt; e != null; e = e.next) {
                result[position++] = e.key;
            }
        }
        return result;
    }

    public V[] values() {
        V[] result = (V[]) new Object[size];
        Entry[] ent = entries;
        int position = 0;
        for (Entry anEnt : ent) {
            for (Entry e = anEnt; e != null; e = e.next) {
                result[position++] = (V) e.value;
            }
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
        private Entry<V> next;

        Entry(long key, V value, Entry<V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
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

    private void addEntry(long key, V value, int bucketIndex) {
        Entry<V> e = entries[bucketIndex];
        entries[bucketIndex] = new Entry<>(key, value, e);
        if (size++ >= treshold)
            resize(2 * entries.length);

    }

    private void resize(int newCapacity) {
        Entry<V>[] newEntries = new Entry[newCapacity];
        transfer(newEntries);
        entries = newEntries;
        treshold = (int) (newCapacity * loadFactor);
    }

    private void transfer(Entry<V>[] newEntries) {
        Entry<V>[] src = entries;
        int newCapacity = newEntries.length;
        for (int j = 0; j < src.length; j++) {
            Entry<V> e = src[j];
            if (e != null) {
                src[j] = null;
                do {
                    Entry<V> next = e.next;
                    int i = indexFor(e.key, newCapacity);
                    e.next = newEntries[i];
                    newEntries[i] = e;
                    e = next;
                } while (e != null);
            }
        }
    }

}
