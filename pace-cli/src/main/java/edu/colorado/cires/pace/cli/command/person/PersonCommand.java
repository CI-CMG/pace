package edu.colorado.cires.pace.cli.command.person;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.TranslateCommand;
import edu.colorado.cires.pace.data.translator.PersonTranslator;
import edu.colorado.cires.pace.repository.search.PersonSearchParameters;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import edu.colorado.cires.pace.translator.converter.Converter;
import edu.colorado.cires.pace.translator.converter.PersonConverter;
import edu.colorado.cires.pace.utilities.TranslationType;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.person.PersonCommand.Create;
import edu.colorado.cires.pace.cli.command.person.PersonCommand.Delete;
import edu.colorado.cires.pace.cli.command.person.PersonCommand.FindAll;
import edu.colorado.cires.pace.cli.command.person.PersonCommand.GetByName;
import edu.colorado.cires.pace.cli.command.person.PersonCommand.GetByUUID;
import edu.colorado.cires.pace.cli.command.person.PersonCommand.Translate;
import edu.colorado.cires.pace.cli.command.person.PersonCommand.Update;
import edu.colorado.cires.pace.data.object.Person;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "person", description = "Manage people", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class, subcommands = {
    Create.class,
    FindAll.class,
    GetByUUID.class,
    GetByName.class,
    Update.class,
    Delete.class,
    Translate.class
})
public class PersonCommand {
  
  private static final RepositoryFactory<Person> repositoryFactory = PersonRepositoryFactory::createJsonRepository;
  private static final Class<Person> clazz = Person.class;
  private static final TypeReference<List<Person>> typeReference = new TypeReference<>() {};
  
  @Command(name = "create", description = "Create person", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Create extends CreateCommand<Person> {
    
    @Parameters(description = "File containing person (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<Person> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<Person>> getTypeReference() {
      return typeReference;
    }

    @Override
    protected RepositoryFactory<Person> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "list", description = "List people", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class FindAll extends FindAllCommand<Person> {

    @Option(names = { "--names", "-n" }, split = ",", description = "Filter results based on names")
    private List<String> names = new ArrayList<>(0);

    @Override
    protected RepositoryFactory<Person> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected SearchParameters<Person> getSearchParameters() {
      return PersonSearchParameters.builder()
          .names(names)
          .build();
    }
  }
  
  @Command(name = "get-by-uuid", description = "Get person by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByUUID extends GetByUUIDCommand<Person> {
    
    @Parameters(description = "Person uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<Person> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "get-by-name", description = "Get person by name", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByName extends GetByUniqueFieldCommand<Person> {
    
    @Parameters(description = "Person name")
    private String name;

    @Override
    protected Supplier<String> getUniqueFieldProvider() {
      return () -> name;
    }

    @Override
    protected RepositoryFactory<Person> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "update", description = "Update person", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Update extends UpdateCommand<Person> {
    
    @Parameters(description = "File containing person (- from stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<Person> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<Person>> getTypeReference() {
      return typeReference;
    }

    @Override
    protected RepositoryFactory<Person> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "delete", description = "Delete person", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Delete extends DeleteCommand<Person> {
    
    @Parameters(description = "Person uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<Person> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "translate", description = "Translate person from spreadsheet", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Translate extends TranslateCommand<Person, PersonTranslator> {
    
    @Option(names = {"--translate-from", "-tf"}, description = "Input file format", required = true)
    private TranslationType translationType;
    
    @Option(names = {"--translator-name", "-tn"}, description = "Translator name", required = true)
    private String translatorName;
    
    @Parameters(description = "Input file (stdin not supported)")
    private File file;

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
    protected Converter<PersonTranslator, Person> getConverter() {
      return new PersonConverter();
    }

    @Override
    protected TypeReference<List<Person>> getTypeReference() {
      return typeReference;
    }

    @Override
    protected Class<Person> getClazz() {
      return clazz;
    }
  }
}
