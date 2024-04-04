package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.cli.util.ApplicationPropertyResolver;
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
