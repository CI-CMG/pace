package edu.colorado.cires.pace.cli.command.translation.excel;

import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import picocli.CommandLine.Command;

@Command(
    name = "excel",
    description = "Manage excel translators",
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider.class,
    subcommands = {
        CreateExcelTranslatorCommand.class,
        DeleteExcelTranslatorCommand.class,
        FindAllExcelTranslatorsCommand.class,
        GetExcelTranslatorByNameCommand.class,
        GetExcelTranslatorByUUIDCommand.class,
        UpdateExcelTranslatorCommand.class
    }
)
public class ExcelTranslatorCommand implements Runnable {

  @Override
  public void run() {}
}
