package edu.colorado.cires.pace.cli.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

public final class ApplicationPropertyResolver {
  
  private String getPropertyValue(String propertyName) {
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
  
  public Path getWorkDir() {
    String dirString = getPropertyValue("pace-cli.work-dir");
    if (StringUtils.isBlank(dirString)) {
      return Path.of(
          System.getProperty("user.home")
      ).resolve(".pace-cli").resolve("data").toAbsolutePath();
    }
    
    return Paths.get(dirString).toAbsolutePath();
  }
  
  public String getVersion() {
    return getPropertyValue("pace-cli.version");
  }

}
