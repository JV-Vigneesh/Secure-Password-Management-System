package application;

import java.util.LinkedList;

class HashNode {
    String key;
    String value;

    public HashNode(String key, String value) {
        this.key = key;
        this.value = value;
    }
}

public class HashTable {
    private final int SIZE = 256;
    private LinkedList<HashNode>[] table;

    public HashTable() {
        table = new LinkedList[SIZE];
        for (int i = 0; i < SIZE; i++) {
            table[i] = new LinkedList<>();
        }
    }

    private int getHash(String key) {
        return key.hashCode() % SIZE;
    }

    public void put(String key, String value) {
        int index = getHash(key);
        table[index].add(new HashNode(key, value));
    }

    public String get(String key) {
        int index = getHash(key);
        for (HashNode node : table[index]) {
            if (node.key.equals(key)) return node.value;
        }
        return null;
    }
}
