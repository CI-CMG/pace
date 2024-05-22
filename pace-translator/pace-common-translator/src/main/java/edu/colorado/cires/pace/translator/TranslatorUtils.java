package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.SoundPropagationModelsPackage;
import edu.colorado.cires.pace.data.object.AudioPackage;
import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.CPODPackage;
import edu.colorado.cires.pace.data.object.Channel;
import edu.colorado.cires.pace.data.object.DataQualityEntry;
import edu.colorado.cires.pace.data.object.Dataset;
import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.DetectionType;
import edu.colorado.cires.pace.data.object.DetectionsPackage;
import edu.colorado.cires.pace.data.object.DutyCycle;
import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Gain;
import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.data.object.LocationDetail;
import edu.colorado.cires.pace.data.object.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.MultiPointStationaryMarineLocation;
import edu.colorado.cires.pace.data.object.ObjectWithName;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.object.OtherSensor;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.QualityLevel;
import edu.colorado.cires.pace.data.object.SampleRate;
import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.data.object.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.StationaryMarineLocation;
import edu.colorado.cires.pace.data.object.StationaryTerrestrialLocation;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.repository.FileTypeRepository;
import edu.colorado.cires.pace.repository.InstrumentRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.repository.OrganizationRepository;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.pace.repository.PlatformRepository;
import edu.colorado.cires.pace.repository.ProjectRepository;
import edu.colorado.cires.pace.repository.SeaRepository;
import edu.colorado.cires.pace.repository.SensorRepository;
import edu.colorado.cires.pace.repository.ShipRepository;
import edu.colorado.cires.pace.translator.TranslatorExecutor.ValueWithColumnNumber;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

final class TranslatorUtils {

  public static <O> O convertMapToObject(Map<String, ValueWithColumnNumber> propertyMap, Class<O> clazz, int row, CRUDRepository<?>... dependencyRepositories)
      throws RowConversionException {
    RuntimeException runtimeException = new RuntimeException();
    
    validateTranslation(propertyMap, clazz, runtimeException, row);

    if (runtimeException.getSuppressed().length > 0) {
      RowConversionException exception = new RowConversionException("Translation failed", row);

      for (Throwable throwable : runtimeException.getSuppressed()) {
        exception.addSuppressed(throwable);
      }

      throw exception;
    }
    
    O object;
    if (clazz.isAssignableFrom(Ship.class)) {
      object = (O) objectWithNameFromMap(propertyMap, runtimeException, (o) -> Ship.builder()
          .uuid(o.getUuid())
          .name(o.getName())
          .build(), row);
    } else if (clazz.isAssignableFrom(Sea.class)) {
      object = (O) objectWithNameFromMap(propertyMap, runtimeException, (o) -> Sea.builder()
          .uuid(o.getUuid())
          .name(o.getName())
          .build(), row);
    } else if (clazz.isAssignableFrom(Project.class)) {
      object = (O) objectWithNameFromMap(propertyMap, runtimeException, (o) -> Project.builder()
          .uuid(o.getUuid())
          .name(o.getName())
          .build(), row);
    } else if (clazz.isAssignableFrom(Platform.class)) {
      object = (O) objectWithNameFromMap(propertyMap, runtimeException, (o) -> Platform.builder()
          .uuid(o.getUuid())
          .name(o.getName())
          .build(), row);
    } else if (clazz.isAssignableFrom(Sensor.class)) {
      object = (O) sensorFromMap(propertyMap, runtimeException, row);
    } else if (clazz.isAssignableFrom(DetectionType.class)) {
      object = (O) DetectionType.builder()
          .uuid(getPropertyAsUUID(propertyMap, "uuid", runtimeException, row))
          .source(getPropertyAsString(propertyMap, "source"))
          .scienceName(getPropertyAsString(propertyMap, "scienceName"))
          .build();
    } else if (clazz.isAssignableFrom(FileType.class)) {
      object = (O) fileTypeFromMap(propertyMap, runtimeException, row);
    } else if (clazz.isAssignableFrom(Instrument.class)) {
      CRUDRepository<?> repository = Arrays.stream(dependencyRepositories)
          .filter(r -> r instanceof FileTypeRepository)
          .findFirst().orElseThrow(
              () -> new RowConversionException("Instrument translation missing fileType repository", row)
          );
      object = (O) instrumentFromMap(propertyMap, (FileTypeRepository) repository, runtimeException, row);
    } else if (clazz.isAssignableFrom(Package.class)) {
      CRUDRepository<?> projectRepository = Arrays.stream(dependencyRepositories)
          .filter(r -> r instanceof ProjectRepository)
          .findFirst().orElseThrow(
              () -> new RowConversionException("Dataset translation missing project repository", row)
          );
      CRUDRepository<?> personRepository = Arrays.stream(dependencyRepositories)
          .filter(r -> r instanceof PersonRepository)
          .findFirst().orElseThrow(
              () -> new RowConversionException("Dataset translation missing person repository", row)
          );
      CRUDRepository<?> organizationRepository = Arrays.stream(dependencyRepositories)
          .filter(r -> r instanceof OrganizationRepository)
          .findFirst().orElseThrow(
              () -> new RowConversionException("Dataset translation missing organization repository", row)
          );
      CRUDRepository<?> platformRepository = Arrays.stream(dependencyRepositories)
          .filter(r -> r instanceof PlatformRepository)
          .findFirst().orElseThrow(
              () -> new RowConversionException("Dataset translation missing platform repository", row)
          );
      CRUDRepository<?> instrumentRepository = Arrays.stream(dependencyRepositories)
          .filter(r -> r instanceof InstrumentRepository)
          .findFirst().orElseThrow(
              () -> new RowConversionException("Dataset translation missing instrument repository", row)
          );
      CRUDRepository<?> sensorRepository = Arrays.stream(dependencyRepositories)
          .filter(r -> r instanceof SensorRepository)
          .findFirst().orElseThrow(
              () -> new RowConversionException("Dataset translation missing sensor repository", row)
          );
      CRUDRepository<?> detectionTypeRepository = Arrays.stream(dependencyRepositories)
          .filter(r -> r instanceof DetectionTypeRepository)
          .findFirst().orElseThrow(
              () -> new RowConversionException("Dataset translation missing sound source repository", row)
          );
      CRUDRepository<?> seaRepository = Arrays.stream(dependencyRepositories)
          .filter(r -> r instanceof SeaRepository)
          .findFirst().orElseThrow(
              () -> new RowConversionException("Dataset translation missing sea repository", row)
          );
      CRUDRepository<?> shipRepository = Arrays.stream(dependencyRepositories)
          .filter(r -> r instanceof ShipRepository)
          .findFirst().orElseThrow(
              () -> new RowConversionException("Dataset translation missing ship repository", row)
          );
      
      object = (O) packingJobFromMap(
          propertyMap,
          (ProjectRepository) projectRepository,
          (PersonRepository) personRepository,
          (OrganizationRepository) organizationRepository,
          (PlatformRepository) platformRepository,
          (InstrumentRepository) instrumentRepository,
          (SensorRepository) sensorRepository,
          (DetectionTypeRepository) detectionTypeRepository,
          (SeaRepository) seaRepository,
          (ShipRepository) shipRepository, 
          row,
          runtimeException
      );
    } else if (clazz.isAssignableFrom(Person.class)) {
      object = (O) objectWithNameFromMap(propertyMap, runtimeException, (p) -> Person.builder()
          .uuid(p.getUuid())
          .name(p.getName())
          .organization(getPropertyAsString(propertyMap, "organization"))
          .position(getPropertyAsString(propertyMap, "position"))
          .street(getPropertyAsString(propertyMap, "street"))
          .city(getPropertyAsString(propertyMap, "city"))
          .state(getPropertyAsString(propertyMap, "state"))
          .zip(getPropertyAsString(propertyMap, "zip"))
          .country(getPropertyAsString(propertyMap, "country"))
          .email(getPropertyAsString(propertyMap, "email"))
          .phone(getPropertyAsString(propertyMap, "phone"))
          .orcid(getPropertyAsString(propertyMap, "orcid"))
          .build(), row);
    } else if (clazz.isAssignableFrom(Organization.class)) {
      object = (O) objectWithNameFromMap(propertyMap, runtimeException, (o) -> Organization.builder()
          .uuid(o.getUuid())
          .name(o.getName())
          .street(getPropertyAsString(propertyMap, "street"))
          .city(getPropertyAsString(propertyMap, "city"))
          .state(getPropertyAsString(propertyMap, "state"))
          .zip(getPropertyAsString(propertyMap, "zip"))
          .country(getPropertyAsString(propertyMap, "country"))
          .email(getPropertyAsString(propertyMap, "email"))
          .phone(getPropertyAsString(propertyMap, "phone"))
          .build(), row);
    } else {
      throw new RowConversionException(String.format(
          "Translation not supported for %s", clazz.getSimpleName()
      ), row);
    }
    
    if (runtimeException.getSuppressed().length > 0) {
      RowConversionException exception = new RowConversionException("Translation failed", row);

      for (Throwable throwable : runtimeException.getSuppressed()) {
        exception.addSuppressed(throwable);
      }
      
      throw exception;
    }
    
    return object;
  }
  
