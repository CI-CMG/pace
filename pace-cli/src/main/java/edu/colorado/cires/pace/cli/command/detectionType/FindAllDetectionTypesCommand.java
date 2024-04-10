package edu.colorado.cires.pace.cli.command.detectionType;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.DetectionType;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List all detection types", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class FindAllDetectionTypesCommand extends FindAllCommand<DetectionType> {

  @Override
  protected RepositoryFactory<DetectionType> getRepositoryFactory() {
    return DetectionTypeRepositoryFactory::createRepository;
  }
}
