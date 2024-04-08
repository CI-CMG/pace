package edu.colorado.cires.pace.cli.command.translation.excel;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.data.ExcelTranslator;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "update", description = "Update excel translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class UpdateExcelTranslatorCommand extends UpdateCommand<ExcelTranslator, String> {
  
  @Parameters(description = "file containing excel translator (- for stdin)")
  private File excelTranslator;

  @Override
  protected UUIDProvider<ExcelTranslator> getUUIDProvider() {
    return ExcelTranslator::getUUID;
  }

  @Override
  protected Supplier<File> getJsonBlobProvider() {
    return () -> excelTranslator;
  }

  @Override
  protected Class<ExcelTranslator> getJsonClass() {
    return ExcelTranslator.class;
  }

  @Override
  protected ControllerFactory<ExcelTranslator, String> getControllerFactory() {
    return ExcelTranslatorControllerFactory::createController;
  }
}