  private static void validateTranslation(Map<String, ValueWithColumnNumber> propertyMap, Class<?> clazz, RuntimeException runtimeException, int row) {
    List<String> fieldNames = switch (clazz.getSimpleName()) {
      case "Sensor" -> {
        if (!propertyMap.containsKey("type")) {
          runtimeException.addSuppressed(new TranslatorValidationException(
              "Translator missing required field 'type'"
          ));
          yield null;
        }
        SensorType sensorType = getSensorType(propertyMap, runtimeException, row);
        if (sensorType == null) {
          yield null;
        }
        yield FieldNameFactory.getSensorDeclaredFields(sensorType);
      }
      case "Package" -> {
        boolean datasetTypeDefined = propertyMap.containsKey("datasetType");
        if (!datasetTypeDefined) {
          runtimeException.addSuppressed(new TranslatorValidationException(
              "Translator missing required field 'datasetType'"
          ));
        }
        boolean locationTypeDefined = propertyMap.containsKey("locationType");
        if (!locationTypeDefined) {
          runtimeException.addSuppressed(new TranslatorValidationException(
              "Translator missing required field 'locationType'"
          ));
        }
        
        if (!locationTypeDefined || !datasetTypeDefined) {
          yield null;
        }
        
        DatasetType datasetType = getPropertyAsDatasetType(propertyMap, "datasetType", runtimeException, row);
        LocationType locationType = getPropertyAsLocationType(propertyMap, "locationType", runtimeException, row);
        if (datasetType == null || locationType == null) {
          yield null;
        }
        
        yield FieldNameFactory.getDatasetDeclaredFields(datasetType, locationType);
      }
      case "Ship", "Sea", "Project", "Platform", "DetectionType", "FileType", "Person", "Organization", "Instrument" -> FieldNameFactory.getDefaultDeclaredFields(clazz);
      default -> null;
    };
    
    if (fieldNames == null) {
      return;
    }
    
    List<String> missingProperties = fieldNames.stream()
        .filter(n -> !propertyMap.containsKey(n))
        .toList();
    
    if (missingProperties.isEmpty()) {
      return;
    }
    
    missingProperties.forEach(
        p -> runtimeException.addSuppressed(new TranslatorValidationException(
            String.format("Translator missing required field '%s'", p)
        ))
    );
  }

