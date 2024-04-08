package edu.colorado.cires.pace.cli.command.translation.csv;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.CSVTranslator;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-uuid", description = "Get CSV translator by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetCSVTranslatorByUUIDCommand extends GetByUUIDCommand<CSVTranslator, String> {
  
  @Parameters(description = "CSV translator uuid")
  private UUID uuid;

  @Override
  protected Supplier<UUID> getUUIDProvider() {
    return () -> uuid;
  }

  @Override
  protected ControllerFactory<CSVTranslator, String> getControllerFactory() {
    return CSVTranslatorControllerFactory::createController;
  }
}
