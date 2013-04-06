package util.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileMultiPropertiesManager implements IPropertiesManager {

    public FileMultiPropertiesManager(String fileName) {
        Properties properties = load(fileName);
        if (properties == null)
            return;
        
    }

    public String getValue(String group, String key) {
        return null;
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