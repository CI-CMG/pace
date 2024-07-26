package edu.colorado.cires.pace.cli.command.sensor;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.TranslateCommand;
import edu.colorado.cires.pace.data.object.sensor.base.translator.SensorTranslator;
import edu.colorado.cires.pace.translator.converter.Converter;
import edu.colorado.cires.pace.translator.converter.SensorConverter;
import edu.colorado.cires.pace.utilities.TranslationType;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.sensor.SensorCommand.Create;
import edu.colorado.cires.pace.cli.command.sensor.SensorCommand.Delete;
import edu.colorado.cires.pace.cli.command.sensor.SensorCommand.FindAll;
import edu.colorado.cires.pace.cli.command.sensor.SensorCommand.GetByName;
import edu.colorado.cires.pace.cli.command.sensor.SensorCommand.GetByUUID;
import edu.colorado.cires.pace.cli.command.sensor.SensorCommand.Translate;
import edu.colorado.cires.pace.cli.command.sensor.SensorCommand.Update;
import edu.colorado.cires.pace.data.object.sensor.base.Sensor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "sensor", description = "Manage sensors", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class, subcommands = {
    Create.class,
    FindAll.class,
    GetByUUID.class,
    GetByName.class,
    Update.class,
    Delete.class,
    Translate.class
})
public class SensorCommand {
  
  private static final RepositoryFactory<Sensor> repositoryFactory = SensorRepositoryFactory::createJsonRepository;
  private static final Class<Sensor> clazz = Sensor.class;
  private static final TypeReference<List<Sensor>> typeReference = new TypeReference<>() {};
  
  @Command(name = "create", description = "Create sensor", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Create extends CreateCommand<Sensor> {
    
    @Parameters(description = "File containing sensor (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<Sensor> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<Sensor>> getTypeReference() {
      return typeReference;
    }

    @Override
    protected RepositoryFactory<Sensor> getRepositoryFactory() {
      return SensorRepositoryFactory::createJsonRepository;
    }
  }
  
  @Command(name = "list", description = "List sensors", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class FindAll extends FindAllCommand<Sensor> {

    @Option(names = { "--names", "-n" }, split = ",", description = "Filter results based on names")
    private List<String> names = new ArrayList<>(0);

    @Option(names = {"--show-hidden"}, description = "Filter results based on visibility", defaultValue = "false")
    private Boolean showHidden;

    @Option(names = {"--show-visible"}, description = "Filter results based on visibility", defaultValue = "false")
    private Boolean showVisible;

    @Override
    protected RepositoryFactory<Sensor> getRepositoryFactory() {
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
  
  @Command(name = "get-by-uuid", description = "Get sensor by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByUUID extends GetByUUIDCommand<Sensor> {
    
    @Parameters(description = "Sensor uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<Sensor> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "get-by-name", description = "Get sensor by name", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GetByName extends GetByUniqueFieldCommand<Sensor> {
    
    @Parameters(description = "Sensor name")
    private String name;

    @Override
    protected Supplier<String> getUniqueFieldProvider() {
      return () -> name;
    }

    @Override
    protected RepositoryFactory<Sensor> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "update", description = "Update sensor", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Update extends UpdateCommand<Sensor> {
    
    @Parameters(description = "File containing sensor (- for stdin)")
    private File file;

    @Override
    protected Supplier<File> getJsonBlobProvider() {
      return () -> file;
    }

    @Override
    protected Class<Sensor> getJsonClass() {
      return clazz;
    }

    @Override
    public TypeReference<List<Sensor>> getTypeReference() {
      return typeReference;
    }

    @Override
    protected RepositoryFactory<Sensor> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "delete", description = "Delete sensor", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Delete extends DeleteCommand<Sensor> {
    
    @Parameters(description = "Sensor uuid")
    private UUID uuid;

    @Override
    protected Supplier<UUID> getUUIDProvider() {
      return () -> uuid;
    }

    @Override
    protected RepositoryFactory<Sensor> getRepositoryFactory() {
      return repositoryFactory;
    }
  }
  
  @Command(name = "translate", description = "Translate sensor from spreadsheet", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class Translate extends TranslateCommand<Sensor, SensorTranslator> {

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
    protected Converter<SensorTranslator, Sensor> getConverter() {
      return new SensorConverter();
    }

    @Override
    protected TypeReference<List<Sensor>> getTypeReference() {
      return typeReference;
    }

    @Override
    protected Class<Sensor> getClazz() {
      return clazz;
    }
  }
}
