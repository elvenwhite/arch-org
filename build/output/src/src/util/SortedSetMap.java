package util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SortedSetMap<K, V extends Comparable> {
	private Map<K, Set<V>> map;

	public SortedSetMap() {
		map = new HashMap<K, Set<V>>();
	}
	
	public boolean containsKey(K key) {
		return map.containsKey(key);
	}
	
	public ImmutableCollection<V> get(K key) {
		if( containsKey(key) )
			return new ImmutableCollection<V>(map.get(key));
		else
			return null;
	}
	
	public void put(K key, V value) {
		Set<V> set = map.get(key);
		if (set == null) {
			set = new TreeSet<V>();
			map.put(key, set);
		}
		set.add(value);
	}

	public void remove(K key) {
		map.remove(key);
	}

	public void removeValue(K key, V value) {
		if (key == null)
			return;
		Set<V> set = map.get(key);
		if (set != null)
			set.remove(value);
	}
}
