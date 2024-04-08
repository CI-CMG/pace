package edu.colorado.cires.pace.cli.command.translation;

import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.translation.csv.CSVTranslatorCommand;
import picocli.CommandLine.Command;

@Command(
    name = "translator",
    description = "Manage translators",
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider.class,
    subcommands = { CSVTranslatorCommand.class }
)
public class TranslatorCommand implements Runnable {

  @Override
  public void run() {}
}
