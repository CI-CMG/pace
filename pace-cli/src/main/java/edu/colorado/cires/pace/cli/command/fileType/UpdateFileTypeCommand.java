package edu.colorado.cires.pace.cli.command.fileType;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.core.repository.UUIDProvider;
import edu.colorado.cires.pace.data.FileType;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "update", description = "Update file type", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class UpdateFileTypeCommand extends UpdateCommand<FileType, String> {
  
  @Parameters(description = "File containing file type (- for stdin)")
  private File fileType;

  @Override
  protected UUIDProvider<FileType> getUUIDProvider() {
    return FileType::getUUID;
  }

  @Override
  protected Supplier<File> getJsonBlobProvider() {
    return () -> fileType;
  }

  @Override
  protected Class<FileType> getJsonClass() {
    return FileType.class;
  }

  @Override
  protected ControllerFactory<FileType, String> getControllerFactory() {
    return FileTypeControllerFactory::createController;
  }
}