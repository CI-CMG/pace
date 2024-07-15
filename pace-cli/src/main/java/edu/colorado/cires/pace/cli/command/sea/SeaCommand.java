package edu.colorado.cires.pace.cli.command.sea;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.TranslateCommand;
import edu.colorado.cires.pace.data.translator.SeaTranslator;
import edu.colorado.cires.pace.repository.search.SeaSearchParameters;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import edu.colorado.cires.pace.translator.converter.Converter;
import edu.colorado.cires.pace.translator.converter.SeaConverter;
import edu.colorado.cires.pace.utilities.TranslationType;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.sea.SeaCommand.Create;
import edu.colorado.cires.pace.cli.command.sea.SeaCommand.Delete;
import edu.colorado.cires.pace.cli.command.sea.SeaCommand.FindAll;
import edu.colorado.cires.pace.cli.command.sea.SeaCommand.GetByName;
import edu.colorado.cires.pace.cli.command.sea.SeaCommand.GetByUUID;
import edu.colorado.cires.pace.cli.command.sea.SeaCommand.Translate;
import edu.colorado.cires.pace.cli.command.sea.SeaCommand.Update;
import edu.colorado.cires.pace.data.object.Sea;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "sea", description = "Manage seas", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class, subcommands = {
    Create.class,
    FindAll.class,
    GetByUUID.class,
    GetByName.class,
    Update.class,
    Delete.class,
    Translate.class
})
public class SeaCommand {
  
  private static final RepositoryFactory<Sea> repositoryFactory = SeaRepositoryFactory::createJsonRepository;
  private static final Class<Sea> clazz = Sea.class;
  private static final TypeReference<List<Sea>> typeReference = new TypeReference<>() {};
  
  @Command(name = "create", description = "Create sea area", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Create extends CreateCommand<Sea> {
    
    @Parameters(description = "File containing sea area (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<Sea> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<Sea>> getTypeReference() {
      return typeReference;
    }

    @Override
    protected RepositoryFactory<Sea> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "list", description = "List sea areas", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class FindAll extends FindAllCommand<Sea> {

    @Option(names = { "--names", "-n" }, split = ",", description = "Filter results based on names")
    private List<String> names = new ArrayList<>(0);

    @Override
    protected RepositoryFactory<Sea> getRepositoryFactory() {
      return repositoryFactory;
    }

    @Override
    protected SearchParameters<Sea> getSearchParameters() {
      return SeaSearchParameters.builder()
          .names(names)
          .build();
    }
  }
  
  @Command(name = "get-by-uuid", description = "Get sea area by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByUUID extends GetByUUIDCommand<Sea> {
    
    @Parameters(description = "Sea area uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<Sea> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "get-by-name", description = "Get sea area by name", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByName extends GetByUniqueFieldCommand<Sea> {
    
    @Parameters(description = "Sea area name")
    private String name;

    @Override
    protected Supplier<String> getUniqueFieldProvider() {
      return () -> name;
    }

    @Override
    protected RepositoryFactory<Sea> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "update", description = "Update sea area (- for stdin)", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Update extends UpdateCommand<Sea> {
    
    @Parameters(description = "File containing sea area (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<Sea> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<Sea>> getTypeReference() {
      return typeReference;
    }

    @Override
    protected RepositoryFactory<Sea> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "delete", description = "Delete sea area", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Delete extends DeleteCommand<Sea> {
    
    @Parameters(description = "Sea area uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<Sea> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "translate", description = "Translate sea area from spreadsheet")
  static class Translate extends TranslateCommand<Sea, SeaTranslator> {

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
    protected Converter<SeaTranslator, Sea> getConverter() {
      return new SeaConverter();
    }

    @Override
    protected TypeReference<List<Sea>> getTypeReference() {
      return typeReference;
    }

    @Override
    protected Class<Sea> getClazz() {
      return clazz;
    }

  }
}
