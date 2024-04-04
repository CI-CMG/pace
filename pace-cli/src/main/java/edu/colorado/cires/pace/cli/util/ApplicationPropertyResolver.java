package edu.colorado.cires.pace.cli.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ApplicationPropertyResolver {
  
  public String getPropertyValue(String propertyName) {
    Properties properties = new Properties();
    try (InputStream inputStream = getClass().getResourceAsStream("/application.properties")) {
      properties.load(inputStream);
    } catch (IOException e) {
      throw new IllegalStateException(String.format(
          "Failed to read %s value from application properties", propertyName
      ), e);
    }
    return properties.getProperty(propertyName);
  }

}