  private static Dataset packingJobFromMap(
      Map<String, ValueWithColumnNumber> propertyMap,
      ProjectRepository projectRepository,
      PersonRepository personRepository,
      OrganizationRepository organizationRepository,
      PlatformRepository platformRepository,
      InstrumentRepository instrumentRepository,
      SensorRepository sensorRepository,
      DetectionTypeRepository detectionTypeRepository,
      SeaRepository seaRepository,
      ShipRepository shipRepository,
      int row,
      RuntimeException runtimeException
  ) {
    
    
    Path temperaturePath = getPropertyAsPath(propertyMap, "temperaturePath", runtimeException, row);
    Path documentsPath = getPropertyAsPath(propertyMap, "documentsPath", runtimeException, row);
    Path otherPath = getPropertyAsPath(propertyMap, "otherPath", runtimeException, row);
    Path navigationPath = getPropertyAsPath(propertyMap, "navigationPath", runtimeException, row);
    Path calibrationDocumentsPath = getPropertyAsPath(propertyMap, "calibrationDocumentsPath", runtimeException, row);
    Path sourcePath = getPropertyAsPath(propertyMap, "sourcePath", runtimeException, row);
    Path biologicalPath = getPropertyAsPath(propertyMap, "biologicalPath", runtimeException, row);
    
    String siteOrCruiseName = getPropertyAsString(propertyMap, "siteOrCruiseName");
    String deploymentId = getPropertyAsString(propertyMap, "deploymentId");
    LocalDate publicReleaseDate = getPropertyAsDate(propertyMap, "publicReleaseDate", runtimeException, row);
    List<Project> projects = getPropertyAsDelimitedResources(
        "projects",
        propertyMap,
        projectRepository,
        runtimeException,
        row
    );
    Person datasetPackager = getPropertyAsResource(
        "datasetPackager",
        getProperty(propertyMap, "datasetPackager"),
        personRepository,
        runtimeException,
        row
    );

    List<Person> scientists = getPropertyAsDelimitedResources(
        "scientists",
        propertyMap,
        personRepository,
        runtimeException,
        row
    );
    
    List<Organization> funders = getPropertyAsDelimitedResources(
        "funders",
        propertyMap,
        organizationRepository,
        runtimeException,
        row
    );
    List<Organization> sponsors = getPropertyAsDelimitedResources(
        "sponsors",
        propertyMap,
        organizationRepository,
        runtimeException,
        row
    );
    Platform platform = getPropertyAsResource(
        "platform",
        getProperty(propertyMap, "platform"),
        platformRepository,
        runtimeException,
        row
    );
    Instrument instrument = getPropertyAsResource(
        "instrument",
        getProperty(propertyMap, "instrument"),
        instrumentRepository,
        runtimeException,
        row
    );
    LocalDateTime startTime = getPropertyAsDateTime(propertyMap, "startTime", runtimeException, row);
    LocalDateTime endTime = getPropertyAsDateTime(propertyMap, "endTime", runtimeException, row);
    LocalDate preDeploymentCalibrationDate = getPropertyAsDate(propertyMap, "preDeploymentCalibrationDate", runtimeException, row);
    LocalDate postDeploymentCalibrationDate = getPropertyAsDate(propertyMap, "postDeploymentCalibrationDate", runtimeException, row);
    String calibrationDescription = getPropertyAsString(propertyMap, "calibrationDescription");
    String deploymentTitle = getPropertyAsString(propertyMap, "deploymentTitle");
    String deploymentPurpose = getPropertyAsString(propertyMap, "deploymentPurpose");
    String deploymentDescription = getPropertyAsString(propertyMap, "deploymentDescription");
    String alternateSiteName = getPropertyAsString(propertyMap, "alternateSiteName");
    String alternateDeploymentName = getPropertyAsString(propertyMap, "alternateDeploymentName");
    String softwareNames = getPropertyAsString(propertyMap, "softwareNames");
    String softwareVersions = getPropertyAsString(propertyMap, "softwareVersions");
    String softwareProtocolCitation = getPropertyAsString(propertyMap, "softwareProtocolCitation");
    String softwareDescription = getPropertyAsString(propertyMap, "softwareDescription");
    String softwareProcessingDescription = getPropertyAsString(propertyMap, "softwareProcessingDescription");
    String datasetTypePropertyName = "datasetType";
    ValueWithColumnNumber datasetTypeCell = getProperty(propertyMap, datasetTypePropertyName);
    DatasetType datasetType = getPropertyAsDatasetType(propertyMap, datasetTypePropertyName, runtimeException, row);
    String instrumentId = getPropertyAsString(propertyMap, "instrumentId");
    Float hydrophoneSensitivity = getPropertyAsFloat(propertyMap, "hydrophoneSensitivity", runtimeException, row);
    Float frequencyRange = getPropertyAsFloat(propertyMap, "frequencyRange", runtimeException, row);
    Float gain = getPropertyAsFloat(propertyMap, "gain", runtimeException, row);
    Person qualityAnalyst = getPropertyAsResource(
        "qualityAnalyst",
        getProperty(propertyMap, "qualityAnalyst"),
        personRepository,
        runtimeException,
        row
    );
    String qualityAnalysisObjectives = getPropertyAsString(propertyMap, "qualityAnalysisObjectives");
    String qualityAnalysisMethod = getPropertyAsString(propertyMap, "qualityAnalysisMethod");
    String qualityAssessmentDescription = getPropertyAsString(propertyMap, "qualityAssessmentDescription");
    LocalDateTime deploymentTime = getPropertyAsDateTime(propertyMap, "deploymentTime", runtimeException, row);
    LocalDateTime recoveryTime = getPropertyAsDateTime(propertyMap, "recoveryTime", runtimeException, row);
    String comments = getPropertyAsString(propertyMap, "comments");
    List<Sensor> sensors = getPropertyAsDelimitedResources("sensors", propertyMap, sensorRepository, runtimeException, row);
    List<DataQualityEntry> qualityEntries = qualityEntriesFromMap(propertyMap, runtimeException, row);
    DetectionType detectionType = getPropertyAsResource(
        "soundSource",
        getProperty(propertyMap, "soundSource"),
        detectionTypeRepository,
        runtimeException,
        row
    );
    Integer analysisTimeZone = getPropertyAsInteger(propertyMap, "analysisTimeZone", runtimeException, row);
    Integer analysisEffort = getPropertyAsInteger(propertyMap, "analysisEffort", runtimeException, row);
    Float sampleRate = getPropertyAsFloat(propertyMap, "sampleRate", runtimeException, row);
    Float minFrequency = getPropertyAsFloat(propertyMap, "minFrequency", runtimeException, row);
    Float maxFrequency = getPropertyAsFloat(propertyMap, "maxFrequency", runtimeException, row);
    LocalDateTime audioStartTime = getPropertyAsDateTime(propertyMap, "audioStartTime", runtimeException, row);
    LocalDateTime audioEndTime = getPropertyAsDateTime(propertyMap, "audioEndTime", runtimeException, row);
    Float modeledFrequency = getPropertyAsFloat(propertyMap, "modeledFrequency", runtimeException, row);
    
    LocationDetail locationDetail = locationDetailFromMap(propertyMap, runtimeException, seaRepository, shipRepository, row);

    Dataset dataset = null;
    if (DatasetType.SOUND_CLIPS.equals(datasetType)) {
      dataset = SoundClipsPackage.builder()
          .temperaturePath(temperaturePath)
          .documentsPath(documentsPath)
          .otherPath(otherPath)
          .navigationPath(navigationPath)
          .calibrationDocumentsPath(calibrationDocumentsPath)
          .sourcePath(sourcePath)
          .biologicalPath(biologicalPath)
          .siteOrCruiseName(siteOrCruiseName)
          .deploymentId(deploymentId)
          .datasetPackager(datasetPackager)
          .projects(projects)
          .publicReleaseDate(publicReleaseDate)
          .scientists(scientists)
          .funders(funders)
          .sponsors(sponsors)
          .platform(platform)
          .instrument(instrument)
          .startTime(startTime)
          .endTime(endTime)
          .preDeploymentCalibrationDate(preDeploymentCalibrationDate)
          .postDeploymentCalibrationDate(postDeploymentCalibrationDate)
          .calibrationDescription(calibrationDescription)
          .deploymentTitle(deploymentTitle)
          .deploymentPurpose(deploymentPurpose)
          .deploymentDescription(deploymentDescription)
          .alternateSiteName(alternateSiteName)
          .alternateDeploymentName(alternateDeploymentName)
          .softwareNames(softwareNames)
          .softwareVersions(softwareVersions)
          .softwareProtocolCitation(softwareProtocolCitation)
          .softwareDescription(softwareDescription)
          .softwareProcessingDescription(softwareProcessingDescription)
          .locationDetail(locationDetail)
          .build();
    } else if (DatasetType.AUDIO.equals(datasetType)) {
      dataset = AudioPackage.builder()
          .temperaturePath(temperaturePath)
          .documentsPath(documentsPath)
          .otherPath(otherPath)
          .navigationPath(navigationPath)
          .calibrationDocumentsPath(calibrationDocumentsPath)
          .sourcePath(sourcePath)
          .biologicalPath(biologicalPath)
          .siteOrCruiseName(siteOrCruiseName)
          .deploymentId(deploymentId)
          .datasetPackager(datasetPackager)
          .projects(projects)
          .publicReleaseDate(publicReleaseDate)
          .scientists(scientists)
          .sponsors(sponsors)
          .funders(funders)
          .platform(platform)
          .instrument(instrument)
          .instrumentId(instrumentId)
          .startTime(startTime)
          .endTime(endTime)
          .preDeploymentCalibrationDate(preDeploymentCalibrationDate)
          .postDeploymentCalibrationDate(postDeploymentCalibrationDate)
          .calibrationDescription(calibrationDescription)
          .hydrophoneSensitivity(hydrophoneSensitivity)
          .frequencyRange(frequencyRange)
          .gain(gain)
          .deploymentTitle(deploymentTitle)
          .deploymentPurpose(deploymentPurpose)
          .deploymentDescription(deploymentDescription)
          .alternateSiteName(alternateSiteName)
          .alternateDeploymentName(alternateDeploymentName)
          .qualityAnalyst(qualityAnalyst)
          .qualityAnalysisObjectives(qualityAnalysisObjectives)
          .qualityAnalysisMethod(qualityAnalysisMethod)
          .qualityAssessmentDescription(qualityAssessmentDescription)
          .qualityEntries(qualityEntries)
          .deploymentTime(deploymentTime)
          .recoveryTime(recoveryTime)
          .comments(comments)
          .sensors(sensors)
          .channels(channelsFromMap(propertyMap, sensorRepository, runtimeException, row))
          .locationDetail(locationDetail)
          .build();
    } else if (DatasetType.CPOD.equals(datasetType)) {
      dataset = CPODPackage.builder()
          .temperaturePath(temperaturePath)
          .documentsPath(documentsPath)
          .otherPath(otherPath)
          .navigationPath(navigationPath)
          .calibrationDocumentsPath(calibrationDocumentsPath)
          .sourcePath(sourcePath)
          .biologicalPath(biologicalPath)
          .siteOrCruiseName(siteOrCruiseName)
          .deploymentId(deploymentId)
          .datasetPackager(datasetPackager)
          .projects(projects)
          .publicReleaseDate(publicReleaseDate)
          .scientists(scientists)
          .sponsors(sponsors)
          .funders(funders)
          .platform(platform)
          .instrument(instrument)
          .instrumentId(instrumentId)
          .startTime(startTime)
          .endTime(endTime)
          .preDeploymentCalibrationDate(preDeploymentCalibrationDate)
          .postDeploymentCalibrationDate(postDeploymentCalibrationDate)
          .calibrationDescription(calibrationDescription)
          .hydrophoneSensitivity(hydrophoneSensitivity)
          .frequencyRange(frequencyRange)
          .gain(gain)
          .deploymentTitle(deploymentTitle)
          .deploymentPurpose(deploymentPurpose)
          .deploymentDescription(deploymentDescription)
          .alternateSiteName(alternateSiteName)
          .alternateDeploymentName(alternateDeploymentName)
          .qualityAnalyst(qualityAnalyst)
          .qualityAnalysisObjectives(qualityAnalysisObjectives)
          .qualityAnalysisMethod(qualityAnalysisMethod)
          .qualityAssessmentDescription(qualityAssessmentDescription)
          .qualityEntries(qualityEntries)
          .deploymentTime(deploymentTime)
          .recoveryTime(recoveryTime)
          .comments(comments)
          .sensors(sensors)
          .channels(channelsFromMap(propertyMap, sensorRepository, runtimeException, row))
          .locationDetail(locationDetail)
          .build();
    } else if (DatasetType.DETECTIONS.equals(datasetType)) {
      dataset = DetectionsPackage.builder()
          .temperaturePath(temperaturePath)
          .documentsPath(documentsPath)
          .otherPath(otherPath)
          .navigationPath(navigationPath)
          .calibrationDocumentsPath(calibrationDocumentsPath)
          .sourcePath(sourcePath)
          .biologicalPath(biologicalPath)
          .siteOrCruiseName(siteOrCruiseName)
          .deploymentId(deploymentId)
          .datasetPackager(datasetPackager)
          .projects(projects)
          .publicReleaseDate(publicReleaseDate)
          .scientists(scientists)
          .sponsors(sponsors)
          .funders(funders)
          .platform(platform)
          .instrument(instrument)
          .startTime(startTime)
          .endTime(endTime)
          .soundSource(detectionType)
          .qualityAnalyst(qualityAnalyst)
          .qualityAnalysisObjectives(qualityAnalysisObjectives)
          .qualityAnalysisMethod(qualityAnalysisMethod)
          .qualityAssessmentDescription(qualityAssessmentDescription)
          .qualityEntries(qualityEntries)
          .softwareNames(softwareNames)
          .softwareVersions(softwareVersions)
          .softwareProtocolCitation(softwareProtocolCitation)
          .softwareDescription(softwareDescription)
          .softwareProcessingDescription(softwareProcessingDescription)
          .analysisTimeZone(analysisTimeZone)
          .analysisEffort(analysisEffort)
          .sampleRate(sampleRate)
          .minFrequency(minFrequency)
          .maxFrequency(maxFrequency)
          .preDeploymentCalibrationDate(preDeploymentCalibrationDate)
          .postDeploymentCalibrationDate(postDeploymentCalibrationDate)
          .calibrationDescription(calibrationDescription)
          .deploymentTitle(deploymentTitle)
          .deploymentPurpose(deploymentPurpose)
          .deploymentDescription(deploymentDescription)
          .alternateSiteName(alternateSiteName)
          .alternateDeploymentName(alternateDeploymentName)
          .locationDetail(locationDetail)
          .build();
    } else if (DatasetType.SOUND_LEVEL_METRICS.equals(datasetType)) {
      dataset = SoundLevelMetricsPackage.builder()
          .temperaturePath(temperaturePath)
          .documentsPath(documentsPath)
          .otherPath(otherPath)
          .navigationPath(navigationPath)
          .calibrationDocumentsPath(calibrationDocumentsPath)
          .sourcePath(sourcePath)
          .biologicalPath(biologicalPath)
          .siteOrCruiseName(siteOrCruiseName)
          .deploymentId(deploymentId)
          .datasetPackager(datasetPackager)
          .projects(projects)
          .publicReleaseDate(publicReleaseDate)
          .scientists(scientists)
          .sponsors(sponsors)
          .funders(funders)
          .platform(platform)
          .instrument(instrument)
          .startTime(startTime)
          .endTime(endTime)
          .preDeploymentCalibrationDate(preDeploymentCalibrationDate)
          .postDeploymentCalibrationDate(postDeploymentCalibrationDate)
          .calibrationDescription(calibrationDescription)
          .deploymentTitle(deploymentTitle)
          .deploymentPurpose(deploymentPurpose)
          .deploymentDescription(deploymentDescription)
          .alternateSiteName(alternateSiteName)
          .alternateDeploymentName(alternateDeploymentName)
          .audioStartTime(audioStartTime)
          .audioEndTime(audioEndTime)
          .qualityAnalyst(qualityAnalyst)
          .qualityAnalysisObjectives(qualityAnalysisObjectives)
          .qualityAnalysisMethod(qualityAnalysisMethod)
          .qualityAssessmentDescription(qualityAssessmentDescription)
          .qualityEntries(qualityEntries)
          .analysisTimeZone(analysisTimeZone)
          .analysisEffort(analysisEffort)
          .sampleRate(sampleRate)
          .minFrequency(minFrequency)
          .maxFrequency(maxFrequency)
          .softwareNames(softwareNames)
          .softwareVersions(softwareVersions)
          .softwareProtocolCitation(softwareProtocolCitation)
          .softwareDescription(softwareDescription)
          .softwareProcessingDescription(softwareProcessingDescription)
          .locationDetail(locationDetail)
          .build();
    } else if (DatasetType.SOUND_PROPAGATION_MODELS.equals(datasetType)) {
      dataset = SoundPropagationModelsPackage.builder()
          .temperaturePath(temperaturePath)
          .documentsPath(documentsPath)
          .otherPath(otherPath)
          .navigationPath(navigationPath)
          .calibrationDocumentsPath(calibrationDocumentsPath)
          .sourcePath(sourcePath)
          .biologicalPath(biologicalPath)
          .siteOrCruiseName(siteOrCruiseName)
          .deploymentId(deploymentId)
          .datasetPackager(datasetPackager)
          .projects(projects)
          .publicReleaseDate(publicReleaseDate)
          .scientists(scientists)
          .sponsors(sponsors)
          .funders(funders)
          .platform(platform)
          .instrument(instrument)
          .startTime(startTime)
          .endTime(endTime)
          .preDeploymentCalibrationDate(preDeploymentCalibrationDate)
          .postDeploymentCalibrationDate(postDeploymentCalibrationDate)
          .calibrationDescription(calibrationDescription)
          .deploymentTitle(deploymentTitle)
          .deploymentPurpose(deploymentPurpose)
          .deploymentDescription(deploymentDescription)
          .alternateSiteName(alternateSiteName)
          .alternateDeploymentName(alternateDeploymentName)
          .audioStartTime(audioStartTime)
          .audioEndTime(audioEndTime)
          .modeledFrequency(modeledFrequency)
          .softwareNames(softwareNames)
          .softwareVersions(softwareVersions)
          .softwareProtocolCitation(softwareProtocolCitation)
          .softwareDescription(softwareDescription)
          .softwareProcessingDescription(softwareProcessingDescription)
          .locationDetail(locationDetail)
          .build();
    } else {
      runtimeException.addSuppressed(new FieldException(
          "datasetType",
          String.format(
              "Translation not supported for %s datasets. Supported values: %s",
              datasetType,
              Arrays.stream(DatasetType.values())
                  .map(DatasetType::getName)
                  .toList()
          ),
          datasetTypeCell.column(),
          row
      ));
    }

    return dataset;
  }
  
