package edu.colorado.cires.pace.cli.command.organization;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.Organization;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-name", description = "Get organization by name", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetOrganizationByNameCommand extends GetByUniqueFieldCommand<Organization, String> {
  
  @Parameters(description = "organization name")
  private String name;

  @Override
  protected Supplier<String> getUniqueFieldProvider() {
    return () -> name;
  }

  @Override
  protected ControllerFactory<Organization, String> getControllerFactory() {
    return OrganizationControllerFactory::createController;
  }
}
