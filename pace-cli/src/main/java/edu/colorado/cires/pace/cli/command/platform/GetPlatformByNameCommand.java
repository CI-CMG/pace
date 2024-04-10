package edu.colorado.cires.pace.cli.command.platform;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.Platform;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-name", description = "Get platform by name", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetPlatformByNameCommand extends GetByUniqueFieldCommand<Platform> {
  
  @Parameters(description = "platform name")
  private String name;

  @Override
  protected Supplier<String> getUniqueFieldProvider() {
    return () -> name;
  }

  @Override
  protected RepositoryFactory<Platform> getRepositoryFactory() {
    return PlatformRepositoryFactory::createRepository;
  }
}
