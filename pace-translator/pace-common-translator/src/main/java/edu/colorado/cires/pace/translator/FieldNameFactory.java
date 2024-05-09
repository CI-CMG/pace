package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.SoundPropagationModelsPackage;
import edu.colorado.cires.pace.data.object.AudioDataset;
import edu.colorado.cires.pace.data.object.AudioPackage;
import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.CPODPackage;
import edu.colorado.cires.pace.data.object.CPodDataset;
import edu.colorado.cires.pace.data.object.Channel;
import edu.colorado.cires.pace.data.object.DataQualityEntry;
import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.DetectionsDataset;
import edu.colorado.cires.pace.data.object.DetectionsPackage;
import edu.colorado.cires.pace.data.object.DutyCycle;
import edu.colorado.cires.pace.data.object.Gain;
import edu.colorado.cires.pace.data.object.LocationDetail;
import edu.colorado.cires.pace.data.object.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.MultiPointStationaryMarineLocation;
import edu.colorado.cires.pace.data.object.OtherSensor;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.SampleRate;
import edu.colorado.cires.pace.data.object.SoundClipsDataset;
import edu.colorado.cires.pace.data.object.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.SoundLevelMetricsDataset;
import edu.colorado.cires.pace.data.object.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.SoundPropagationModelsDataset;
import edu.colorado.cires.pace.data.object.StationaryMarineLocation;
import edu.colorado.cires.pace.data.object.StationaryTerrestrialLocation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class FieldNameFactory {
  
  public static List<String> getDefaultDeclaredFields(Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredFields())
        .map(Field::getName)
        .toList();
  }

  public static List<String> getSensorDeclaredFields(SensorType sensorType) {
    List<FieldNameWithType> fields = Arrays.stream(switch (sensorType) {
      case other -> OtherSensor.class.getDeclaredFields();
      case depth -> DepthSensor.class.getDeclaredFields();
      case audio -> AudioSensor.class.getDeclaredFields();
    }).map(f -> new FieldNameWithType(f.getName(), f.getGenericType())).collect(Collectors.toList());
    
    List<Type> explicitClasses = List.of(
        Position.class
    );
    
    while (fieldsContainExplicitField(fields, explicitClasses)) {
      fields = processExplicitFields(fields, explicitClasses);
    }

    List<String> fieldNames = fields.stream()
        .map(FieldNameWithType::fieldName)
        .collect(Collectors.toList());

    fieldNames.add(1, "type");
    
    return fieldNames;
  }

  public static List<String> getDatasetDeclaredFields(DatasetType datasetType, LocationType locationType) {
    List<FieldNameWithType> fields = getBaseFields(datasetType, locationType);
    
    List<Type> explicitClasses = List.of(
        MarineInstrumentLocation.class,
        Channel.class,
        DataQualityEntry.class,
        SampleRate.class,
        DutyCycle.class,
        Gain.class
    );

    while (fieldsContainExplicitField(fields, explicitClasses)) {
      fields = processExplicitFields(fields, explicitClasses);
    }

    List<String> fieldNames = fields.stream()
        .map(FieldNameWithType::fieldName)
        .collect(Collectors.toList());

    fieldNames.add(1, "datasetType");
    fieldNames.add(2, "locationType");
    
    return fieldNames;
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

  private static List<FieldNameWithType> processType(FieldNameWithType parentField, Type type, List<Type> explicitClasses, boolean useListSyntax) {
    if (explicitClasses.contains(type)) {
      Class<?> clazz;
      try {
        clazz = Class.forName(type.getTypeName());
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
      List<FieldNameWithType> newFields = fieldsToFieldNames(clazz, null, (f) -> true);

      return newFields.stream()
          .map(f -> new FieldNameWithType(appendFieldName(parentField.fieldName(), f.fieldName(), useListSyntax), f.type()))
          .toList();
    }

    return Collections.emptyList();
  }

  private static List<FieldNameWithType> processExplicitFields(List<FieldNameWithType> fields, List<Type> explicitClasses) {
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

  private static List<FieldNameWithType> getBaseFields(DatasetType datasetType, LocationType locationType) {
    Function<Field, Boolean> locationFilter = (f) -> !f.getType().equals(LocationDetail.class);

    List<FieldNameWithType> fieldNames = switch (datasetType) {
      case CPOD -> fieldsToFieldNames(CPodDataset.class, CPODPackage.class, locationFilter);
      case AUDIO -> fieldsToFieldNames(AudioDataset.class, AudioPackage.class, locationFilter);
      case DETECTIONS -> fieldsToFieldNames(DetectionsDataset.class, DetectionsPackage.class, locationFilter);
      case SOUND_CLIPS -> fieldsToFieldNames(SoundClipsDataset.class, SoundClipsPackage.class, locationFilter);
      case SOUND_LEVEL_METRICS -> fieldsToFieldNames(SoundLevelMetricsDataset.class, SoundLevelMetricsPackage.class, locationFilter);
      case SOUND_PROPAGATION_MODELS -> fieldsToFieldNames(SoundPropagationModelsDataset.class, SoundPropagationModelsPackage.class, locationFilter);
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

}
