package edu.colorado.cires.pace.cli.command.translation.csv;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.CSVTranslator;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "delete", description = "Delete CSV translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class DeleteCSVTranslatorCommand extends DeleteCommand<CSVTranslator> {
  
  @Parameters(description = "CSV translator uuid")
  private UUID uuid;

  @Override
  protected Supplier<UUID> getUUIDProvider() {
    return () -> uuid;
  }

  @Override
  protected ControllerFactory<CSVTranslator> getControllerFactory() {
    return CSVTranslatorControllerFactory::createController;
  }
}
