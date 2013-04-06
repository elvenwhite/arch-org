package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Properties 파일을 읽고 반환합니다.
 * <p>
 * 파일은 classpath 중 /properties/ 아래에서 파일을 찾게 됩니다.
 * </p>
 * 
 * @author Taeksu Kim (dolicoli@selab.snu.ac.kr)
 * @see java.util.Properties
 * 
 */
public class OldPropertiesManager {
    private static HashMap map = new HashMap();

    /**
     * Singleton 객체를 생성합니다.
     * <p>
     * group.properties 파일에 대한 객체를 생성합니다.
     * </p>
     * 
     * @param group
     *            Properties 그룹
     * @return {@link OldPropertiesManager} 객체
     */
    @SuppressWarnings("unchecked")
    public static OldPropertiesManager getInstance(String group) {
        if (!map.containsKey(group)) {
            OldPropertiesManager propertiesManager = new OldPropertiesManager();
            propertiesManager.load(group);
            map.put(group, propertiesManager);
        }
        return (OldPropertiesManager) map.get(group);
    }

    /**
     * Load된 Properties
     */
    private Properties properties;

    OldPropertiesManager() {
    }

    /**
     * group.properties 파일에서 key를 이용해 {@link java.lang.String String} 값을 반환합니다.
     * 
     * @param group
     *            Properties 그룹
     * @param key
     *            키
     * @return 값
     */
    public static String getValue(String group, String key) {
        return getValue(group, key, "");
    }

    /**
     * group.properties 파일에서 key를 이용해 {@link java.lang.String String} 값을 반환합니다.
     * <br/> 값이 없거나 문제가 발생했을 경우 지정한 기본값을 반환합니다.
     * 
     * @param group
     *            Properties 그룹
     * @param key
     *            키
     * @param defaultValue
     *            기본값
     * @return 값
     * @see #getValue(String, String)
     */
    public static String getValue(String group, String key, String defaultValue) {
        OldPropertiesManager manager = getInstance(group);
        Properties properties = manager.getProperties();
        if (properties == null)
            return defaultValue;
        String value = properties.getProperty(key);
        if (value == null || value.equals("")) {
            return defaultValue;
        }
        return value;
    }

    public static String[] getValues(String group, String key) {
        OldPropertiesManager manager = getInstance(group);
        Properties properties = manager.getProperties();
        if (properties == null)
            return null;
        String value = properties.getProperty(key);
        if (value == null || value.equals("")) {
            return null;
        }
        String[] values = value.split(",");
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].trim();
        }
        return values;
    }

    /**
     * group.properties 파일에서 key를 이용해 <code>boolean</code> 값을 반환합니다.
     * 
     * @param group
     *            Properties 그룹
     * @param key
     *            키
     * @return 값
     */
    public static boolean getBooleanValue(String group, String key) {
        return getBooleanValue(group, key, false);
    }

    /**
     * group.properties 파일에서 key를 이용해 <code>boolean</code> 값을 반환합니다. <br/> 값이
     * 없거나 문제가 발생했을 경우 지정한 기본값을 반환합니다.
     * 
     * @param group
     *            Properties 그룹
     * @param key
     *            키
     * @param defaultValue
     *            기본값
     * @return 값
     * @see #getBooleanValue(String, String)
     */
    public static boolean getBooleanValue(String group, String key,
            boolean defaultValue) {
        OldPropertiesManager manager = getInstance(group);
        Properties properties = manager.getProperties();
        if (properties == null)
            return defaultValue;
        String value = properties.getProperty(key);
        if (value == null || value.equals(""))
            return defaultValue;
        try {
            return Boolean.parseBoolean(value);
        } catch (Throwable t) {
            return defaultValue;
        }
    }

    /**
     * group.properties 파일에서 key를 이용해 <code>int</code> 값을 반환합니다.
     * 
     * @param group
     *            Properties 그룹
     * @param key
     *            키
     * @return 값
     */
    public static int getIntValue(String group, String key) {
        return getIntValue(group, key, 0);
    }

    /**
     * group.properties 파일에서 key를 이용해 <code>int</code> 값을 반환합니다. <br/> 값이 없거나
     * 문제가 발생했을 경우 지정한 기본값을 반환합니다.
     * 
     * @param group
     *            Properties 그룹
     * @param key
     *            키
     * @param defaultValue
     *            기본값
     * @return 값
     * @see #getIntValue(String, String)
     */
    public static int getIntValue(String group, String key, int defaultValue) {
        OldPropertiesManager manager = getInstance(group);
        Properties properties = manager.getProperties();
        if (properties == null)
            return defaultValue;
        String value = properties.getProperty(key);
        if (value == null || value.equals(""))
            return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (Throwable t) {
            return defaultValue;
        }
    }
    
    private void load(String group) {
        Properties properties = new Properties();

        String path = getClass().getResource("/").getPath() + "properties/"
                + group + ".properties";

        InputStream is = null;
        try {
            is = new FileInputStream(path);
            properties.load(is);
            this.properties = properties;
        } catch (IOException e) {
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Properties getProperties() {
        return properties;
    }

}
