package edu.colorado.cires.pace.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;

public final class ApplicationPropertyResolver {

  private static final String APPLICATION_BASE_DIR = ".pace";

  public <T> T getPropertyValue(String propertyName, Function<String, T> transform) {
    Properties properties = new Properties();
    try (InputStream inputStream = getClass().getResourceAsStream("/application.properties")) {
      properties.load(inputStream);
    } catch (IOException e) {
      throw new IllegalStateException(String.format(
          "Failed to read %s value from application properties", propertyName
      ), e);
    }
    
    String propertyValue = properties.getProperty(propertyName);
    
    if (StringUtils.isBlank(propertyValue)) {
      return null;
    }
    
    return transform.apply(propertyValue);
  }

  public Path getWorkDir() {
    String dirString = getPropertyValue("pace-cli.work-dir", (s) -> s);
    if (StringUtils.isBlank(dirString)) {
      return Path.of(
          System.getProperty("user.home")
      ).resolve(APPLICATION_BASE_DIR).resolve("data").toAbsolutePath();
    }

    return Paths.get(dirString).toAbsolutePath();
  }
  
  public Path getOutputDir() {
    String dirString = getPropertyValue("pace-cli.output-dir", (s) -> s);
    if (StringUtils.isBlank(dirString)) {
      return Path.of(
          System.getProperty("user.home")
      ).resolve(APPLICATION_BASE_DIR).resolve("output").toAbsolutePath();
    }

    return Paths.get(dirString).toAbsolutePath();
  }

  public String getVersion() {
    return getVersion(true);
  }

  public String getVersion(boolean includePatchVersion) {
    String fullVersion = getPropertyValue("pace-cli.version", (s) -> s);
    if (includePatchVersion) {
      return fullVersion;
    }

    String[] versionParts = fullVersion.split("\\.");
    return String.format(
        "%s.%s",
        versionParts[0],
        versionParts[1]
    );
  }

}
