package edu.colorado.cires.pace.cli.command.fileType;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.FileType;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "delete", description = "Delete file type", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class DeleteFileTypeCommand extends DeleteCommand<FileType, String> {
  
  @Parameters(description = "file type uuid")
  private UUID uuid;

  @Override
  protected Supplier<UUID> getUUIDProvider() {
    return () -> uuid;
  }

  @Override
  protected ControllerFactory<FileType, String> getControllerFactory() {
    return FileTypeControllerFactory::createController;
  }
}
