package ru.itmo.java;

public class HashTable {

    private static final int INITIAL_CAPACITY = 16;
    private static final double INITIAL_LOAD_FACTOR = 0.5;
    private int capacity;
    private double loadFactor;
    private int size;
    private Entry[] elements;

    public HashTable() {
        this(INITIAL_CAPACITY, INITIAL_LOAD_FACTOR);
    }

    public HashTable(int initialCapacity) {
        this(initialCapacity, INITIAL_LOAD_FACTOR);
    }

    public HashTable(int initialCapacity, double initialLoadFactor) {
        if (initialCapacity <= 0 || initialCapacity > Integer.MAX_VALUE - 8) {
            capacity = INITIAL_CAPACITY;
        } else {
            capacity = initialCapacity;
        }
        if (initialLoadFactor < 0 || initialLoadFactor > 1) {
            loadFactor = INITIAL_LOAD_FACTOR;
        } else {
            loadFactor = initialLoadFactor;
        }
        elements = new Entry[capacity];
    }

    public Object put(Object key, Object value) {
        int index = find(key);
        if (elements[index] != null) {
            Object prevValue = elements[index].getValue();
            elements[index].setValue(value);
            return prevValue;
        }
        index = (key.hashCode() & Integer.MAX_VALUE) % capacity;
        while (elements[index] != null && !elements[index].isDeleted()) {
            index = (index + 1) % capacity;
        }
        elements[index] = new Entry(key, value);
        size++;
        if (isFull()) {
            ensureCapacity();
        }
        return null;
    }

    public Object get(Object key) {
        int index = find(key);
        if (elements[index] != null) {
            return elements[index].getValue();
        }
        return null;
    }

    public Object remove(Object key) {
        int index = find(key);
        if (elements[index] != null) {
            Object prevValue = elements[index].getValue();
            elements[index].delete();
            size--;
            return prevValue;
        }
        return null;
    }

    public int size() {
        return size;
    }

    private int find(Object key) {
        int index = (key.hashCode() & Integer.MAX_VALUE) % capacity;
        while (elements[index] != null) {
            if (elements[index].getKey().equals(key) && !elements[index].isDeleted()) {
                return index;
            }
            index = (index + 1) % capacity;
        }
        return index;
    }

    private boolean isFull() {
        return loadFactor * capacity <= size;
    }

    private void ensureCapacity() {
        Entry[] oldElements = elements;
        capacity *= 2;
        elements = new Entry[capacity];
        for (Entry oldElement : oldElements) {
            if (oldElement != null && !oldElement.isDeleted()) {
                put(oldElement.getKey(), oldElement.getValue());
                size--;
            }
        }
    }

    private static class Entry {
        private final Object key;
        private Object value;
        private boolean isDeleted;

        public Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
            this.isDeleted = false;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public boolean isDeleted() {
            return isDeleted;
        }

        public void delete() {
            isDeleted = true;
        }
    }
}
