package edu.colorado.cires.pace.cli.command.translation.excel;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.ExcelTranslator;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "delete", description = "Delete excel translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class DeleteExcelTranslatorCommand extends DeleteCommand<ExcelTranslator, String> {
  
  @Parameters(description = "excel translator uuid")
  private UUID uuid;

  @Override
  protected Supplier<UUID> getUUIDProvider() {
    return () -> uuid;
  }

  @Override
  protected ControllerFactory<ExcelTranslator, String> getControllerFactory() {
    return ExcelTranslatorControllerFactory::createController;
  }
}
