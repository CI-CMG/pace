package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import picocli.CommandLine.IVersionProvider;

public class VersionProvider implements IVersionProvider {

  @Override
  public String[] getVersion() {
    return new String[] {
        ApplicationPropertyResolver.getVersion()
    };
  }

}
