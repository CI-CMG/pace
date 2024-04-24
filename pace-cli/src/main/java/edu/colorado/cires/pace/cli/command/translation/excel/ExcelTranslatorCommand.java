package edu.colorado.cires.pace.cli.command.translation.excel;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.common.BaseCommand;
import edu.colorado.cires.pace.data.object.ExcelTranslator;
import java.util.List;
import picocli.CommandLine.Command;

@Command(name = "excel-translator", description = "Manage excel translators")
public class ExcelTranslatorCommand extends BaseCommand<ExcelTranslator> {

  protected ExcelTranslatorCommand() {
    super(ExcelTranslator.class, ExcelTranslatorRepositoryFactory::createRepository, new TypeReference<>() {});
  }

  @Override
  public void run() {}
}
