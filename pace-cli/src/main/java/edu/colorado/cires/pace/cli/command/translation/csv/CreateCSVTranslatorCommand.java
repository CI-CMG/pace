package edu.colorado.cires.pace.cli.command.translation.csv;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.CSVTranslator;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "create", description = "Create CSV translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class CreateCSVTranslatorCommand extends CreateCommand<CSVTranslator> {
  
  @Parameters(description = "file containing CSV translator (- for stdin)")
  private File csvTranslator;

  @Override
  protected Supplier<File> getJsonBlobProvider() {
    return () -> csvTranslator;
  }

  @Override
  protected Class<CSVTranslator> getJsonClass() {
    return CSVTranslator.class;
  }

  @Override
  protected RepositoryFactory<CSVTranslator> getRepositoryFactory() {
    return CSVTranslatorRepositoryFactory::createRepository;
  }
}