  private static List<DataQualityEntry> qualityEntriesFromMap(Map<String, ValueWithColumnNumber> propertyMap, RuntimeException runtimeException, int row) {
    
    Map<Integer, List<Entry<String, ValueWithColumnNumber>>> qcMap = propertyMap.entrySet().stream()
        .filter(e -> e.getKey().matches("^qualityEntries\\[\\d]\\.(?:startTime|endTime|minFrequency|maxFrequency|qualityLevel|comments)"))
        .collect(Collectors.groupingBy(
            e -> Integer.parseInt(String.valueOf(e.getKey().split("qualityEntries\\[")[1].charAt(0)))
        ));
    
    List<DataQualityEntry> dataQualityEntries = new ArrayList<>(0);
    
    qcMap.forEach((key, value) -> {
      Map<String, ValueWithColumnNumber> map = Map.ofEntries(value.toArray(Entry[]::new));
      
      dataQualityEntries.add(key, DataQualityEntry.builder()
              .startTime(getPropertyAsDateTime(map, String.format(
                  "qualityEntries[%s].startTime", key
              ), runtimeException, row))
              .endTime(getPropertyAsDateTime(map, String.format(
                  "qualityEntries[%s].endTime", key
              ), runtimeException, row))
              .minFrequency(getPropertyAsFloat(map, String.format(
                  "qualityEntries[%s].minFrequency", key
              ), runtimeException, row))
              .maxFrequency(getPropertyAsFloat(map, String.format(
                  "qualityEntries[%s].maxFrequency", key
              ), runtimeException, row))
              .qualityLevel(getQualityLevel(map, String.format(
                  "qualityEntries[%s].qualityLevel", key
              ), runtimeException, row))
              .comments(getPropertyAsString(map, String.format(
                  "qualityEntries[%s].comments", key
              )))
          .build());
    });
    
    return dataQualityEntries;
  }
  
