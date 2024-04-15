package edu.colorado.cires.pace.cli.command.translation;

import edu.colorado.cires.pace.cli.command.translation.csv.CSVTranslatorCommand;
import edu.colorado.cires.pace.cli.command.translation.excel.ExcelTranslatorCommand;
import picocli.CommandLine.Command;

@Command(
    name = "translator",
    description = "Manage translators",
    subcommands = { CSVTranslatorCommand.class, ExcelTranslatorCommand.class }
)
public class TranslatorCommand implements Runnable {

  @Override
  public void run() {}
}
