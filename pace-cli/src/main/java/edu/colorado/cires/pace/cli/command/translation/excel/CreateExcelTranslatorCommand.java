package edu.colorado.cires.pace.cli.command.translation.excel;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.ExcelTranslator;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "create", description = "Create excel translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class CreateExcelTranslatorCommand extends CreateCommand<ExcelTranslator> {
  
  @Parameters(description = "file containing excel translator (- for stdin)")
  private File excelTranslator;

  @Override
  protected Supplier<File> getJsonBlobProvider() {
    return () -> excelTranslator;
  }

  @Override
  protected Class<ExcelTranslator> getJsonClass() {
    return ExcelTranslator.class;
  }

  @Override
  protected RepositoryFactory<ExcelTranslator> getRepositoryFactory() {
    return ExcelTranslatorRepositoryFactory::createRepository;
  }
}
