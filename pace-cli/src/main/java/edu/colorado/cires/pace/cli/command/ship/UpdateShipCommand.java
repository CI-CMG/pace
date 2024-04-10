package edu.colorado.cires.pace.cli.command.ship;

import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.data.object.Ship;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "update", description = "Update ship", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class UpdateShipCommand extends UpdateCommand<Ship> {
  
  @Parameters(description = "Ship from file (- for stdin)")
  private File ship;

  @Override
  protected RepositoryFactory<Ship> getRepositoryFactory() {
    return ShipRepositoryFactory::createRepository;
  }

  @Override
  protected Supplier<File> getJsonBlobProvider() {
    return () -> ship;
  }

  @Override
  protected Class<Ship> getJsonClass() {
    return Ship.class;
  }
}
