package application;

import java.util.LinkedList;



public class HashTable<K, V> {
    private static final int SIZE = 100;
    private LinkedList<Entry<K, V>>[] table;

    public HashTable() {
        table = new LinkedList[SIZE];
        for (int i = 0; i < SIZE; i++) {
            table[i] = new LinkedList<>();
        }
    }
    private static HashTable<String, String> passwordCache = new HashTable<>();

    public static void cachePassword(String site, String password) {
        passwordCache.put(site, password);
    }

    private int getHash(K key) {
        return Math.abs(key.hashCode() % SIZE);
    }

    public void put(K key, V value) {
        int index = getHash(key);
        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        table[index].add(new Entry<>(key, value));
    }

    public V get(K key) {
        int index = getHash(key);
        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    public void remove(K key) {
        int index = getHash(key);
        table[index].removeIf(entry -> entry.key.equals(key));
    }

    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
