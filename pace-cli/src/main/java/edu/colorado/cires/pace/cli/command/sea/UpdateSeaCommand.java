package edu.colorado.cires.pace.cli.command.sea;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.Sea;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "update", description = "Update sea area", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class UpdateSeaCommand extends UpdateCommand<Sea> {
  
  @Parameters(description = "File containing sea area (- for stdin)")
  private File seaArea;

  @Override
  protected Supplier<File> getJsonBlobProvider() {
    return () -> seaArea;
  }

  @Override
  protected Class<Sea> getJsonClass() {
    return Sea.class;
  }

  @Override
  protected RepositoryFactory<Sea> getRepositoryFactory() {
    return SeaRepositoryFactory::createRepository;
  }
}
