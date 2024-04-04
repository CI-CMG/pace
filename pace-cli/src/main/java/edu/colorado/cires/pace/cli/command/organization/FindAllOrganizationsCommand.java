package edu.colorado.cires.pace.cli.command.organization;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.Organization;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List all organizations", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class FindAllOrganizationsCommand extends FindAllCommand<Organization, String> {

  @Override
  protected ControllerFactory<Organization, String> getControllerFactory() {
    return OrganizationControllerFactory::createController;
  }

}
