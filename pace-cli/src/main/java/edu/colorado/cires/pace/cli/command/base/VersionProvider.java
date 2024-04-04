package edu.colorado.cires.pace.cli.command.base;

import edu.colorado.cires.pace.cli.util.ApplicationPropertyResolver;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import picocli.CommandLine.IVersionProvider;

public class VersionProvider implements IVersionProvider {
  
  private final ApplicationPropertyResolver propertyResolver = new ApplicationPropertyResolver();

  @Override
  public String[] getVersion() {
    return new String[] {
        propertyResolver.getPropertyValue("pace-cli.version")
    };
  }

}
