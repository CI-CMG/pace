package edu.colorado.cires.pace.cli.command.ship;

import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.data.object.Ship;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-uuid", description = "Get ship by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetShipByUUIDCommand extends GetByUUIDCommand<Ship> {
  
  @Parameters(description = "ship uuid")
  private UUID uuid;

  @Override
  protected RepositoryFactory<Ship> getRepositoryFactory() {
    return ShipRepositoryFactory::createRepository;
  }

  @Override
  protected Supplier<UUID> getUUIDProvider() {
    return () -> uuid;
  }
}
