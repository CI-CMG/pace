package edu.colorado.cires.pace.cli.command.translation.excel;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.ExcelTranslator;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-uuid", description = "Get excel translator by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetExcelTranslatorByUUIDCommand extends GetByUUIDCommand<ExcelTranslator, String> {
  
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
