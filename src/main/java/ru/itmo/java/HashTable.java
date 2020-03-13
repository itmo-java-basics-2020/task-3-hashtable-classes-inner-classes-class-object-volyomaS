package ru.itmo.java;

public class HashTable {

    private static final int INITIAL_CAPACITY = 16;
    private static final double INITIAL_LOAD_FACTOR = 0.5;
    private int capacity;
    private double loadFactor;
    private int size;
    private Entry[] elements;

    public HashTable(int initialCapacity, double initialLoadFactor) {
        capacity = initialCapacity;
        loadFactor = initialLoadFactor;
        elements = new Entry[capacity];
    }

    public HashTable(int initialCapacity) {
        this(initialCapacity, INITIAL_LOAD_FACTOR);
    }

    public HashTable() {
        this(INITIAL_CAPACITY, INITIAL_LOAD_FACTOR);
    }

    Object put(Object key, Object value) {
        int hash = (key.hashCode() & Integer.MAX_VALUE) % capacity;
        while (elements[hash] != null) {
            if (elements[hash].getKey().equals(key) && !elements[hash].isDeleted()) {
                break;
            }
            hash = (hash + 1) % capacity;
        }
        if (elements[hash] != null) {
            Object prevValue = elements[hash].getValue();
            elements[hash].setValue(value);
            return prevValue;
        }
        hash = (key.hashCode() & Integer.MAX_VALUE) % capacity;
        while (elements[hash] != null) {
            if (elements[hash].isDeleted()) {
                elements[hash] = new Entry(key, value);
                increaseSize();
                if (checkCapacity()) {
                    elements = ensureCapacity();
                }
                return null;
            }
            hash = (hash + 1) % capacity;
        }
        elements[hash] = new Entry(key, value);
        increaseSize();
        if (checkCapacity()) {
            elements = ensureCapacity();
        }
        return null;
    }

    private boolean checkCapacity() {
        return getThreshold() <= size;
    }

    private Entry[] ensureCapacity() {
        int newCapacity = capacity * 2;
        Entry[] newElements = new Entry[newCapacity];
        for (int i = 0; i < capacity; i++) {
            if (elements[i] != null && !elements[i].isDeleted()) {
                int hash = (elements[i].getKey().hashCode() & Integer.MAX_VALUE) % newCapacity;
                while (newElements[hash % newCapacity] != null) {
                    hash++;
                }
                newElements[hash % newCapacity] = elements[i];
            }
        }
        capacity *= 2;
        return newElements;
    }

    Object get(Object key) {
        int hash = (key.hashCode() & Integer.MAX_VALUE) % capacity;
        while (elements[hash] != null) {
            if (elements[hash].getKey().equals(key) && !elements[hash].isDeleted()) {
                return elements[hash].getValue();
            }
            hash = (hash + 1) % capacity;
        }
        return null;
    }

    Object remove(Object key) {
        int hash = (key.hashCode() & Integer.MAX_VALUE) % capacity;
        while (elements[hash] != null) {
            if (elements[hash].getKey().equals(key) && !elements[hash].isDeleted()) {
                Object prevValue = elements[hash].getValue();
                elements[hash].delete();
                decreaseSize();
                return prevValue;
            }
            hash = (hash + 1) % capacity;
        }
        return null;
    }

    int size() {
        return size;
    }

    private void increaseSize() {
        size++;
    }

    private void decreaseSize() {
        size--;
    }

    private double getThreshold() {
        return loadFactor * capacity;
    }

    private static class Entry {
        private Object key;
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
