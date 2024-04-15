package edu.colorado.cires.pace.cli.command.translation.csv;

import edu.colorado.cires.pace.cli.command.common.BaseCommand;
import edu.colorado.cires.pace.data.object.CSVTranslator;
import picocli.CommandLine.Command;

@Command(name = "csv", description = "Manage CSV translators")
public class CSVTranslatorCommand extends BaseCommand<CSVTranslator> {

  protected CSVTranslatorCommand() {
    super(CSVTranslator.class, CSVTranslatorRepositoryFactory::createRepository);
  }

  @Override
  public void run() {}
}
