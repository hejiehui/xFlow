package com.xrosstools.xflow;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DataMap {
	private Map<String, Object> entries = new ConcurrentHashMap<>();

	public DataMap() {}
	
	public DataMap(Map<String, Object> entries) {
		this.entries.putAll(entries);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T)entries.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getOrDefault(String key, T value) {
		return (T)entries.getOrDefault(key, value);
	}

	public <T> void put(String key, T data) {
		entries.put(key, data);
	}
	
	public <T> void putIfAbsent(String key, T data) {
		entries.putIfAbsent(key, data);
	}

	public boolean contains(String key) {
		return entries.containsKey(key);
	}

	public boolean isEmpty() {
		return entries.isEmpty();
	}

	public Set<String> keySet() {
		return entries.keySet();
	}
	
	public void clear() {
		entries.clear();
	}
	
	public int size() {
		return entries.size();
	}
	
	public void copyFrom(DataMap source, String...keys) {
		for(String key: keys)
			put(key, source.get(key));
	}
	
	public DataMap copy() {
		DataMap copy = new DataMap();
		copy.entries.putAll(entries);
		return copy;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, ?> enrty: entries.entrySet()) {
			sb.append(String.format("%s: %s\n", enrty.getKey(), enrty.getValue()));
		}
		return sb.toString();
	}
}
