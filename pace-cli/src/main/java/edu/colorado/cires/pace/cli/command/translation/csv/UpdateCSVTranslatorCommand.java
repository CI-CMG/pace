package edu.colorado.cires.pace.cli.command.translation.csv;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.data.CSVTranslator;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "update", description = "Update CSV translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class UpdateCSVTranslatorCommand extends UpdateCommand<CSVTranslator, String> {
  
  @Parameters(description = "file containing CSV translator (- for stdin)")
  private File csvTranslator;

  @Override
  protected UUIDProvider<CSVTranslator> getUUIDProvider() {
    return CSVTranslator::getUUID;
  }

  @Override
  protected Supplier<File> getJsonBlobProvider() {
    return () -> csvTranslator;
  }

  @Override
  protected Class<CSVTranslator> getJsonClass() {
    return CSVTranslator.class;
  }

  @Override
  protected ControllerFactory<CSVTranslator, String> getControllerFactory() {
    return CSVTranslatorControllerFactory::createController;
  }
}
