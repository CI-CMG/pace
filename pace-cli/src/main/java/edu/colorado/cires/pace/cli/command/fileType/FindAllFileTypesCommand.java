package edu.colorado.cires.pace.cli.command.fileType;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.FileType;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List all file types", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class FindAllFileTypesCommand extends FindAllCommand<FileType> {

  @Override
  protected ControllerFactory<FileType> getControllerFactory() {
    return FileTypeControllerFactory::createController;
  }
}
