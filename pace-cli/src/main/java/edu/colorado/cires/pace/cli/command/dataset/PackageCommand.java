package edu.colorado.cires.pace.cli.command.dataset;

import static edu.colorado.cires.pace.utilities.SerializationUtils.createObjectMapper;
import static edu.colorado.cires.pace.utilities.SerializationUtils.deserializeAndProcess;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
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
import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.translator.PackageTranslator;
import edu.colorado.cires.pace.translator.converter.Converter;
import edu.colorado.cires.pace.translator.converter.PackageConverter;
import edu.colorado.cires.pace.utilities.TranslationType;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
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
import edu.colorado.cires.pace.packaging.ProgressIndicator;
import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
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
    Pack.class
})
public class PackageCommand implements Runnable {
  
  private static final RepositoryFactory<Package> repositoryFactory = PackageRepositoryFactory::createRepository;
  private static final Class<Package> clazz = Package.class;
  private static final TypeReference<List<Package>> typeReference = new TypeReference<>() {};

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
      return typeReference;
    }

    @Override
    protected RepositoryFactory<Package> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Package> getClazz() {
      return clazz;
    }
  }
  
  @Command(name = "list", description = "List packages", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class FindAll extends FindAllCommand<Package> {

    @Override
    protected RepositoryFactory<Package> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Package> getClazz() {
      return clazz;
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

    @Override
    protected Class<Package> getClazz() {
      return clazz;
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

    @Override
    protected Class<Package> getClazz() {
      return clazz;
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
      return typeReference;
    }

    @Override
    protected RepositoryFactory<Package> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected Class<Package> getClazz() {
      return clazz;
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

    @Override
    protected Class<Package> getClazz() {
      return clazz;
    }
  }

  @Command(name = "translate", description = "Translate packages", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Translate extends TranslateCommand<Package, PackageTranslator> {

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
    protected RepositoryFactory<Package>[] getDependencyRepositoryFactories() {
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

    @Override
    protected Converter<PackageTranslator, Package> getConverter() throws IOException {
      return new PackageConverter(
          PersonRepositoryFactory.createJsonRepository(workDir, objectMapper),
          ProjectRepositoryFactory.createJsonRepository(workDir, objectMapper),
          OrganizationRepositoryFactory.createJsonRepository(workDir, objectMapper),
          PlatformRepositoryFactory.createJsonRepository(workDir, objectMapper),
          InstrumentRepositoryFactory.createJsonRepository(workDir, objectMapper),
          SeaRepositoryFactory.createJsonRepository(workDir, objectMapper),
          ShipRepositoryFactory.createJsonRepository(workDir, objectMapper),
          DetectionTypeRepositoryFactory.createJsonRepository(workDir, objectMapper),
          SensorRepositoryFactory.createRepository(workDir, objectMapper)
      );
    }

    @Override
    protected TypeReference<List<Package>> getTypeReference() {
      return typeReference;
    }
  }
  
  @Command(name = "process", description = "Process packages", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Pack implements Runnable {

    @Parameters(description = "file containing package job(s) (- for stdin)")
    private File packageJob;
    
    @Parameters(description = "output directory")
    private File outputDirectory;

    @Override
    public void run() {
      try {
        Path workDir = new ApplicationPropertyResolver().getDataDir();
        ObjectMapper objectMapper = createObjectMapper();
        
        List<Person> people = PersonRepositoryFactory.createJsonRepository(workDir, objectMapper).findAll().toList();
        List<Organization> organizations = OrganizationRepositoryFactory.createJsonRepository(workDir, objectMapper).findAll().toList();
        List<Project> projects = ProjectRepositoryFactory.createJsonRepository(workDir, objectMapper).findAll().toList();
        
        Path outputPath = outputDirectory.toPath();
        
        ProgressIndicator[] progressIndicators = new ProgressIndicator[]{
            new CLIProgressIndicator()
        };
        
        
        try {
          List<Package> packages = new ArrayList<>(0);
          deserializeAndProcess(
              objectMapper,
              packageJob,
              Package.class,
              typeReference,
              (deserializedObject) -> {
                packages.add(deserializedObject);
                return deserializedObject;
              }
          );
          
          PackageProcessor packageProcessor = new PackageProcessor(
              objectMapper, people, organizations, projects, packages, outputPath, progressIndicators
          );
          
          packageProcessor.process();
        } catch (RuntimeException exception) {
          throw exception.getCause();
        }
      } catch (Throwable e) {
        throw new IllegalStateException("Packaging failed", e);
      }
    }
  }
  
}
