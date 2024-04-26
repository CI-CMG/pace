package edu.colorado.cires.pace.cli.command.dataset;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.TranslateCommand;
import edu.colorado.cires.pace.cli.command.common.TranslationType;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.instrument.InstrumentRepositoryFactory;
import edu.colorado.cires.pace.cli.command.organization.OrganizationRepositoryFactory;
import edu.colorado.cires.pace.cli.command.person.PersonRepositoryFactory;
import edu.colorado.cires.pace.cli.command.platform.PlatformRepositoryFactory;
import edu.colorado.cires.pace.cli.command.project.ProjectRepositoryFactory;
import edu.colorado.cires.pace.cli.command.sensor.SensorRepositoryFactory;
import edu.colorado.cires.pace.cli.command.soundSource.SoundSourceRepositoryFactory;
import edu.colorado.cires.pace.data.object.Dataset;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "dataset", description = "Manage datasets", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
public class DatasetCommand extends TranslateCommand<Dataset> {

  @Parameters(description = "File to translate from")
  private File file;

  @Option(names = {"--translate-from", "-tf"}, description = "File format to translate from", required = true)
  private TranslationType translationType;

  @Option(names = {"--translator-name", "-tn"}, description = "Translator name", required = true)
  private String translatorName;

  @Override
  protected Supplier<TranslationType> getTranslationTypeSupplier() {
    return () -> translationType;
  }

  @Override
  protected Supplier<String> getTranslatorNameSupplier() {
    return () -> translatorName;
  }

  @Override
  protected Supplier<File> getInputSupplier() {
    return () -> file;
  }

  @Override
  protected Class<Dataset> getJsonClass() {
    return Dataset.class;
  }

  @Override
  protected RepositoryFactory[] getDependencyRepositoryFactories() {
    return new RepositoryFactory[]{
        PersonRepositoryFactory::createRepository,
        ProjectRepositoryFactory::createRepository,
        OrganizationRepositoryFactory::createRepository,
        PlatformRepositoryFactory::createRepository,
        InstrumentRepositoryFactory::createRepository,
        SensorRepositoryFactory::createRepository,
        SoundSourceRepositoryFactory::createRepository
    };
  }
}
