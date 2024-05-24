package edu.colorado.cires.pace.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.function.Function;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public final class ApplicationPropertyResolver {

  private static final Path APPLICATION_BASE_DIR = Paths.get(System.getProperty("user.home")).resolve(".pace");

  public <T> T getPropertyValue(String propertyName, Function<String, T> transform) {
    Properties properties = new Properties();
    try {
      createPropertiesFileAndConfigDir();

      try (InputStream defaultInputStream = getClass().getResourceAsStream("/application.properties");
          InputStream userPropertiesInputStream = new FileInputStream(getPropertiesFilePath().toFile())) {
        properties.load(defaultInputStream);
        properties.load(userPropertiesInputStream);
      }
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
  
  private Path getConfigDir() {
    return APPLICATION_BASE_DIR.resolve("config");
  }
  
  private Path getPropertiesFilePath() {
    return getConfigDir().resolve("application.properties");
  }
  
  private void createPropertiesFileAndConfigDir() throws IOException {
    Path propertiesFile = getPropertiesFilePath();
    if (propertiesFile.toFile().exists()) {
      return;
    }
    
    if (!propertiesFile.getParent().toFile().exists()) {
      FileUtils.createParentDirectories(propertiesFile.toFile());
    }

    try (InputStream inputStream = getClass().getResourceAsStream("/application.properties"); OutputStream outputStream = new FileOutputStream(propertiesFile.toFile())) {
      if (inputStream == null) {
        throw new FileNotFoundException("application.properties file not found in application resources");
      }
      Properties properties = new Properties();
      properties.load(inputStream);
      
      properties.remove("pace.version");
      
      properties.store(outputStream, "PACE user properties");
    }
  }

  public Path getDataDir() {
    String dirString = getPropertyValue("pace.data-dir", (s) -> s);
    if (StringUtils.isBlank(dirString)) {
      return APPLICATION_BASE_DIR.resolve("data").toAbsolutePath();
    }

    return Paths.get(dirString).toAbsolutePath();
  }

  public String getVersion() {
    return getVersion(true);
  }

  public String getVersion(boolean includePatchVersion) {
    String fullVersion = getPropertyValue("pace.version", (s) -> s);
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
