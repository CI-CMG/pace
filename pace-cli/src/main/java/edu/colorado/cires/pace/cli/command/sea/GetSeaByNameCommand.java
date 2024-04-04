package edu.colorado.cires.pace.cli.command.sea;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.Sea;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-name", description = "Get sea area by name", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetSeaByNameCommand extends GetByUniqueFieldCommand<Sea, String> {
  
  @Parameters(description = "sea area name")
  private String name;

  @Override
  protected Supplier<String> getUniqueFieldProvider() {
    return () -> name;
  }

  @Override
  protected ControllerFactory<Sea, String> getControllerFactory() {
    return SeaControllerFactory::createController;
  }
}
