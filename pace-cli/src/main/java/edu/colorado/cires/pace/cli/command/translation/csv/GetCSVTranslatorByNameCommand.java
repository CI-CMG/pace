package edu.colorado.cires.pace.cli.command.translation.csv;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.CSVTranslator;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-name", description = "Get CSV translator by name",  mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetCSVTranslatorByNameCommand extends GetByUniqueFieldCommand<CSVTranslator> {
  
  @Parameters(description = "CSV translator name")
  private String name;

  @Override
  protected Supplier<String> getUniqueFieldProvider() {
    return () -> name;
  }

  @Override
  protected ControllerFactory<CSVTranslator> getControllerFactory() {
    return CSVTranslatorControllerFactory::createController;
  }
}
