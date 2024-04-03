package edu.colorado.cires.pace.cli;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import picocli.CommandLine.IVersionProvider;

public class VersionProvider implements IVersionProvider {

  @Override
  public String[] getVersion() {
    Properties properties = new Properties();
    try (InputStream inputStream = getClass().getResourceAsStream("/application.properties")) {
      properties.load(inputStream);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to establish application version", e);
    }
    return new String[] {properties.getProperty("app.version")};
  }

}
