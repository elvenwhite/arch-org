package util.properties;

import java.util.HashMap;


public class MemPropertiesManager implements IPropertiesManager {

    private HashMap<String, String> map = new HashMap<String, String>();

    public void putValue(String group, String key, String value) {
        map.put(toKey(group, key), value);
    }

    public String getValue(String group, String key) {
        String newKey = toKey(group, key);
        return map.get(newKey);
    }

    private static String toKey(String group, String key) {
        return group + "_" + key;
    }

}