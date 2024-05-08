package edu.colorado.cires.pace.cli.command.dataset;

import static edu.colorado.cires.pace.utilities.SerializationUtils.createObjectMapper;
import static edu.colorado.cires.pace.utilities.SerializationUtils.deserializeAndProcess;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.common.GenerateTranslatorCommand;
import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.TranslateCommand;
import edu.colorado.cires.pace.translator.FieldNameFactory;
import edu.colorado.cires.pace.utilities.TranslationType;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.dataset.DatasetCommand.GenerateTranslator;
import edu.colorado.cires.pace.cli.command.dataset.DatasetCommand.Package;
import edu.colorado.cires.pace.cli.command.dataset.DatasetCommand.Translate;
import edu.colorado.cires.pace.cli.command.detectionType.DetectionTypeRepositoryFactory;
import edu.colorado.cires.pace.cli.command.instrument.InstrumentRepositoryFactory;
import edu.colorado.cires.pace.cli.command.organization.OrganizationRepositoryFactory;
import edu.colorado.cires.pace.cli.command.person.PersonRepositoryFactory;
import edu.colorado.cires.pace.cli.command.platform.PlatformRepositoryFactory;
import edu.colorado.cires.pace.cli.command.project.ProjectRepositoryFactory;
import edu.colorado.cires.pace.cli.command.sea.SeaRepositoryFactory;
import edu.colorado.cires.pace.cli.command.sensor.SensorRepositoryFactory;
import edu.colorado.cires.pace.cli.command.ship.ShipRepositoryFactory;
import edu.colorado.cires.pace.cli.util.CLIProgressIndicator;
import edu.colorado.cires.pace.data.object.PackingJob;
import edu.colorado.cires.pace.packaging.PackageProcessor;
import edu.colorado.cires.pace.packaging.PackagingException;
import edu.colorado.cires.pace.packaging.ProgressIndicator;
import edu.colorado.cires.pace.translator.DatasetType;
import edu.colorado.cires.pace.translator.LocationType;
import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "dataset", description = "Manage datasets", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class,
subcommands = { Translate.class, Package.class, GenerateTranslator.class })
public class DatasetCommand implements Runnable {

  @Override
  public void run() {}

  @Command(name = "translate", description = "Translate datasets", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Translate extends TranslateCommand<PackingJob> {

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
    protected Class<PackingJob> getJsonClass() {
      return PackingJob.class;
    }

    @Override
    protected RepositoryFactory[] getDependencyRepositoryFactories() {
      return new RepositoryFactory[]{
          ProjectRepositoryFactory::createJsonRepository,
          PersonRepositoryFactory::createJsonRepository,
          OrganizationRepositoryFactory::createJsonRepository,
          PlatformRepositoryFactory::createJsonRepository,
          InstrumentRepositoryFactory::createJsonRepository,
          SensorRepositoryFactory::createRepository,
          DetectionTypeRepositoryFactory::createJsonRepository,
          SeaRepositoryFactory::createJsonRepository,
          ShipRepositoryFactory::createJsonRepository
      };
    }
  }
  
  @Command(name = "package", description = "Package datasets", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Package implements Runnable {

    @Parameters(description = "file containing package job (- for stdin)")
    private File packageJob;

    @Override
    public void run() {
      try {
        PackageProcessor packageProcessor = new PackageProcessor(createObjectMapper());
        Path outputPath = new ApplicationPropertyResolver().getWorkDir().resolve("output");
        
        ObjectMapper objectMapper = createObjectMapper();
        
        ProgressIndicator[] progressIndicators = new ProgressIndicator[]{
            new CLIProgressIndicator()
        };
        
        try {
          deserializeAndProcess(
              objectMapper,
              packageJob,
              PackingJob.class,
              new TypeReference<>() {},
              (deserializedObject) -> {
                try {
                  packageProcessor.process(deserializedObject, outputPath, progressIndicators);
                  return deserializedObject;
                } catch (PackagingException | IOException e) {
                  throw new RuntimeException(e);
                }
              }
          );
        } catch (RuntimeException exception) {
          throw exception.getCause();
        }
      } catch (Throwable e) {
        throw new IllegalStateException("Packaging failed", e);
      }
    }
  }

  @Command(name = "generate-translator", description = "Generate default CSV or Excel translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GenerateTranslator extends GenerateTranslatorCommand<PackingJob> {
    
    @Option(names = {"--dataset-type", "-dt"}, description = "Dataset type", required = true, converter = DatasetTypeConverter.class)
    private DatasetType datasetType;
    
    @Option(names = {"--location-type", "-lt"}, description = "Deployment location type", required = true, converter = LocationTypeConverter.class)
    private LocationType locationType;
    
    @Parameters(description = "Translator type")
    private TranslationType translatorType;

    @Override
    protected Class<PackingJob> getClazz() {
      return PackingJob.class;
    }

    @Override
    protected TranslationType getTranslatorType() {
      return translatorType;
    }

    @Override
    protected List<String> getFieldNames() {
      return FieldNameFactory.getDatasetDeclaredFields(datasetType, locationType);
    }

    @Override
    protected String getTranslatorName() {
      return String.format(
          "%s-%s-%s", super.getTranslatorName(), datasetType.getName(), locationType.getName() 
      );
    }
  }
  
}
