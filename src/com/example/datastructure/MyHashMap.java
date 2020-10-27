package com.example.datastructure;

public class MyHashMap<K, V> {

	private Entry<K, V>[] table;
	private int capacity;
	private int DEFAULT_CAPACITY = 16;

	MyHashMap() {
		this.capacity = DEFAULT_CAPACITY;
		this.table = new Entry[capacity];
	}

	static class Entry<K, V> {
		K key;
		V value;
		Entry<K, V> next;

		Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		Entry(K key, V value, Entry<K, V> next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}
	}

	public void put(K key, V value) {
		if (key == null) {
			return;
		}
		
		int hash = hash(key);
		Entry<K, V> newEntry = new Entry<>(key, value);
		
		if(table[hash] == null) {
			table[hash] = newEntry;
		}else {
			Entry<K, V> current = table[hash];
			Entry<K, V> previous = null;
			while(current != null) {
				if(current.key.equals(key)) {
					current.value = newEntry.value;
					return;
				}
				previous = current;
				current = current.next;
			}
			previous.next = newEntry;
		}
	}
	
	public V get(K key) {
		if(key == null) {
			return null;
		}
		int hash = hash(key);
		
		if(table[hash] == null) {
			return null;
		}else {
			Entry<K, V> current = table[hash];
			while(current != null) {
				if(current.key.equals(key)) {
					return current.value;
				}
				current = current.next;
			}
		}
		return null;
	}

	private int hash(K key) {
		return Math.abs(key.hashCode()) % capacity;
	}
}
