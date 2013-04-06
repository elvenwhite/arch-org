package util;

import java.util.HashMap;
import java.util.Map;

public class CounterMap<K> {
	
	private Map<K, Integer> map;
	
	public CounterMap() {
		map = new HashMap<K, Integer>();
	}
	
	public void increase(K key) {
		if( map.containsKey(key) ) {
			int counter = map.get(key);
			map.put(key, counter+1);
		}
		else {
			map.put(key, 1);
		}
	}
	
	public int getCount(K key) {
		if( map.containsKey(key) ) {
			return map.get(key);
		}
		else 
			return 0;
	}
	
}
