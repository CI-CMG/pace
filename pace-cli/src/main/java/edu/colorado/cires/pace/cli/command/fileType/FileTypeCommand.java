package edu.colorado.cires.pace.cli.command.fileType;

import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import picocli.CommandLine.Command;

@Command(
    name = "file-type",
    description = "Manage file types",
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider.class,
    subcommands = {
        CreateFileTypeCommand.class,
        DeleteFileTypeCommand.class,
        FindAllFileTypesCommand.class,
        GetFileTypeByTypeCommand.class,
        GetFileTypeByUUIDCommand.class,
        UpdateFileTypeCommand.class,
    }
)
public class FileTypeCommand implements Runnable {

  @Override
  public void run() {
    
  }
}
