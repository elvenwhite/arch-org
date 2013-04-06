package util.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class FilePropertiesManager implements IPropertiesManager {

    private HashMap<String, Properties> map = new HashMap<String, Properties>();

    public FilePropertiesManager() {
    }

    public FilePropertiesManager(String group) {
        Properties properties = load(group);
        map.put(group, properties);
    }

    public String getValue(String group, String key) {
        if (!map.containsKey(group)) {
            try {
                Properties properties = load(group);
                map.put(group, properties);
            } catch (Throwable t) {
                return null;
            }
        }
        Properties properties = map.get(group);
        return properties.getProperty(key);
    }

    protected Properties load(String group) {
        Properties properties = new Properties();

        String path = getClass().getResource("/").getPath() + "properties/"
                + group + ".properties";

        InputStream is = null;
        try {
            is = new FileInputStream(path);
            properties.load(is);
            return properties;
        } catch (IOException e) {
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
