package edu.colorado.cires.pace.cli.command.organization;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.Organization;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-uuid", description = "Get organization by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetOrganizationByUUIDCommand extends GetByUUIDCommand<Organization, String> {
  
  @Parameters(description = "organization uuid")
  private UUID uuid;

  @Override
  protected Supplier<UUID> getUUIDProvider() {
    return () -> uuid;
  }

  @Override
  protected ControllerFactory<Organization, String> getControllerFactory() {
    return OrganizationControllerFactory::createController;
  }
}