  private static FileType fileTypeFromMap(Map<String, ValueWithColumnNumber> propertyMap, RuntimeException runtimeException, int row) {
    UUID uuid = getPropertyAsUUID(propertyMap, "uuid", runtimeException, row);

    String type = getPropertyAsString(propertyMap, "type");
    String comment = getPropertyAsString(propertyMap, "comment");
    
    return FileType.builder()
        .uuid(uuid)
        .type(type)
        .comment(comment)
        .build();
  }
  
  private static Instrument instrumentFromMap(Map<String, ValueWithColumnNumber> propertyMap, FileTypeRepository fileTypeRepository, RuntimeException runtimeException, int row) {
    UUID uuid = getPropertyAsUUID(propertyMap, "uuid", runtimeException, row);

    String name = getPropertyAsString(propertyMap, "name");

    List<FileType> fileTypes = getPropertyAsDelimitedResources(
        "fileTypes",
        propertyMap,
        fileTypeRepository,
        runtimeException,
        row
    );
    
    return Instrument.builder()
        .uuid(uuid)
        .name(name)
        .fileTypes(fileTypes)
        .build();
  }
  
  private static Sensor sensorFromMap(Map<String, ValueWithColumnNumber> propertyMap, RuntimeException runtimeException, int row) {
    UUID uuid = getPropertyAsUUID(propertyMap, "uuid", runtimeException, row);
    
    String name = getPropertyAsString(propertyMap, "name");
    String description = getPropertyAsString(propertyMap, "description");

    Float x = getPropertyAsFloat(propertyMap, "position.x", runtimeException, row);
    Float y = getPropertyAsFloat(propertyMap, "position.y", runtimeException, row);
    Float z = getPropertyAsFloat(propertyMap, "position.z", runtimeException, row);
    
    Position position = Position.builder()
        .x(x)
        .y(y)
        .z(z)
        .build();
    
    SensorType type = getSensorType(propertyMap, runtimeException, row);
    
    Sensor sensor = null;
    if (SensorType.depth.equals(type)) {
      sensor = DepthSensor.builder()
          .uuid(uuid)
          .name(name)
          .position(position)
          .description(description)
          .build();
    } else if (SensorType.audio.equals(type)) {
      sensor = AudioSensor.builder()
          .uuid(uuid)
          .name(name)
          .position(position)
          .description(description)
          .hydrophoneId(getPropertyAsString(propertyMap, "hydrophoneId"))
          .preampId(getPropertyAsString(propertyMap, "preampId"))
          .build();
    } else if (SensorType.other.equals(type)) {
      sensor = OtherSensor.builder()
          .uuid(uuid)
          .name(name)
          .position(position)
          .description(description)
          .properties(getPropertyAsString(propertyMap, "properties"))
          .sensorType(getPropertyAsString(propertyMap, "sensorType"))
          .build();
    }
    
    return sensor;
  }

