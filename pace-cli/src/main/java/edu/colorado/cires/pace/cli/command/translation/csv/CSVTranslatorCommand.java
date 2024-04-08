package edu.colorado.cires.pace.cli.command.translation.csv;

import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import picocli.CommandLine.Command;

@Command(
    name = "csv",
    description = "Manage CSV translators",
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider.class,
    subcommands = { 
        CreateCSVTranslatorCommand.class,
        DeleteCSVTranslatorCommand.class,
        FindAllCSVTranslatorsCommand.class,
        GetCSVTranslatorByNameCommand.class,
        GetCSVTranslatorByUUIDCommand.class,
        UpdateCSVTranslatorCommand.class
    }
)
public class CSVTranslatorCommand implements Runnable {

  @Override
  public void run() {}
}
