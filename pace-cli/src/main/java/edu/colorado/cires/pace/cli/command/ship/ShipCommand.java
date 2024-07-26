package edu.colorado.cires.pace.cli.command.ship;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.TranslateCommand;
import edu.colorado.cires.pace.data.object.ship.translator.ShipTranslator;
import edu.colorado.cires.pace.translator.converter.Converter;
import edu.colorado.cires.pace.translator.converter.ShipConverter;
import edu.colorado.cires.pace.utilities.TranslationType;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.ship.ShipCommand.Create;
import edu.colorado.cires.pace.cli.command.ship.ShipCommand.Delete;
import edu.colorado.cires.pace.cli.command.ship.ShipCommand.FindAll;
import edu.colorado.cires.pace.cli.command.ship.ShipCommand.GetByName;
import edu.colorado.cires.pace.cli.command.ship.ShipCommand.GetByUUID;
import edu.colorado.cires.pace.cli.command.ship.ShipCommand.Translate;
import edu.colorado.cires.pace.cli.command.ship.ShipCommand.Update;
import edu.colorado.cires.pace.data.object.ship.Ship;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "ship", description = "Manage ships", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class, subcommands = {
    Create.class,
    FindAll.class,
    GetByUUID.class,
    GetByName.class,
    Update.class,
    Delete.class,
    Translate.class
})
public class ShipCommand {
  
  private static final RepositoryFactory<Ship> repositoryFactory = ShipRepositoryFactory::createJsonRepository;
  private static final Class<Ship> clazz = Ship.class;
  private static final TypeReference<List<Ship>> typeReference = new TypeReference<>() {};
  
  @Command(name = "create", description = "Create ship", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Create extends CreateCommand<Ship> {
    
    @Parameters(description = "File containing ship (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<Ship> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<Ship>> getTypeReference() {
      return typeReference;
    }

    @Override
    protected RepositoryFactory<Ship> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "list", description = "List ships", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class FindAll extends FindAllCommand<Ship> {

    @Option(names = { "--names", "-n" }, split = ",", description = "Filter results based on names")
    private List<String> names = new ArrayList<>(0);

    @Option(names = {"--show-hidden"}, description = "Filter results based on visibility", defaultValue = "false")
    private Boolean showHidden;

    @Option(names = {"--show-visible"}, description = "Filter results based on visibility", defaultValue = "false")
    private Boolean showVisible;

    @Override
    protected RepositoryFactory<Ship> getRepositoryFactory() {
      return repositoryFactory;
    }


    @Override
    protected List<String> getUniqueFields() {
      return names;
    }

    @Override
    protected Boolean getShowHidden() {
      return showHidden;
    }

    @Override
    protected Boolean getShowVisible() {
      return showVisible;
    }
  }
  
  @Command(name = "get-by-uuid", description = "Get ship by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByUUID extends GetByUUIDCommand<Ship> {
    
    @Parameters(description = "Ship uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<Ship> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "get-by-name", description = "Get ship by name", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByName extends GetByUniqueFieldCommand<Ship> {
    
    @Parameters(description = "Ship name")
    private String name;

    @Override
    protected Supplier<String> getUniqueFieldProvider() {
      return () -> name;
    }

    @Override
    protected RepositoryFactory<Ship> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "update", description = "Update ship", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Update extends UpdateCommand<Ship> {
    
    @Parameters(description = "File containing ship (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<Ship> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<Ship>> getTypeReference() {
      return typeReference;
    }

    @Override
    protected RepositoryFactory<Ship> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "delete", description = "Delete ship", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Delete extends DeleteCommand<Ship> {
    
    @Parameters(description = "Ship uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<Ship> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "translate", description = "Translate ship from spreadsheet", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Translate extends TranslateCommand<Ship, ShipTranslator> {
    
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
    protected Converter<ShipTranslator, Ship> getConverter() {
      return new ShipConverter();
    }

    @Override
    protected TypeReference<List<Ship>> getTypeReference() {
      return typeReference;
    }

    @Override
    protected Class<Ship> getClazz() {
      return clazz;
    }
  }
}
