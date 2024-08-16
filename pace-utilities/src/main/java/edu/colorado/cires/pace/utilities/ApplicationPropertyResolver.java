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

  static {
    try {
      createPropertiesFileAndConfigDir();

      try (
          InputStream defaultInputStream = ApplicationPropertyResolver.class.getResourceAsStream("/application.properties");
          InputStream userPropertiesInputStream = new FileInputStream(getPropertiesFilePath().toFile())
      ) {
        Properties properties = System.getProperties();
        properties.load(defaultInputStream);
        properties.load(userPropertiesInputStream);
        System.setProperties(properties);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    initializeDataDirectory();
  }

  public static <T> T getPropertyValue(String propertyName, Function<String, T> transform) {
    String propertyValue = System.getProperty(propertyName);

    if (StringUtils.isBlank(propertyValue)) {
      return null;
    }

    return transform.apply(propertyValue);
  }
  
  private static Path getConfigDir() {
    return APPLICATION_BASE_DIR.resolve("config");
  }
  
  private static Path getPropertiesFilePath() {
    return getConfigDir().resolve("application.properties");
  }
  
  private static void createPropertiesFileAndConfigDir() throws IOException {
    Path propertiesFile = getPropertiesFilePath();
    if (propertiesFile.toFile().exists()) {
      return;
    }
    
    Path parentPath = propertiesFile.getParent();
    if (parentPath != null && !parentPath.toFile().exists()) {
      FileUtils.createParentDirectories(propertiesFile.toFile());
    }

    try (InputStream inputStream = ApplicationPropertyResolver.class.getResourceAsStream("/application.properties"); OutputStream outputStream = new FileOutputStream(propertiesFile.toFile())) {
      if (inputStream == null) {
        throw new FileNotFoundException("application.properties file not found in application resources");
      }
      Properties properties = new Properties();
      properties.load(inputStream);
      
      properties.remove("pace.version");
      
      properties.store(outputStream, "PACE user properties");
    }
  }

  private static void initializeDataDirectory() {
//    try{
//      Path path = getDataDir();
//      if (path.toFile().exists()) {
//        return;
//      }
//      DataInitializer.initialize(path, SerializationUtils.createObjectMapper(), "file-types");
//      DataInitializer.initialize(path, SerializationUtils.createObjectMapper(), "instrument");
//      DataInitializer.initialize(path, SerializationUtils.createObjectMapper(), "platforms");
//    } catch (Exception e) {
//      throw new IllegalStateException("Unable to initialize data directory", e);
//    }
  }

  public static Path getDataDir() {
    String dirString = getPropertyValue("pace.metadata-directory", (s) -> s);
    Path defaultDataDir = APPLICATION_BASE_DIR.resolve("data").toAbsolutePath();
    if (StringUtils.isBlank(dirString)) {
      return defaultDataDir;
    }

    Path userOptionDir = Paths.get(dirString);
    if (!userOptionDir.isAbsolute()) {
      return defaultDataDir;
    }
    return userOptionDir;
  }

  public static String getVersion() {
    return getVersion(true);
  }

  public static String getVersion(boolean includePatchVersion) {
    String fullVersion = getPropertyValue("pace.version", (s) -> s);
    
    if (fullVersion == null) {
      return null;
    }
    
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
