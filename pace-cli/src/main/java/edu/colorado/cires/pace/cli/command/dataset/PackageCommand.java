package edu.colorado.cires.pace.cli.command.dataset;

import static edu.colorado.cires.pace.utilities.SerializationUtils.createObjectMapper;
import static edu.colorado.cires.pace.utilities.SerializationUtils.deserializeAndProcess;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.GenerateTranslatorCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.TranslateCommand;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.dataset.PackageCommand.Create;
import edu.colorado.cires.pace.cli.command.dataset.PackageCommand.Delete;
import edu.colorado.cires.pace.cli.command.dataset.PackageCommand.FindAll;
import edu.colorado.cires.pace.cli.command.dataset.PackageCommand.GetByPackageId;
import edu.colorado.cires.pace.cli.command.dataset.PackageCommand.GetByUUID;
import edu.colorado.cires.pace.cli.command.dataset.PackageCommand.Pack;
import edu.colorado.cires.pace.cli.command.dataset.PackageCommand.Update;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.translator.FieldNameFactory;
import edu.colorado.cires.pace.utilities.TranslationType;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.dataset.PackageCommand.GenerateTranslator;
import edu.colorado.cires.pace.cli.command.dataset.PackageCommand.Translate;
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
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "package", description = "Manage packages", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class,
subcommands = {
    Create.class,
    FindAll.class,
    GetByUUID.class,
    GetByPackageId.class,
    Update.class,
    Delete.class,
    Translate.class,
    Pack.class,
    GenerateTranslator.class
})
public class PackageCommand implements Runnable {
  
  private static final RepositoryFactory<Package> repositoryFactory = PackageRepositoryFactory::createRepository;
  private static final Class<Package> clazz = Package.class;

  @Override
  public void run() {}
  
  @Command(name = "create", description = "Create packages", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Create extends CreateCommand<Package> {
    
    @Parameters(description = "File containing package (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<Package> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<Package>> getTypeReference() {
      return new TypeReference<>() {};
    }

    @Override
    protected RepositoryFactory<Package> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "list", description = "List packages", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class FindAll extends FindAllCommand<Package> {

    @Override
    protected RepositoryFactory<Package> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "get-by-uuid", description = "Get package by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByUUID extends GetByUUIDCommand<Package> {
    
    @Parameters(description = "Package uuid")
    private UUID uuid;
    
    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<Package> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "get-by-package-id", description = "Get package by packageId", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByPackageId extends GetByUniqueFieldCommand<Package> {
    
    @Parameters(description = "Get package by package id")
    private String packageId;

    @Override
    protected Supplier<String> getUniqueFieldProvider() {
      return () -> packageId;
    }

    @Override
    protected RepositoryFactory<Package> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "update", description = "Update package", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Update extends UpdateCommand<Package> {
    
    @Parameters(description = "File containing package (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<Package> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<Package>> getTypeReference() {
      return new TypeReference<>() {};
    }

    @Override
    protected RepositoryFactory<Package> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "delete", description = "Delete package", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Delete extends DeleteCommand<Package> {
    
    @Parameters(description = "Package uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<Package> getRepositoryFactory() {
      return repositoryFactory;
    }
  }

  @Command(name = "translate", description = "Translate packages", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Translate extends TranslateCommand<Package> {

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
    protected Class<Package> getJsonClass() {
      return Package.class;
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
  
  @Command(name = "process", description = "Process packages", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Pack implements Runnable {

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
              Package.class,
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
  static class GenerateTranslator extends GenerateTranslatorCommand<Package> {
    
    @Option(names = {"--dataset-type", "-dt"}, description = "Dataset type", required = true, converter = DatasetTypeConverter.class)
    private DatasetType datasetType;
    
    @Option(names = {"--location-type", "-lt"}, description = "Deployment location type", required = true, converter = LocationTypeConverter.class)
    private LocationType locationType;
    
    @Parameters(description = "Translator type")
    private TranslationType translatorType;

    @Override
    protected Class<Package> getClazz() {
      return Package.class;
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