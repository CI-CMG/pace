package edu.colorado.cires.pace.cli.command.detectionType;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.DetectionType;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-uuid", description = "Get detection type by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetDetectionTypeUUIDCommand extends GetByUUIDCommand<DetectionType> {
  
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
