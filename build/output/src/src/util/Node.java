package util;

import java.util.HashMap;

public class Node {
	
	private String name;
	private Object data;
    private HashMap<String, Object> map;

	Node(String name) {
		this.name = name;
        map = new HashMap<String, Object>();
	}
	
	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}
	
	public boolean equals(Object o) {
		if( o instanceof Node ) {
			Node n = (Node)o;
			return getName().equals(n.getName());
		}
		return false;
	}
    
    public void setValue(String key, Object value) {
        map.put(key, value);
    }
    
    public Object getValue(String key) {
        return map.get(key);
    }
}