  private static <O extends ObjectWithName> O objectWithNameFromMap(Map<String, ValueWithColumnNumber> propertyMap, RuntimeException runtimeException, Function<ObjectWithName, O> convertFn, int row) {
    return convertFn.apply(new ObjectWithName() {
      @Override
      public String getName() {
        return getPropertyAsString(propertyMap, "name");
      }

      @Override
      public UUID getUuid() {
        return getPropertyAsUUID(propertyMap, "uuid", runtimeException, row);
      }
    });
  }
  
  private static UUID getPropertyAsUUID(Map<String, ValueWithColumnNumber> map, String property, RuntimeException runtimeException, int row) {
    ValueWithColumnNumber uuidCell = getProperty(map, property);
    if (uuidCell.value().isEmpty()) {
      return null;
    }
    
    String uuidString = uuidCell.value().get();
    
    if (StringUtils.isBlank(uuidString)) {
      return null;
    }
    
    try {
      return UUID.fromString(uuidString);
    } catch (IllegalArgumentException e) {
      runtimeException.addSuppressed(
          new FieldException("uuid", "invalid uuid format", uuidCell.column(), row)
      );
      return null;
    }
  }
  
  private static SensorType getSensorType(Map<String, ValueWithColumnNumber> map, RuntimeException runtimeException, int row) {
    ValueWithColumnNumber typeCell = getProperty(map, "type");
    if (typeCell.value().isEmpty()) {
      return null;
    }
    
    try {
      return SensorType.valueOf(typeCell.value().get());
    } catch (Exception e) {
      runtimeException.addSuppressed(
          new FieldException("type", String.format(
              "Invalid sensor type. Was not one of %s", Arrays.stream(SensorType.values())
                  .map(Enum::name)
                  .collect(Collectors.joining(", "))
          ), typeCell.column(), row)
      );
      
      return null;
    }
  }
  
  private static QualityLevel getQualityLevel(Map<String, ValueWithColumnNumber> map, String property, RuntimeException runtimeException, int row) {
    ValueWithColumnNumber qualityLevelCell = getProperty(map, property);
    if (qualityLevelCell.value().isEmpty()) {
      return null;
    }
    
    String qualityLevelString = qualityLevelCell.value().get();
    if (StringUtils.isBlank(qualityLevelString)) {
      return null;
    }
    
    try {
      return QualityLevel.fromName(qualityLevelString);
    } catch (Exception e) {
      runtimeException.addSuppressed(
          new FieldException(
              property,
              String.format(
                  "Invalid quality level. Was not one of %s", Arrays.stream(QualityLevel.values())
                      .map(QualityLevel::getName)
                      .collect(Collectors.joining(", "))
              ),
              qualityLevelCell.column(),
              row
          )
      );
      return null;
    }
  }
  
  private static String getPropertyAsString(Map<String, ValueWithColumnNumber> map, String property) {
    return getProperty(map, property).value().orElse(null);
  }
  
  private static ValueWithColumnNumber getProperty(Map<String, ValueWithColumnNumber> map, String property) {
    ValueWithColumnNumber valueWithColumnNumber = map.get(property);
    if (valueWithColumnNumber == null) {
      return new ValueWithColumnNumber(
          Optional.empty(),
          null
      );
    }
    
    Optional<String> value = valueWithColumnNumber.value();
    
    String stringValue = value.orElse(null);
    if (stringValue != null && StringUtils.isBlank(stringValue)) {
      return new ValueWithColumnNumber(
          Optional.empty(),
          valueWithColumnNumber.column()
      );
    }
    
    if (stringValue == null) {
      return new ValueWithColumnNumber(
          Optional.empty(),
          valueWithColumnNumber.column()
      );
    }

    return new ValueWithColumnNumber(
        Optional.of(stringValue.trim()),
        valueWithColumnNumber.column()
    );
  }
  
  private static Float getPropertyAsFloat(Map<String, ValueWithColumnNumber> map, String property, RuntimeException runtimeException, int row) {
    ValueWithColumnNumber floatCell = getProperty(map, property);
    
    if (floatCell.value().isEmpty()) {
      return null;
    }
    
    try {
      return Float.parseFloat(floatCell.value().get());
    } catch (NumberFormatException e) {
      runtimeException.addSuppressed(
          new FieldException(property, "invalid decimal format", floatCell.column(), row)
      );
      return null;
    }
  }
  
  private static Integer getPropertyAsInteger(Map<String, ValueWithColumnNumber> map, String property, RuntimeException runtimeException, int row) {
    ValueWithColumnNumber integerCell = getProperty(map, property);

    if (integerCell.value().isEmpty()) {
      return null;
    }

    try {
      return Integer.parseInt(integerCell.value().get());
    } catch (NumberFormatException e) {
      runtimeException.addSuppressed(
          new FieldException(property, "invalid integer format", integerCell.column(), row)
      );
      return null;
    }
  }
  
  private static LocalDate getPropertyAsDate(Map<String, ValueWithColumnNumber> map, String property, RuntimeException runtimeException, int row) {
    ValueWithColumnNumber dateCell = getProperty(map, property);
    
    if (dateCell.value().isEmpty()) {
      return null;
    }
    
    try {
      return LocalDate.parse(dateCell.value().get());
    } catch (DateTimeParseException e) {
      runtimeException.addSuppressed(
          new FieldException(property, "invalid date format", dateCell.column(), row)
      );
      return null;
    }
  }

  private static DatasetType getPropertyAsDatasetType(Map<String, ValueWithColumnNumber> map, String property, RuntimeException runtimeException, int row) {
    ValueWithColumnNumber datasetTypeCell = getProperty(map, property);

    if (datasetTypeCell.value().isEmpty()) {
      return null;
    }

    try {
      return DatasetType.fromName(datasetTypeCell.value().get());
    } catch (IllegalArgumentException e) {
      runtimeException.addSuppressed(
          new FieldException(property, String.format(
              "Invalid dataset type. Was not one of %s", Arrays.stream(DatasetType.values())
                  .map(DatasetType::getName)
                  .collect(Collectors.joining(", "))
          ), datasetTypeCell.column(), row)
      );
      return null;
    }
  }

