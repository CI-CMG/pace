package edu.colorado.cires.pace.cli.command.translation.excel;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.ExcelTranslator;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List all excel translators", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class FindAllExcelTranslatorsCommand extends FindAllCommand<ExcelTranslator> {

  @Override
  protected ControllerFactory<ExcelTranslator> getControllerFactory() {
    return ExcelTranslatorControllerFactory::createController;
  }
}
