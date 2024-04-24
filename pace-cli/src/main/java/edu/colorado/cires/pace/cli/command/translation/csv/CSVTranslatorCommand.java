package edu.colorado.cires.pace.cli.command.translation.csv;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.common.BaseCommand;
import edu.colorado.cires.pace.data.object.CSVTranslator;
import java.util.List;
import picocli.CommandLine.Command;

@Command(name = "csv-translator", description = "Manage CSV translators")
public class CSVTranslatorCommand extends BaseCommand<CSVTranslator> {

  protected CSVTranslatorCommand() {
    super(CSVTranslator.class, CSVTranslatorRepositoryFactory::createRepository, new TypeReference<>() {});
  }

  @Override
  public void run() {}
}
