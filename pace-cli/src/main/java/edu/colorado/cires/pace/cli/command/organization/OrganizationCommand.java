package edu.colorado.cires.pace.cli.command.organization;

import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import picocli.CommandLine.Command;

@Command(
    name = "organization",
    description = "Manage organizations",
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider.class,
    subcommands = {
        CreateOrganizationCommand.class,
        DeleteOrganizationCommand.class,
        FindAllOrganizationsCommand.class,
        GetOrganizationByNameCommand.class,
        GetOrganizationByUUIDCommand.class,
        UpdateOrganizationCommand.class
    }
)
public class OrganizationCommand implements Runnable {

  @Override
  public void run() {
    
  }
}
