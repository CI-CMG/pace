package edu.colorado.cires.pace.cli.command.detectionType;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.DetectionType;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "create", description = "Create detection type", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class CreateDetectionTypeCommand extends CreateCommand<DetectionType> {
  
  @Parameters(description = "File containing detection type (- for stdin)")
  private File detectionType;

  @Override
  protected Supplier<File> getJsonBlobProvider() {
    return () -> detectionType;
  }

  @Override
  protected Class<DetectionType> getJsonClass() {
    return DetectionType.class;
  }

  @Override
  protected RepositoryFactory<DetectionType> getRepositoryFactory() {
    return DetectionTypeRepositoryFactory::createRepository;
  }
}
