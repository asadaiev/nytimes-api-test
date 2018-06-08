package nytimes.common;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Allow easy access to the config.properties
 */
public class Configuration {
  private Properties props;

  /**
   * Create instance and load properties on instantiation.
   *
   * @throws RuntimeException wrapping the underlying IOException in case config cannot be
   *                          accessed.
   */
  public Configuration(String propFile) {
    props = new Properties();

    try (InputStream readProperty = Configuration.class
            .getResourceAsStream(System.getProperty("CONFIG_RESOURCE", propFile))) {
      props.load(readProperty);

    } catch (IOException iox) {
      throw new RuntimeException(iox);
    }
  }

  /**
   * Gets a property from the configuration. In order to allow overriding
   * properties, first checks system properties passed on.
   *
   * @param key - the key to access the property
   * @return the value of the property
   */
  public String getProperty(String key) {
    return System.getProperty(key, props.getProperty(key));
  }


  public Properties getProperties() {
    return new Properties(props);
  }

  /**
   * Gets a property from the configuration and converts it to an integer In
   * order to allow overriding properties, first checks system properties
   * passed on.
   *
   * @param key - the key to access the property
   * @return the value of the property converted to an Integer
   */
  public Integer getIntegerProperty(String key) {
    return Integer.valueOf(getProperty(key));
  }

  @Override
  /** {@inheritDoc} */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (String key : props.stringPropertyNames()) {
      sb.append(key).append("=").append(getProperty(key));

      if (!props.getProperty(key).equals(getProperty(key))) {
        sb.append(" ( ").append(props.get(key)).append(" )");
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  public void setProperty(String key, String value) {
    props.setProperty(key, value);
  }
}