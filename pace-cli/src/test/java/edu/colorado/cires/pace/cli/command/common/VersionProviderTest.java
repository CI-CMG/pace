package edu.colorado.cires.pace.cli.command.common;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.junit.jupiter.api.Test;

class VersionProviderTest {

  @Test
  void getVersion() throws IOException {
    try (InputStream inputStream = new FileInputStream("src/test/resources/application.properties")) {
      Properties properties = new Properties();
      properties.load(inputStream);

      assertEquals(properties.getProperty("pace.version"), new VersionProvider().getVersion()[0]);
    }
  }
}