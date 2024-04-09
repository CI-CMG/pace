package edu.colorado.cires.pace.cli.command.organization;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.Organization;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "delete", description = "Delete organization", versionProvider = VersionProvider.class)
class DeleteOrganizationCommand extends DeleteCommand<Organization> {
  
  @Parameters(description = "organization uui")
  private UUID uuid;

  @Override
  protected Supplier<UUID> getUUIDProvider() {
    return () -> uuid;
  }

  @Override
  protected ControllerFactory<Organization> getControllerFactory() {
    return OrganizationControllerFactory::createController;
  }
}
