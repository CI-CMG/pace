package edu.colorado.cires.pace.cli.command.translation.excel;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.ExcelTranslator;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-name", description = "Get excel translator by name",  mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetExcelTranslatorByNameCommand extends GetByUniqueFieldCommand<ExcelTranslator> {
  
  @Parameters(description = "excel translator name")
  private String name;

  @Override
  protected Supplier<String> getUniqueFieldProvider() {
    return () -> name;
  }

  @Override
  protected ControllerFactory<ExcelTranslator> getControllerFactory() {
    return ExcelTranslatorControllerFactory::createController;
  }
}
