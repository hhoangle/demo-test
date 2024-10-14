package commons;

import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();

    static {
        properties.setProperty("env", System.getProperty("env"));
        properties.setProperty("baseUrl", System.getProperty("baseUrl"));
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}