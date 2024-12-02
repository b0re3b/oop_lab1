//package testdatabase;
//
//import com.creditapp.config.AppProperties;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Properties;
//
//@Component
//@ConfigurationProperties(prefix = "app")
//public class TestAppProperties {
//    private static AppProperties instance;
//    private Properties properties;
//
//    private TestAppProperties() {
//        properties = new Properties();
//        loadProperties();
//    }
//
//
//    public static synchronized AppProperties getInstance() {
//        if (instance == null) {
//            instance = new AppProperties();
//        }
//        return instance;
//    }
//
//
//    private void loadProperties() {
//        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
//            if (input == null) {
//                System.out.println("Sorry, unable to find application.properties");
//                return;
//            }
//
//            properties.load(input);
//        } catch (IOException ex) {
//            System.err.println("Error loading application properties: " + ex.getMessage());
//        }
//    }
//
//
//    public String getProperty(String key) {
//        return properties.getProperty(key);
//    }
//
//
//    public String getProperty(String key, String defaultValue) {
//        return properties.getProperty(key, defaultValue);
//    }
//
//
//    public Integer getIntProperty(String key) {
//        String value = properties.getProperty(key);
//        try {
//            return value != null ? Integer.parseInt(value) : null;
//        } catch (NumberFormatException e) {
//            System.err.println("Invalid integer value for key: " + key);
//            return null;
//        }
//    }
//
//
//    public int getIntProperty(String key, int defaultValue) {
//        Integer value = getIntProperty(key);
//        return value != null ? value : defaultValue;
//    }
//
//
//    public Boolean getBooleanProperty(String key) {
//        String value = properties.getProperty(key);
//        return value != null ? Boolean.parseBoolean(value) : null;
//    }
//
//
//    public boolean getBooleanProperty(String key, boolean defaultValue) {
//        Boolean value = getBooleanProperty(key);
//        return value != null ? value : defaultValue;
//    }
//
//
//    public void reloadProperties() {
//        loadProperties();
//    }
//    @Test
//    void testSingletonInstance() {
//        AppProperties first = AppProperties.getInstance();
//        AppProperties second = AppProperties.getInstance();
//
//        assertSame(first, second, "Singleton instances should be the same");
//    }
//
//    @Test
//    void testGetProperty() {
//        // Test string property retrieval
//        assertEquals("test.value", appProperties.getProperty("test.key"));
//        assertEquals("default", appProperties.getProperty("non.existent.key", "default"));
//    }
//
//    @Test
//    void testGetIntProperty() {
//        // Test integer property retrieval
//        assertEquals(42, appProperties.getIntProperty("int.key"));
//        assertEquals(100, appProperties.getIntProperty("non.existent.key", 100));
//        assertNull(appProperties.getIntProperty("invalid.int.key"));
//    }
//}