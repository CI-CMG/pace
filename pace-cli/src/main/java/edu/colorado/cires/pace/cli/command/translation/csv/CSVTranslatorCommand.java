package edu.colorado.cires.pace.cli.command.translation.csv;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.translation.csv.CSVTranslatorCommand.Create;
import edu.colorado.cires.pace.cli.command.translation.csv.CSVTranslatorCommand.Delete;
import edu.colorado.cires.pace.cli.command.translation.csv.CSVTranslatorCommand.FindAll;
import edu.colorado.cires.pace.cli.command.translation.csv.CSVTranslatorCommand.GenerateCSV;
import edu.colorado.cires.pace.cli.command.translation.csv.CSVTranslatorCommand.GetByName;
import edu.colorado.cires.pace.cli.command.translation.csv.CSVTranslatorCommand.GetByUUID;
import edu.colorado.cires.pace.cli.command.translation.csv.CSVTranslatorCommand.Update;
import edu.colorado.cires.pace.cli.util.ApplicationPropertyResolver;
import edu.colorado.cires.pace.cli.util.SerializationUtils;
import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.translator.csv.CSVGenerator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "csv-translator", description = "Manage CSV translators", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class, subcommands = {
    Create.class,
    FindAll.class,
    GetByUUID.class,
    GetByName.class,
    Update.class,
    Delete.class,
    GenerateCSV.class
})
public class CSVTranslatorCommand implements Runnable {
  
  private static final RepositoryFactory<CSVTranslator> repositoryFactory = CSVTranslatorRepositoryFactory::createRepository;
  private static final Class<CSVTranslator> clazz = CSVTranslator.class;

  @Override
  public void run() {}

  @Command(name = "create", description = "Create a CSV translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Create extends CreateCommand<CSVTranslator> {
    
    @Parameters(description = "File containing CSV translator (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<CSVTranslator> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<CSVTranslator>> getTypeReference() {
      return new TypeReference<>() {};
    }

    @Override
    protected RepositoryFactory<CSVTranslator> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "list", description = "List CSV translators", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class FindAll extends FindAllCommand<CSVTranslator> {

    @Override
    protected RepositoryFactory<CSVTranslator> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "get-by-name", description = "Get CSV translator by name", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByName extends GetByUniqueFieldCommand<CSVTranslator> {
    
    @Parameters(description = "CSV translator name")
    private String name;
    
    @Override
    protected Supplier<String> getUniqueFieldProvider() {
      return () -> name;
    }

    @Override
    protected RepositoryFactory<CSVTranslator> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "get-by-uuid", description = "Get CSV translator by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByUUID extends GetByUUIDCommand<CSVTranslator> {
    
    @Parameters(description = "CSV translator uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<CSVTranslator> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "update", description = "Update CSV translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Update extends UpdateCommand<CSVTranslator> {
    
    @Parameters(description = "FIle containing CSV translator (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<CSVTranslator> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<CSVTranslator>> getTypeReference() {
      return new TypeReference<>() {};
    }

    @Override
    protected RepositoryFactory<CSVTranslator> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "delete", description = "Delete CSV translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Delete extends DeleteCommand<CSVTranslator> {
    
    @Parameters(description = "CSV translator uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<CSVTranslator> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "generate-csv", description = "Generate CSV from translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GenerateCSV implements Runnable {
    
    @Parameters(description = "Output file path")
    private File file;
    
    @Parameters(description = "Translator name")
    private String translatorName;

    private Path getDatastoreDirectory() {
      return new ApplicationPropertyResolver().getWorkDir();
    }

    @Override
    public void run() {
      try {
        CRUDRepository<CSVTranslator> repository = repositoryFactory.createRepository(getDatastoreDirectory(), SerializationUtils.createObjectMapper());
        CSVTranslator csvTranslator = repository.getByUniqueField(translatorName);
        new CSVGenerator().generateSpreadsheet(file.toPath(), csvTranslator);
      } catch (IOException | NotFoundException | DatastoreException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
