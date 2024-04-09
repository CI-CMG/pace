package edu.colorado.cires.pace.cli.command.fileType;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.FileType;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-uuid", description = "Get file type by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetFileTypeByUUIDCommand extends GetByUUIDCommand<FileType> {
  
  @Parameters(description = "file type uuid")
  private UUID uuid;

  @Override
  protected Supplier<UUID> getUUIDProvider() {
    return () -> uuid;
  }

  @Override
  protected ControllerFactory<FileType> getControllerFactory() {
    return FileTypeControllerFactory::createController;
  }
}
