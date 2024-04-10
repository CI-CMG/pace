package edu.colorado.cires.pace.cli.command.detectionType;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.DetectionType;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "delete", description = "Delete detection type", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class DeleteDetectionTypeCommand extends DeleteCommand<DetectionType> {
  
  @Parameters(description = "detection type uuid")
  private UUID uuid;

  @Override
  protected Supplier<UUID> getUUIDProvider() {
    return () -> uuid;
  }

  @Override
  protected RepositoryFactory<DetectionType> getRepositoryFactory() {
    return DetectionTypeRepositoryFactory::createRepository;
  }
}