  private static LocationType getPropertyAsLocationType(Map<String, ValueWithColumnNumber> map, String property, RuntimeException runtimeException, int row) {
    ValueWithColumnNumber locationTypeCell = getProperty(map, property);

    if (locationTypeCell.value().isEmpty()) {
      return null;
    }

    try {
      return LocationType.fromName(locationTypeCell.value().get());
    } catch (IllegalArgumentException e) {
      runtimeException.addSuppressed(
          new FieldException(property, String.format(
              "Invalid location type. Was not one of %s", Arrays.stream(LocationType.values())
                  .map(LocationType::getName)
                  .collect(Collectors.joining(", "))
          ), locationTypeCell.column(), row)
      );
      return null;
    }
  }

  private static LocalDateTime getPropertyAsDateTime(Map<String, ValueWithColumnNumber> propertyMap, String property, RuntimeException runtimeException, int row) {
    ValueWithColumnNumber dateTimeCell = getProperty(propertyMap, property);
    
    if (dateTimeCell.value().isEmpty()) {
      return null;
    }
    
    try {
      return LocalDateTime.parse(dateTimeCell.value().get());
    } catch (DateTimeParseException e) {
      runtimeException.addSuppressed(
          new FieldException(property, "invalid date time format", dateTimeCell.column(), row)
      );
      return null;
    }
  }
  
  private static <O extends ObjectWithUniqueField> List<O> getPropertyAsDelimitedResources(String propertyName, Map<String, ValueWithColumnNumber> propertyMap, CRUDRepository<O> repository, RuntimeException runtimeException, int row) {
    ValueWithColumnNumber objectCell = getProperty(propertyMap, propertyName);
    
    if (objectCell.value().isPresent()) {
      return Arrays.stream(objectCell.value().get().split(";"))
          .map(uniqueField -> getPropertyAsResource(propertyName, new ValueWithColumnNumber(Optional.of(uniqueField), objectCell.column()), repository, runtimeException, row))
          .toList();
    } else {
      return Collections.emptyList();
    }
  }
  
  private static <O extends ObjectWithUniqueField> O getPropertyAsResource(String propertyName, ValueWithColumnNumber uniqueFieldCell, CRUDRepository<O> repository, RuntimeException runtimeException, int row) {
    if (uniqueFieldCell.value().isEmpty()) {
      return null;
    }
    
    String uniqueField = uniqueFieldCell.value().get();

    try {
      return repository.getByUniqueField(uniqueField);
    } catch (DatastoreException | NotFoundException e) {
      runtimeException.addSuppressed(new FieldException(
          propertyName, e.getMessage(), uniqueFieldCell.column(), row
      ));
      return null;
    }
  }
  
  private static Path getPropertyAsPath(Map<String, ValueWithColumnNumber> propertyMap, String propertyName, RuntimeException runtimeException, int row) {
    ValueWithColumnNumber pathCell = getProperty(propertyMap, propertyName);
    
    if (pathCell.value().isEmpty()) {
      return null;
    }
    
    try {
      return Path.of(pathCell.value().get());
    } catch (InvalidPathException e) {
      runtimeException.addSuppressed(
          new FieldException(propertyName, "Invalid path format", pathCell.column(), row)
      );
      return null;
    }
  }
  
  private static LocationDetail locationDetailFromMap(Map<String, ValueWithColumnNumber> propertyMap, RuntimeException runtimeException, SeaRepository seaRepository, ShipRepository shipRepository, int row) {
    ValueWithColumnNumber locationTypeCell = getProperty(propertyMap, "locationType");
    if (locationTypeCell.value().isEmpty()) {
      return null;
    }
    
    String locationType = locationTypeCell.value().get();
    
    if (LocationType.STATIONARY_MARINE.getName().equals(locationType)) {
      return StationaryMarineLocation.builder()
          .seaArea(getPropertyAsResource("seaArea", getProperty(propertyMap, "seaArea"), seaRepository, runtimeException, row))
          .deploymentLocation(MarineInstrumentLocation.builder()
              .latitude(getPropertyAsFloat(propertyMap, "deploymentLocation.latitude", runtimeException, row))
              .longitude(getPropertyAsFloat(propertyMap, "deploymentLocation.longitude", runtimeException, row))
              .seaFloorDepth(getPropertyAsFloat(propertyMap, "deploymentLocation.seaFloorDepth", runtimeException, row))
              .instrumentDepth(getPropertyAsFloat(propertyMap, "deploymentLocation.instrumentDepth", runtimeException, row))
              .build())
          .recoveryLocation(MarineInstrumentLocation.builder()
              .latitude(getPropertyAsFloat(propertyMap, "recoveryLocation.latitude", runtimeException, row))
              .longitude(getPropertyAsFloat(propertyMap, "recoveryLocation.longitude", runtimeException, row))
              .seaFloorDepth(getPropertyAsFloat(propertyMap, "recoveryLocation.seaFloorDepth", runtimeException, row))
              .instrumentDepth(getPropertyAsFloat(propertyMap, "recoveryLocation.instrumentDepth", runtimeException, row))
              .build())
          .build();
    } else if (LocationType.MOBILE_MARINE.getName().equals(locationType)) {
      return MobileMarineLocation.builder()
          .seaArea(getPropertyAsResource("seaArea", getProperty(propertyMap, "seaArea"), seaRepository, runtimeException, row))
          .vessel(getPropertyAsResource("vessel", getProperty(propertyMap, "vessel"), shipRepository, runtimeException, row))
          .locationDerivationDescription(getPropertyAsString(propertyMap, "locationDerivationDescription"))
          .build();
    } else if (LocationType.STATIONARY_TERRESTRIAL.getName().equals(locationType)) {
      return StationaryTerrestrialLocation.builder()
          .latitude(getPropertyAsFloat(propertyMap, "latitude", runtimeException, row))
          .longitude(getPropertyAsFloat(propertyMap, "longitude", runtimeException, row))
          .surfaceElevation(getPropertyAsFloat(propertyMap, "surfaceElevation", runtimeException, row))
          .instrumentElevation(getPropertyAsFloat(propertyMap, "instrumentElevation", runtimeException, row))
          .build();
    } else if (LocationType.MULTIPOINT_STATIONARY_MARINE.getName().equals(locationType)) {
      List<MarineInstrumentLocation> marineInstrumentLocations = new ArrayList<>(0);
      
      Map<Integer, List<Entry<String, ValueWithColumnNumber>>> locationMap = propertyMap.entrySet().stream()
          .filter(e -> e.getKey().matches("^locations\\[\\d]\\.(?:latitude|longitude|seaFloorDepth|instrumentDepth)"))
          .collect(Collectors.groupingBy(
              e -> Integer.parseInt(String.valueOf(e.getKey().split("locations\\[")[1].charAt(0)))
          ));
      
      locationMap.forEach((key, value) -> {
        Map<String, ValueWithColumnNumber> map = Map.ofEntries(value.toArray(Entry[]::new));
        marineInstrumentLocations.add(key, MarineInstrumentLocation.builder()
            .latitude(getPropertyAsFloat(map, String.format(
                "locations[%s].latitude", key 
            ), runtimeException, row))
            .longitude(getPropertyAsFloat(map, String.format(
                "locations[%s].longitude", key 
            ), runtimeException, row))
            .seaFloorDepth(getPropertyAsFloat(map, String.format(
                "locations[%s].seaFloorDepth", key 
            ), runtimeException, row))
            .instrumentDepth(getPropertyAsFloat(map, String.format(
                "locations[%s].instrumentDepth", key 
            ), runtimeException, row))
            .build());
      });
      
      return MultiPointStationaryMarineLocation.builder()
          .seaArea(getPropertyAsResource("seaArea", getProperty(propertyMap, "seaArea"), seaRepository, runtimeException, row))
          .locations(marineInstrumentLocations)
          .build();
    } else {
      runtimeException.addSuppressed(new FieldException(
          "type", String.format(
          "Invalid location type. Was not one of %s", Arrays.stream(LocationType.values())
              .map(LocationType::getName)
              .collect(Collectors.joining(", "))
          ),
          locationTypeCell.column(),
          row
      ));
      return null;
    }
  }
  
