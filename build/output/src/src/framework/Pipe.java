package framework;

import java.util.HashMap;
import java.util.Map;

public class Pipe {
	private Map<String, Object> map;
	
	Pipe() {
		map = new HashMap<String, Object>();
	}
	
	public void addData(String key, Object data) {
		map.put(key, data);
	}
	
//	public void addData(Map<String, Object> data) {
//		map.putAll(data);
//	}
	
	public Object getData(String key) {
		if( map.containsKey(key) ) {
			return map.get(key);
		}
		else
			return null;
	}
}
