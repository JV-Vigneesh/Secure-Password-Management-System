package application;

import java.util.LinkedList;

class HashTable {
    private static final int SIZE = 100;
    private LinkedList<Entry>[] table;

    public HashTable() {
        table = new LinkedList[SIZE];
        for (int i = 0; i < SIZE; i++) {
            table[i] = new LinkedList<>();
        }
    }

    public void put(String key, String value) {
        int index = hashFunction(key);
        for (Entry entry : table[index]) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        table[index].add(new Entry(key, value));
    }

    public String get(String key) {
        int index = hashFunction(key);
        for (Entry entry : table[index]) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    private int hashFunction(String key) {
        return Math.abs(key.hashCode()) % SIZE;
    }

    static class Entry {
        String key, value;

        Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