  private static List<Channel> channelsFromMap(Map<String, ValueWithColumnNumber> propertyMap, SensorRepository sensorRepository, RuntimeException runtimeException, int row) {
    List<Channel> channels = new ArrayList<>(0);
    
    Map<Integer, List<Entry<String, ValueWithColumnNumber>>> channelsMap = propertyMap.entrySet().stream()
        .filter(e -> e.getKey().matches("^channels\\[\\d].*"))
        .collect(Collectors.groupingBy(
            e -> Integer.parseInt(String.valueOf(
                e.getKey().split("channels\\[")[1].charAt(0)
            ))
        ));
    
    channelsMap.forEach((key, value) -> {
      Map<String, ValueWithColumnNumber> map = Map.ofEntries(value.toArray(Entry[]::new));
      
      String propertyPrefix = String.format("channels[%s]", key);
      
      channels.add(key, Channel.builder()
              .sensor(getPropertyAsResource(
                  propertyPrefix + ".sensor", getProperty(map, propertyPrefix + ".sensor"), sensorRepository, runtimeException, row 
              ))
              .startTime(getPropertyAsDateTime(map, propertyPrefix + ".startTime", runtimeException, row))
              .endTime(getPropertyAsDateTime(map, propertyPrefix + ".endTime", runtimeException, row))
              .sampleRates(sampleRatesFromMap(map, key, runtimeException, row))
              .dutyCycles(dutyCyclesFromMap(map, key, runtimeException, row))
              .gains(gainsFromMap(map, key, runtimeException, row))
          .build());
    });
    
    return channels;
  }
  
  private static List<SampleRate> sampleRatesFromMap(Map<String, ValueWithColumnNumber> propertyMap, Integer channelIndex, RuntimeException runtimeException, int row) {
    List<SampleRate> sampleRates = new ArrayList<>(0);
    
    Map<Integer, List<Entry<String, ValueWithColumnNumber>>> sampleRatesMap = propertyMap.entrySet().stream()
        .filter(e -> e.getKey().matches("^channels\\[\\d].sampleRates\\[\\d].*"))
        .collect(Collectors.groupingBy(
            e -> Integer.parseInt(String.valueOf(
                e.getKey().split("sampleRates\\[")[1].charAt(0)
            ))
        ));
    
    sampleRatesMap.forEach((key, value) -> {
      Map<String, ValueWithColumnNumber> map = Map.ofEntries(value.toArray(Entry[]::new));
      
      String propertyPrefix = String.format(
          "channels[%s].sampleRates[%s]", channelIndex, key
      );
      
      sampleRates.add(key, SampleRate.builder()
              .startTime(getPropertyAsDateTime(map, propertyPrefix + ".startTime", runtimeException, row))
              .endTime(getPropertyAsDateTime(map, propertyPrefix + ".endTime", runtimeException, row))
              .sampleRate(getPropertyAsFloat(map, propertyPrefix + ".sampleRate", runtimeException, row))
              .sampleBits(getPropertyAsInteger(map, propertyPrefix + ".sampleBits", runtimeException, row))
          .build());
    });
    
    return sampleRates;
  }
  
  private static List<DutyCycle> dutyCyclesFromMap(Map<String, ValueWithColumnNumber> propertyMap, Integer channelIndex, RuntimeException runtimeException, int row) {
    List<DutyCycle> dutyCycles = new ArrayList<>(0);
    
    Map<Integer, List<Entry<String, ValueWithColumnNumber>>> dutyCyclesMap = propertyMap.entrySet().stream()
        .filter(e -> e.getKey().matches("^channels\\[\\d].dutyCycles\\[\\d].*"))
        .collect(Collectors.groupingBy(
            e -> Integer.parseInt(String.valueOf(
                e.getKey().split("dutyCycles\\[")[1].charAt(0)
            ))
        ));
    
    dutyCyclesMap.forEach((key, value) -> {
      Map<String, ValueWithColumnNumber> map = Map.ofEntries(value.toArray(Entry[]::new));
      
      String propertyPrefix = String.format(
        "channels[%s].dutyCycles[%s]", channelIndex, key  
      );
      
      dutyCycles.add(DutyCycle.builder()
              .startTime(getPropertyAsDateTime(map, propertyPrefix + ".startTime", runtimeException, row))
              .endTime(getPropertyAsDateTime(map, propertyPrefix + ".endTime", runtimeException, row))
              .interval(getPropertyAsFloat(map, propertyPrefix + ".interval", runtimeException, row))
              .duration(getPropertyAsFloat(map, propertyPrefix + ".duration", runtimeException, row))
          .build());
    });
    
    return dutyCycles;
  }
  
  private static List<Gain> gainsFromMap(Map<String, ValueWithColumnNumber> propertyMap, Integer channelIndex, RuntimeException runtimeException, int row) {
    List<Gain> gains = new ArrayList<>(0);
    
    Map<Integer, List<Entry<String, ValueWithColumnNumber>>> gainsMap = propertyMap.entrySet().stream()
        .filter(e -> e.getKey().matches("^channels\\[\\d].gains\\[\\d].*"))
        .collect(Collectors.groupingBy(
            e -> Integer.parseInt(String.valueOf(
                e.getKey().split("gains\\[")[1].charAt(0)
            ))
        ));
    
    gainsMap.forEach((key, value) -> {
      Map<String, ValueWithColumnNumber> map = Map.ofEntries(value.toArray(Entry[]::new));
      
      String propertyPrefix = String.format(
          "channels[%s].gains[%s]", channelIndex, key
      );
      
      gains.add(Gain.builder()
              .startTime(getPropertyAsDateTime(map, propertyPrefix + ".startTime", runtimeException, row))
              .endTime(getPropertyAsDateTime(map, propertyPrefix + ".endTime", runtimeException, row))
              .gain(getPropertyAsFloat(map, propertyPrefix + ".gain", runtimeException, row))
          .build());
    });
    
    return gains;
  }

}
