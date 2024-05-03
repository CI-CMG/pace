package edu.colorado.cires.pace.cli.command.dataset;

import static edu.colorado.cires.pace.cli.util.SerializationUtils.createObjectMapper;
import static edu.colorado.cires.pace.cli.util.SerializationUtils.deserializeAndProcess;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.common.GenerateTranslatorCommand;
import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.TranslateCommand;
import edu.colorado.cires.pace.cli.command.common.TranslationType;
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
import edu.colorado.cires.pace.cli.util.ApplicationPropertyResolver;
import edu.colorado.cires.pace.cli.util.CLIProgressIndicator;
import edu.colorado.cires.pace.data.SoundPropagationModelsPackingJob;
import edu.colorado.cires.pace.data.object.AudioDataset;
import edu.colorado.cires.pace.data.object.AudioPackingJob;
import edu.colorado.cires.pace.data.object.CPODPackingJob;
import edu.colorado.cires.pace.data.object.CPodDataset;
import edu.colorado.cires.pace.data.object.Channel;
import edu.colorado.cires.pace.data.object.DataQualityEntry;
import edu.colorado.cires.pace.data.object.DetectionsDataset;
import edu.colorado.cires.pace.data.object.DetectionsPackingJob;
import edu.colorado.cires.pace.data.object.DutyCycle;
import edu.colorado.cires.pace.data.object.Gain;
import edu.colorado.cires.pace.data.object.LocationDetail;
import edu.colorado.cires.pace.data.object.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.MultiPointStationaryMarineLocation;
import edu.colorado.cires.pace.data.object.PackingJob;
import edu.colorado.cires.pace.data.object.SampleRate;
import edu.colorado.cires.pace.data.object.SoundClipsDataset;
import edu.colorado.cires.pace.data.object.SoundClipsPackingJob;
import edu.colorado.cires.pace.data.object.SoundLevelMetricsDataset;
import edu.colorado.cires.pace.data.object.SoundLevelMetricsPackingJob;
import edu.colorado.cires.pace.data.object.SoundPropagationModelsDataset;
import edu.colorado.cires.pace.data.object.StationaryMarineLocation;
import edu.colorado.cires.pace.data.object.StationaryTerrestrialLocation;
import edu.colorado.cires.pace.packaging.PackageProcessor;
import edu.colorado.cires.pace.packaging.PackagingException;
import edu.colorado.cires.pace.packaging.ProgressIndicator;
import edu.colorado.cires.pace.translator.DatasetType;
import edu.colorado.cires.pace.translator.LocationType;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
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
  
  private record FieldNameWithType(String fieldName, Type type) {}
  
  private static String appendFieldName(String baseName, String nameToAppend, boolean useListSyntax) {
    if (useListSyntax) {
      return String.format(
          "%s[0].%s", baseName, nameToAppend
      );
    } else {
      return String.format(
          "%s.%s", baseName, nameToAppend
      );
    }
  }
  
  private static List<FieldNameWithType> processType(FieldNameWithType parentField, Type type, List<Type> explicitClasses, boolean useListSyntax)
      throws ClassNotFoundException {
    if (explicitClasses.contains(type)) {
      Class<?> clazz = Class.forName(type.getTypeName());
      List<FieldNameWithType> newFields = fieldsToFieldNames(clazz, null, (f) -> true);

      return newFields.stream()
          .map(f -> new FieldNameWithType(appendFieldName(parentField.fieldName(), f.fieldName(), useListSyntax), f.type()))
          .toList();
    }
    
    return Collections.emptyList();
  }
  
  private static List<FieldNameWithType> processExplicitFields(List<FieldNameWithType> fields, List<Type> explicitClasses)
      throws ClassNotFoundException {
    List<FieldNameWithType> originalFields = new ArrayList<>(fields);
    for (int i = 0; i < originalFields.size(); i++) {
      FieldNameWithType fieldNameWithType = originalFields.get(i);
      Type type = fieldNameWithType.type();
      if (type instanceof ParameterizedType parameterizedType) {
        if (parameterizedType.getRawType().equals(List.class)) {
          Type argumentType = parameterizedType.getActualTypeArguments()[0];
          List<FieldNameWithType> replacementFields = processType(fieldNameWithType, argumentType, explicitClasses, true);
          if (!replacementFields.isEmpty()) {
            fields.remove(fieldNameWithType);
            fields.addAll(replacementFields);
          }
        } else {
          throw new IllegalArgumentException(String.format(
              "Unsupported template data type: %s", parameterizedType.getRawType().getTypeName()
          ));
        }
      } else {
        List<FieldNameWithType> replacementFields = processType(fieldNameWithType, type, explicitClasses, false);
        if (!replacementFields.isEmpty()) {
          fields.remove(fieldNameWithType);
          fields.addAll(i, replacementFields);
        }
      }
    }
    
    return fields;
  }
  
  private static boolean fieldsContainExplicitField(List<FieldNameWithType> fields, List<Type> explicitFields) {
    return fields.stream().anyMatch(f -> {
      Type type = f.type();
      if (type instanceof ParameterizedType parameterizedType) {
        if (parameterizedType.getRawType().equals(List.class)) {
          return explicitFields.contains(parameterizedType.getActualTypeArguments()[0]);
        }
        return false;
      } else {
        return explicitFields.contains(type);
      }
    });
  }

  private static List<String> getDeclaredFields(DatasetType datasetType, LocationType locationType, List<Type> explicitClasses)
      throws ClassNotFoundException {
    List<FieldNameWithType> fields = getBaseFields(datasetType, locationType);
    
    while (fieldsContainExplicitField(fields, explicitClasses)) {
      fields = processExplicitFields(fields, explicitClasses);
    }
    
    return fields.stream()
        .map(FieldNameWithType::fieldName)
        .toList();
  }

  private static List<FieldNameWithType> getBaseFields(DatasetType datasetType, LocationType locationType) {
    Function<Field, Boolean> locationFilter = (f) -> !f.getType().equals(LocationDetail.class);

    List<FieldNameWithType> fieldNames = switch (datasetType) {
      case CPOD -> fieldsToFieldNames(CPodDataset.class, CPODPackingJob.class, locationFilter);
      case AUDIO -> fieldsToFieldNames(AudioDataset.class, AudioPackingJob.class, locationFilter);
      case DETECTIONS -> fieldsToFieldNames(DetectionsDataset.class, DetectionsPackingJob.class, locationFilter);
      case SOUND_CLIPS -> fieldsToFieldNames(SoundClipsDataset.class, SoundClipsPackingJob.class, locationFilter);
      case SOUND_LEVEL_METRICS -> fieldsToFieldNames(SoundLevelMetricsDataset.class, SoundLevelMetricsPackingJob.class, locationFilter);
      case SOUND_PROPAGATION_MODELS -> fieldsToFieldNames(SoundPropagationModelsDataset.class, SoundPropagationModelsPackingJob.class, locationFilter);
    };

    fieldNames.addAll(switch (locationType) {
      case MULTIPOINT_STATIONARY_MARINE -> fieldsToFieldNames(MultiPointStationaryMarineLocation.class, null, (f) -> true);
      case STATIONARY_TERRESTRIAL -> fieldsToFieldNames(StationaryTerrestrialLocation.class, null, (f) -> true);
      case STATIONARY_MARINE -> fieldsToFieldNames(StationaryMarineLocation.class, null, (f) -> true);
      case MOBILE_MARINE -> fieldsToFieldNames(MobileMarineLocation.class, null, (f) -> true);
    });

    return fieldNames;
  }

  private static List<FieldNameWithType> fieldsToFieldNames(Class<?> clazz, Class<?> additionalClazz, Function<Field, Boolean> filter) {
    List<FieldNameWithType> fields = Arrays.stream(clazz.getDeclaredFields())
        .filter(filter::apply)
        .map(f -> new FieldNameWithType(f.getName(), f.getGenericType()))
        .collect(Collectors.toList());

    if (additionalClazz != null) {
      fields.addAll(Arrays.stream(additionalClazz.getDeclaredFields())
          .map(f -> new FieldNameWithType(f.getName(), f.getGenericType()))
          .toList());
    }

    return fields;
  }
  
  @Command(name = "generate-translator", description = "Generate default CSV or Excel translator", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
  static class GenerateTranslator extends GenerateTranslatorCommand<PackingJob> {
    
    @Option(names = {"--dataset-type", "-dt"}, description = "Dataset type")
    private DatasetType datasetType;
    
    @Option(names = {"--location-type", "-lt"}, description = "Deployment location type", required = true)
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
    protected List<String> getFieldNames() throws Exception {
      return getDeclaredFields(datasetType, locationType, List.of(
          MarineInstrumentLocation.class,
          Channel.class,
          DataQualityEntry.class,
          SampleRate.class,
          DutyCycle.class,
          Gain.class
      ));
    }

    @Override
    protected String getTranslatorName() {
      return String.format(
          "%s-%s-%s", super.getTranslatorName(), datasetType.getName(), locationType.getName() 
      );
    }
  }
  
}
