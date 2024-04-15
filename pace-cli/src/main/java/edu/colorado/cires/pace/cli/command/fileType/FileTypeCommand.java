package edu.colorado.cires.pace.cli.command.fileType;

import edu.colorado.cires.pace.cli.command.common.BaseCommand;
import edu.colorado.cires.pace.data.object.FileType;
import picocli.CommandLine.Command;

@Command(name = "file-type", description = "Manage file types")
public class FileTypeCommand extends BaseCommand<FileType> {

  FileTypeCommand() {
    super(FileType.class, FileTypeRepositoryFactory::createRepository);
  }

  @Override
  public void run() {
    
  }
}
