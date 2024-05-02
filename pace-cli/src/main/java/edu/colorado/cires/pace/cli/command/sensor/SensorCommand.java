package edu.colorado.cires.pace.cli.command.sensor;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.GenerateTranslatorCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.TranslateCommand;
import edu.colorado.cires.pace.cli.command.common.TranslationType;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.sensor.SensorCommand.Create;
import edu.colorado.cires.pace.cli.command.sensor.SensorCommand.Delete;
import edu.colorado.cires.pace.cli.command.sensor.SensorCommand.FindAll;
import edu.colorado.cires.pace.cli.command.sensor.SensorCommand.GenerateTranslator;
import edu.colorado.cires.pace.cli.command.sensor.SensorCommand.GetByName;
import edu.colorado.cires.pace.cli.command.sensor.SensorCommand.GetByUUID;
import edu.colorado.cires.pace.cli.command.sensor.SensorCommand.Translate;
import edu.colorado.cires.pace.cli.command.sensor.SensorCommand.Update;
import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.CSVTranslatorField;
import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import edu.colorado.cires.pace.data.object.OtherSensor;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.translator.SensorType;
import java.io.File;
import java.lang.reflect.Field;
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
    Translate.class,
    GenerateTranslator.class
})
public class SensorCommand implements Runnable {
  
  private static final RepositoryFactory<Sensor> repositoryFactory = SensorRepositoryFactory::createRepository;
  private static final Class<Sensor> clazz = Sensor.class;

  @Override
  public void run() {}
  
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
      return new TypeReference<>() {};
    }

    @Override
    protected RepositoryFactory<Sensor> getRepositoryFactory() {
      return SensorRepositoryFactory::createRepository;
    }
  }
  
  @Command(name = "list", description = "List sensors", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class FindAll extends FindAllCommand<Sensor> {

    @Override
    protected RepositoryFactory<Sensor> getRepositoryFactory() {
      return repositoryFactory;
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
      return new TypeReference<>() {};
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
  static class Translate extends TranslateCommand<Sensor> {

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
    protected Class<Sensor> getJsonClass() {
      return clazz;
    }

    @Override
    protected RepositoryFactory[] getDependencyRepositoryFactories() {
      return new RepositoryFactory[0];
    }
  }
  
  @Command(name = "generate-translator", description = "Generate default CSV or Excel translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GenerateTranslator extends GenerateTranslatorCommand<Sensor> {

    @Parameters(description = "Translator type")
    private TranslationType translatorType;
    
    @Parameters(description = "Sensor type")
    private SensorType sensorType;
    
    @Override
    protected Class<Sensor> getClazz() {
      return clazz;
    }

    @Override
    protected TranslationType getTranslatorType() {
      return translatorType;
    }
    
    private Field[] getDeclaredFields(SensorType sensorType) {
      return switch (sensorType) {
        case other -> OtherSensor.class.getDeclaredFields();
        case depth -> DepthSensor.class.getDeclaredFields();
        case audio -> AudioSensor.class.getDeclaredFields();
      };
    }

    @Override
    protected CSVTranslator getCSVTranslator() {
      List<CSVTranslatorField> fields = new ArrayList<>(0);

      Field[] objectFields = getDeclaredFields(sensorType);

      for (int i = 0; i < objectFields.length; i++) {
        fields.add(CSVTranslatorField.builder()
                .propertyName(objectFields[i].getName())
                .columnNumber(i + 1)
            .build());
      }
      
      return CSVTranslator.builder()
          .name(String.format(
              "%s-%s", clazz.getSimpleName(), sensorType.name()
          ))
          .fields(fields)
          .build();
    }

    @Override
    protected ExcelTranslator getExcelTranslator() {
      List<ExcelTranslatorField> fields = new ArrayList<>(0);
      
      Field[] objectFields = getDeclaredFields(sensorType);

      for (int i = 0; i < objectFields.length; i++) {
        fields.add(ExcelTranslatorField.builder()
                .propertyName(objectFields[i].getName())
                .sheetNumber(1)
                .columnNumber(i + 1)
            .build());
      }
      
      return ExcelTranslator.builder()
          .name(String.format(
              "%s-%s", clazz.getSimpleName(), sensorType.name()
          ))
          .fields(fields)
          .build();
    }
  }
}
