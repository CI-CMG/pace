package edu.colorado.cires.pace.cli.command.fileType;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.FileType;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-type", description = "Get file type by type", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetFileTypeByTypeCommand extends GetByUniqueFieldCommand<FileType, String> {
  
  @Parameters(description = "file type")
  private String type;

  @Override
  protected Supplier<String> getUniqueFieldProvider() {
    return () -> type;
  }

  @Override
  protected ControllerFactory<FileType, String> getControllerFactory() {
    return FileTypeControllerFactory::createController;
  }
}
