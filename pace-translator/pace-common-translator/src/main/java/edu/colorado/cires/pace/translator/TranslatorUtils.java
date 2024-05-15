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
import edu.colorado.cires.pace.data.object.TabularTranslationField;
import edu.colorado.cires.pace.data.object.TabularTranslator;
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
import java.lang.reflect.Field;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

final class TranslatorUtils {

  public static <O> O convertMapToObject(Map<String, Optional<String>> propertyMap, Class<O> clazz, int row, CRUDRepository<?>... dependencyRepositories)
      throws RowConversionException {
    RuntimeException runtimeException = new RuntimeException();
    
    O object;
    if (clazz.isAssignableFrom(Ship.class)) {
      object = (O) objectWithNameFromMap(propertyMap, runtimeException, (o) -> Ship.builder()
          .uuid(o.getUuid())
          .name(o.getName())
          .build());
    } else if (clazz.isAssignableFrom(Sea.class)) {
      object = (O) objectWithNameFromMap(propertyMap, runtimeException, (o) -> Sea.builder()
          .uuid(o.getUuid())
          .name(o.getName())
          .build());
    } else if (clazz.isAssignableFrom(Project.class)) {
      object = (O) objectWithNameFromMap(propertyMap, runtimeException, (o) -> Project.builder()
          .uuid(o.getUuid())
          .name(o.getName())
          .build());
    } else if (clazz.isAssignableFrom(Platform.class)) {
      object = (O) objectWithNameFromMap(propertyMap, runtimeException, (o) -> Platform.builder()
          .uuid(o.getUuid())
          .name(o.getName())
          .build());
    } else if (clazz.isAssignableFrom(Sensor.class)) {
      object = (O) sensorFromMap(propertyMap, runtimeException);
    } else if (clazz.isAssignableFrom(DetectionType.class)) {
      object = (O) DetectionType.builder()
          .uuid(uuidFromString(getProperty(propertyMap, "uuid"), runtimeException))
          .source(getProperty(propertyMap, "source"))
          .scienceName(getProperty(propertyMap, "scienceName"))
          .build();
    } else if (clazz.isAssignableFrom(FileType.class)) {
      object = (O) fileTypeFromMap(propertyMap, runtimeException);
    } else if (clazz.isAssignableFrom(Instrument.class)) {
      CRUDRepository<?> repository = Arrays.stream(dependencyRepositories)
          .filter(r -> r instanceof FileTypeRepository)
          .findFirst().orElseThrow(
              () -> new RowConversionException("Instrument translation missing fileType repository", row)
          );
      object = (O) instrumentFromMap(propertyMap, (FileTypeRepository) repository, runtimeException);
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
          runtimeException
      );
    } else if (clazz.isAssignableFrom(Person.class)) {
      object = (O) objectWithNameFromMap(propertyMap, runtimeException, (p) -> Person.builder()
          .uuid(p.getUuid())
          .name(p.getName())
          .organization(getProperty(propertyMap, "organization"))
          .position(getProperty(propertyMap, "position"))
          .street(getProperty(propertyMap, "street"))
          .city(getProperty(propertyMap, "city"))
          .state(getProperty(propertyMap, "state"))
          .zip(getProperty(propertyMap, "zip"))
          .country(getProperty(propertyMap, "country"))
          .email(getProperty(propertyMap, "email"))
          .phone(getProperty(propertyMap, "phone"))
          .orcid(getProperty(propertyMap, "orcid"))
          .build());
    } else if (clazz.isAssignableFrom(Organization.class)) {
      object = (O) objectWithNameFromMap(propertyMap, runtimeException, (o) -> Organization.builder()
          .uuid(o.getUuid())
          .name(o.getName())
          .street(getProperty(propertyMap, "street"))
          .city(getProperty(propertyMap, "city"))
          .state(getProperty(propertyMap, "state"))
          .zip(getProperty(propertyMap, "zip"))
          .country(getProperty(propertyMap, "country"))
          .email(getProperty(propertyMap, "email"))
          .phone(getProperty(propertyMap, "phone"))
          .build());
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

  public static void validateTranslator(TabularTranslator<? extends TabularTranslationField> translator, Class<?> clazz) throws TranslatorValidationException {
    Set<String> translatorFields = translator.getFields().stream()
        .map(TabularTranslationField::getPropertyName)
        .collect(Collectors.toSet());
    
    if (clazz.isAssignableFrom(Ship.class)) {
      validateTranslatorWithFlatFields(translatorFields, Ship.class);
    } else if (clazz.isAssignableFrom(Sea.class)) {
      validateTranslatorWithFlatFields(translatorFields, Sea.class);
    } else if (clazz.isAssignableFrom(Project.class)) {
      validateTranslatorWithFlatFields(translatorFields, Project.class);
    } else if (clazz.isAssignableFrom(Platform.class)) {
      validateTranslatorWithFlatFields(translatorFields, Platform.class);
    } else if (clazz.isAssignableFrom(DetectionType.class)) {
      validateTranslatorWithFlatFields(translatorFields, DetectionType.class);
    } else if (clazz.isAssignableFrom(Sensor.class)) {
      validateSensorTranslator(translatorFields);
    } else if (clazz.isAssignableFrom(Instrument.class)) {
      validateInstrumentTranslator(translator);
    } else if (clazz.isAssignableFrom(FileType.class)) {
      validateTranslatorWithFlatFields(translatorFields, FileType.class);
    } else if (clazz.isAssignableFrom(Package.class)) {
      validateTranslatorWithFlatFields(translatorFields, Package.class);
    } else if (clazz.isAssignableFrom(Person.class)) {
      validateTranslatorWithFlatFields(translatorFields, Person.class);
    } else if (clazz.isAssignableFrom(Organization.class)) {
      validateTranslatorWithFlatFields(translatorFields, Organization.class);
    } else {
      throw new TranslatorValidationException(String.format(
          "Translation not supported for %s", clazz.getSimpleName()
      ));
    }
  }
  
  private static void validateInstrumentTranslator(TabularTranslator<? extends TabularTranslationField> translator) throws TranslatorValidationException {
    Set<String> baseRequiredFields = new HashSet<>(Set.of(
        "uuid", "name", "fileTypes" 
    ));
    
    Set<String> fieldNames = translator.getFields().stream()
        .map(TabularTranslationField::getPropertyName)
        .collect(Collectors.toSet());

    Set<String> missingFields = baseRequiredFields.stream()
        .filter(v -> !fieldNames.contains(v))
        .collect(Collectors.toSet());

    if (!missingFields.isEmpty()) {
      throw new TranslatorValidationException(String.format(
          "Translator does not fully describe %s. Missing fields: %s", Instrument.class.getSimpleName(), missingFields
      ));
    }
  }
  
  private static void validateSensorTranslator(Set<String> propertyNames) throws TranslatorValidationException {
    Set<String> baseRequiredFields = new HashSet<>(Set.of(
        "uuid", "name", "description", "position.x", "position.y", "position.z", "type"
    ));

    Set<String> missingFields = baseRequiredFields.stream()
        .filter(v -> !propertyNames.contains(v))
        .collect(Collectors.toSet());
    
    if (!missingFields.isEmpty()) {
      throw new TranslatorValidationException(String.format(
          "Translator does not fully describe %s. Missing fields: %s", Sensor.class.getSimpleName(), missingFields
      ));
    }
  }
  
  private static void validateTranslatorWithFlatFields(Set<String> propertyNames, Class<?> clazz) throws TranslatorValidationException {
    Set<String> missingFieldNames = Arrays.stream(clazz.getDeclaredFields())
        .map(Field::getName)
        .filter(name -> !propertyNames.contains(name))
        .collect(Collectors.toSet());

    if (!missingFieldNames.isEmpty()) {
      throw new TranslatorValidationException(
          String.format(
              "Translator does not fully describe %s. Missing fields: %s", clazz.getSimpleName(), missingFieldNames
          )
      );
    }
  }
  
  private static Dataset packingJobFromMap(
      Map<String, Optional<String>> propertyMap,
      ProjectRepository projectRepository,
      PersonRepository personRepository,
      OrganizationRepository organizationRepository,
      PlatformRepository platformRepository,
      InstrumentRepository instrumentRepository,
      SensorRepository sensorRepository,
      DetectionTypeRepository detectionTypeRepository,
      SeaRepository seaRepository,
      ShipRepository shipRepository,
      RuntimeException runtimeException
  ) {
    Path temperaturePath = getPropertyAsPath("temperaturePath", propertyMap, runtimeException);
    Path documentsPath = getPropertyAsPath("documentsPath", propertyMap, runtimeException);
    Path otherPath = getPropertyAsPath("otherPath", propertyMap, runtimeException);
    Path navigationPath = getPropertyAsPath("navigationPath", propertyMap, runtimeException);
    Path calibrationDocumentsPath = getPropertyAsPath("calibrationDocumentsPath", propertyMap, runtimeException);
    Path sourcePath = getPropertyAsPath("sourcePath", propertyMap, runtimeException);
    Path biologicalPath = getPropertyAsPath("biologicalPath", propertyMap, runtimeException);
    
    String siteOrCruiseName = getProperty(propertyMap, "siteOrCruiseName");
    String deploymentId = getProperty(propertyMap, "deploymentId");
    LocalDate publicReleaseDate = getPropertyAsDate(propertyMap, "publicReleaseDate", runtimeException);
    List<Project> projects = getPropertyAsDelimitedResources(
        "projects",
        propertyMap,
        projectRepository,
        runtimeException
    );
    Person datasetPackager = getPropertyAsResource(
        "datasetPackager",
        getProperty(propertyMap, "datasetPackager"),
        personRepository,
        runtimeException
    );

    List<Person> scientists = getPropertyAsDelimitedResources(
        "scientists",
        propertyMap,
        personRepository,
        runtimeException
    );
    
    List<Organization> funders = getPropertyAsDelimitedResources(
        "funders",
        propertyMap,
        organizationRepository,
        runtimeException
    );
    List<Organization> sponsors = getPropertyAsDelimitedResources(
        "sponsors",
        propertyMap,
        organizationRepository,
        runtimeException
    );
    Platform platform = getPropertyAsResource(
        "platform",
        getProperty(propertyMap, "platform"),
        platformRepository,
        runtimeException
    );
    Instrument instrument = getPropertyAsResource(
        "instrument",
        getProperty(propertyMap, "instrument"),
        instrumentRepository,
        runtimeException
    );
    LocalDateTime startTime = getPropertyAsDateTime(propertyMap, "startTime", runtimeException);
    LocalDateTime endTime = getPropertyAsDateTime(propertyMap, "endTime", runtimeException);
    LocalDate preDeploymentCalibrationDate = getPropertyAsDate(propertyMap, "preDeploymentCalibrationDate", runtimeException);
    LocalDate postDeploymentCalibrationDate = getPropertyAsDate(propertyMap, "postDeploymentCalibrationDate", runtimeException);
    String calibrationDescription = getProperty(propertyMap, "calibrationDescription");
    String deploymentTitle = getProperty(propertyMap, "deploymentTitle");
    String deploymentPurpose = getProperty(propertyMap, "deploymentPurpose");
    String deploymentDescription = getProperty(propertyMap, "deploymentDescription");
    String alternateSiteName = getProperty(propertyMap, "alternateSiteName");
    String alternateDeploymentName = getProperty(propertyMap, "alternateDeploymentName");
    String softwareNames = getProperty(propertyMap, "softwareNames");
    String softwareVersions = getProperty(propertyMap, "softwareVersions");
    String softwareProtocolCitation = getProperty(propertyMap, "softwareProtocolCitation");
    String softwareDescription = getProperty(propertyMap, "softwareDescription");
    String softwareProcessingDescription = getProperty(propertyMap, "softwareProcessingDescription");
    String datasetType = getProperty(propertyMap, "datasetType");
    String instrumentId = getProperty(propertyMap, "instrumentId");
    Float hydrophoneSensitivity = getPropertyAsFloat(propertyMap, "hydrophoneSensitivity", runtimeException);
    Float frequencyRange = getPropertyAsFloat(propertyMap, "frequencyRange", runtimeException);
    Float gain = getPropertyAsFloat(propertyMap, "gain", runtimeException);
    Person qualityAnalyst = getPropertyAsResource(
        "qualityAnalyst",
        getProperty(propertyMap, "qualityAnalyst"),
        personRepository,
        runtimeException
    );
    String qualityAnalysisObjectives = getProperty(propertyMap, "qualityAnalysisObjectives");
    String qualityAnalysisMethod = getProperty(propertyMap, "qualityAnalysisMethod");
    String qualityAssessmentDescription = getProperty(propertyMap, "qualityAssessmentDescription");
    LocalDateTime deploymentTime = getPropertyAsDateTime(propertyMap, "deploymentTime", runtimeException);
    LocalDateTime recoveryTime = getPropertyAsDateTime(propertyMap, "recoveryTime", runtimeException);
    String comments = getProperty(propertyMap, "comments");
    List<Sensor> sensors = getPropertyAsDelimitedResources("sensors", propertyMap, sensorRepository, runtimeException);
    List<DataQualityEntry> qualityEntries = qualityEntriesFromMap(propertyMap, runtimeException);
    DetectionType detectionType = getPropertyAsResource(
        "soundSource",
        getProperty(propertyMap, "soundSource"),
        detectionTypeRepository,
        runtimeException
    );
    Integer analysisTimeZone = getPropertyAsInteger(propertyMap, "analysisTimeZone", runtimeException);
    Integer analysisEffort = getPropertyAsInteger(propertyMap, "analysisEffort", runtimeException);
    Float sampleRate = getPropertyAsFloat(propertyMap, "sampleRate", runtimeException);
    Float minFrequency = getPropertyAsFloat(propertyMap, "minFrequency", runtimeException);
    Float maxFrequency = getPropertyAsFloat(propertyMap, "maxFrequency", runtimeException);
    LocalDateTime audioStartTime = getPropertyAsDateTime(propertyMap, "audioStartTime", runtimeException);
    LocalDateTime audioEndTime = getPropertyAsDateTime(propertyMap, "audioEndTime", runtimeException);
    Float modeledFrequency = getPropertyAsFloat(propertyMap, "modeledFrequency", runtimeException);
    
    LocationDetail locationDetail = locationDetailFromMap(propertyMap, runtimeException, seaRepository, shipRepository);

    Dataset dataset = null;
    if (DatasetType.SOUND_CLIPS.getName().equals(datasetType)) {
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
    } else if (DatasetType.AUDIO.getName().equals(datasetType)) {
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
          .channels(channelsFromMap(propertyMap, sensorRepository, runtimeException))
          .locationDetail(locationDetail)
          .build();
    } else if (DatasetType.CPOD.getName().equals(datasetType)) {
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
          .channels(channelsFromMap(propertyMap, sensorRepository, runtimeException))
          .locationDetail(locationDetail)
          .build();
    } else if (DatasetType.DETECTIONS.getName().equals(datasetType)) {
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
    } else if (DatasetType.SOUND_LEVEL_METRICS.getName().equals(datasetType)) {
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
    } else if (DatasetType.SOUND_PROPAGATION_MODELS.getName().equals(datasetType)) {
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
          )
      ));
    }

    return dataset;
  }
  
  private static List<DataQualityEntry> qualityEntriesFromMap(Map<String, Optional<String>> propertyMap, RuntimeException runtimeException) {
    
    Map<Integer, List<Entry<String, Optional<String>>>> qcMap = propertyMap.entrySet().stream()
        .filter(e -> e.getKey().matches("^qualityEntries\\[\\d]\\.(?:startTime|endTime|minFrequency|maxFrequency|qualityLevel|comments)"))
        .collect(Collectors.groupingBy(
            e -> Integer.parseInt(String.valueOf(e.getKey().split("qualityEntries\\[")[1].charAt(0)))
        ));
    
    List<DataQualityEntry> dataQualityEntries = new ArrayList<>(0);
    
    qcMap.forEach((key, value) -> {
      Map<String, Optional<String>> map = Map.ofEntries(value.toArray(Entry[]::new));
      
      dataQualityEntries.add(key, DataQualityEntry.builder()
              .startTime(getPropertyAsDateTime(map, String.format(
                  "qualityEntries[%s].startTime", key
              ), runtimeException))
              .endTime(getPropertyAsDateTime(map, String.format(
                  "qualityEntries[%s].endTime", key
              ), runtimeException))
              .minFrequency(getPropertyAsFloat(map, String.format(
                  "qualityEntries[%s].minFrequency", key
              ), runtimeException))
              .maxFrequency(getPropertyAsFloat(map, String.format(
                  "qualityEntries[%s].maxFrequency", key
              ), runtimeException))
              .qualityLevel(getQualityLevel(map, String.format(
                  "qualityEntries[%s].qualityLevel", key
              ), runtimeException))
              .comments(getProperty(map, String.format(
                  "qualityEntries[%s].comments", key
              )))
          .build());
    });
    
    return dataQualityEntries;
  }
  
  private static FileType fileTypeFromMap(Map<String, Optional<String>> propertyMap, RuntimeException runtimeException) {
    UUID uuid = uuidFromString(getProperty(propertyMap, "uuid"), runtimeException);

    String type = getProperty(propertyMap, "type");
    String comment = getProperty(propertyMap, "comment");
    
    return FileType.builder()
        .uuid(uuid)
        .type(type)
        .comment(comment)
        .build();
  }
  
  private static Instrument instrumentFromMap(Map<String, Optional<String>> propertyMap, FileTypeRepository fileTypeRepository, RuntimeException runtimeException) {
    UUID uuid = uuidFromString(getProperty(propertyMap, "uuid"), runtimeException);

    String name = getProperty(propertyMap, "name");

    List<FileType> fileTypes = getPropertyAsDelimitedResources(
        "fileTypes",
        propertyMap,
        fileTypeRepository,
        runtimeException
    );
    
    return Instrument.builder()
        .uuid(uuid)
        .name(name)
        .fileTypes(fileTypes)
        .build();
  }
  
  private static Sensor sensorFromMap(Map<String, Optional<String>> propertyMap, RuntimeException runtimeException) {
    UUID uuid = uuidFromString(getProperty(propertyMap, "uuid"), runtimeException);
    
    String name = getProperty(propertyMap, "name");
    String description = getProperty(propertyMap, "description");

    Float x = getPropertyAsFloat(propertyMap, "position.x", runtimeException);
    Float y = getPropertyAsFloat(propertyMap, "position.y", runtimeException);
    Float z = getPropertyAsFloat(propertyMap, "position.z", runtimeException);
    
    Position position = Position.builder()
        .x(x)
        .y(y)
        .z(z)
        .build();
    
    SensorType type = getSensorType(propertyMap, runtimeException);
    
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
          .hydrophoneId(getProperty(propertyMap, "hydrophoneId"))
          .preampId(getProperty(propertyMap, "preampId"))
          .build();
    } else if (SensorType.other.equals(type)) {
      sensor = OtherSensor.builder()
          .uuid(uuid)
          .name(name)
          .position(position)
          .description(description)
          .properties(getProperty(propertyMap, "properties"))
          .sensorType(getProperty(propertyMap, "sensorType"))
          .build();
    }
    
    return sensor;
  }

  private static <O extends ObjectWithName> O objectWithNameFromMap(Map<String, Optional<String>> propertyMap, RuntimeException runtimeException, Function<ObjectWithName, O> convertFn) {
    return convertFn.apply(new ObjectWithName() {
      @Override
      public String getName() {
        return getProperty(propertyMap, "name");
      }

      @Override
      public UUID getUuid() {
        return uuidFromString(getProperty(propertyMap, "uuid"), runtimeException);
      }
    });
  }
  
  private static UUID uuidFromString(String uuidString, RuntimeException runtimeException) {
    if (uuidString == null || StringUtils.isBlank(uuidString)) {
      return null;
    }
    
    try {
      return UUID.fromString(uuidString);
    } catch (IllegalArgumentException e) {
      runtimeException.addSuppressed(
          new FieldException("uuid", "invalid uuid format")
      );
      return null;
    }
  }
  
  private static SensorType getSensorType(Map<String, Optional<String>> map, RuntimeException runtimeException) {
    String typeString = getProperty(map, "type");
    if (typeString == null || StringUtils.isBlank(typeString)) {
      return null;
    }
    
    try {
      return SensorType.valueOf(typeString);
    } catch (Exception e) {
      runtimeException.addSuppressed(
          new FieldException("type", String.format(
              "Invalid sensor type. Was not one of %s", Arrays.stream(SensorType.values())
                  .map(Enum::name)
                  .collect(Collectors.joining(", "))
          ))
      );
      
      return null;
    }
  }
  
  private static QualityLevel getQualityLevel(Map<String, Optional<String>> map, String property, RuntimeException runtimeException) {
    String qualityLevelString = getProperty(map, property);
    if (qualityLevelString == null || StringUtils.isBlank(qualityLevelString)) {
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
              )
          )
      );
      return null;
    }
  }
  
  private static String getProperty(Map<String, Optional<String>> map, String property) {
    Optional<String> value = map.get(property);
    if (value == null) {
      return null;
    }
    
    String stringValue = value.orElse(null);
    if (stringValue != null && StringUtils.isBlank(stringValue)) {
      return null;
    }
    
    if (stringValue == null) {
      return null;
    }

    return stringValue.trim();
  }
  
  private static Float getPropertyAsFloat(Map<String, Optional<String>> map, String property, RuntimeException runtimeException) {
    String propertyStringValue = getProperty(map, property);
    
    if (propertyStringValue == null) {
      return null;
    }
    
    try {
      return Float.parseFloat(propertyStringValue);
    } catch (NumberFormatException e) {
      runtimeException.addSuppressed(
          new FieldException(property, "invalid decimal format")
      );
      return null;
    }
  }
  
  private static Integer getPropertyAsInteger(Map<String, Optional<String>> map, String property, RuntimeException runtimeException) {
    String propertyStringValue = getProperty(map, property);

    if (propertyStringValue == null) {
      return null;
    }

    try {
      return Integer.parseInt(propertyStringValue);
    } catch (NumberFormatException e) {
      runtimeException.addSuppressed(
          new FieldException(property, "invalid integer format")
      );
      return null;
    }
  }
  
  private static LocalDate getPropertyAsDate(Map<String, Optional<String>> map, String property, RuntimeException runtimeException) {
    String propertyStringValue = getProperty(map, property);
    
    if (propertyStringValue == null) {
      return null;
    }
    
    try {
      return LocalDate.parse(propertyStringValue);
    } catch (DateTimeParseException e) {
      runtimeException.addSuppressed(
          new FieldException(property, "invalid date format")
      );
      return null;
    }
  }
  
  private static LocalDateTime getPropertyAsDateTime(Map<String, Optional<String>> propertyMap, String property, RuntimeException runtimeException) {
    String propertyStringValue = getProperty(propertyMap, property);
    
    if (propertyStringValue == null) {
      return null;
    }
    
    try {
      return LocalDateTime.parse(propertyStringValue);
    } catch (DateTimeParseException e) {
      runtimeException.addSuppressed(
          new FieldException(property, "invalid date time format")
      );
      return null;
    }
  }
  
  private static <O extends ObjectWithUniqueField> List<O> getPropertyAsDelimitedResources(String propertyName, Map<String, Optional<String>> propertyMap, CRUDRepository<O> repository, RuntimeException runtimeException) {
    String uniqueFields = getProperty(propertyMap, propertyName);
    
    if (uniqueFields != null) {
      return Arrays.stream(uniqueFields.split(";"))
          .map(uniqueField -> getPropertyAsResource(propertyName, uniqueField, repository, runtimeException))
          .toList();
    } else {
      return Collections.emptyList();
    }
  }
  
  private static <O extends ObjectWithUniqueField> O getPropertyAsResource(String propertyName, String uniqueField, CRUDRepository<O> repository, RuntimeException runtimeException) {
    if (uniqueField == null) {
      return null;
    }

    try {
      return repository.getByUniqueField(uniqueField);
    } catch (DatastoreException | NotFoundException e) {
      runtimeException.addSuppressed(new FieldException(
          propertyName, e.getMessage()
      ));
      return null;
    }
  }
  
  private static Path getPropertyAsPath(String propertyName, Map<String, Optional<String>> propertyMap, RuntimeException runtimeException) {
    String stringValue = getProperty(propertyMap, propertyName);
    
    if (stringValue == null) {
      return null;
    }
    
    try {
      return Path.of(stringValue);
    } catch (InvalidPathException e) {
      runtimeException.addSuppressed(
          new FieldException(propertyName, "Invalid path format")
      );
      return null;
    }
  }
  
  private static LocationDetail locationDetailFromMap(Map<String, Optional<String>> propertyMap, RuntimeException runtimeException, SeaRepository seaRepository, ShipRepository shipRepository) {
    String locationType = getProperty(propertyMap, "locationType");
    if (locationType == null) {
      return null;
    }
    if (LocationType.STATIONARY_MARINE.getName().equals(locationType)) {
      return StationaryMarineLocation.builder()
          .seaArea(getPropertyAsResource("seaArea", getProperty(propertyMap, "seaArea"), seaRepository, runtimeException))
          .deploymentLocation(MarineInstrumentLocation.builder()
              .latitude(getPropertyAsFloat(propertyMap, "deploymentLocation.latitude", runtimeException))
              .longitude(getPropertyAsFloat(propertyMap, "deploymentLocation.longitude", runtimeException))
              .seaFloorDepth(getPropertyAsFloat(propertyMap, "deploymentLocation.seaFloorDepth", runtimeException))
              .instrumentDepth(getPropertyAsFloat(propertyMap, "deploymentLocation.instrumentDepth", runtimeException))
              .build())
          .recoveryLocation(MarineInstrumentLocation.builder()
              .latitude(getPropertyAsFloat(propertyMap, "recoveryLocation.latitude", runtimeException))
              .longitude(getPropertyAsFloat(propertyMap, "recoveryLocation.longitude", runtimeException))
              .seaFloorDepth(getPropertyAsFloat(propertyMap, "recoveryLocation.seaFloorDepth", runtimeException))
              .instrumentDepth(getPropertyAsFloat(propertyMap, "recoveryLocation.instrumentDepth", runtimeException))
              .build())
          .build();
    } else if (LocationType.MOBILE_MARINE.getName().equals(locationType)) {
      return MobileMarineLocation.builder()
          .seaArea(getPropertyAsResource("seaArea", getProperty(propertyMap, "seaArea"), seaRepository, runtimeException))
          .vessel(getPropertyAsResource("vessel", getProperty(propertyMap, "vessel"), shipRepository, runtimeException))
          .locationDerivationDescription(getProperty(propertyMap, "locationDerivationDescription"))
          .build();
    } else if (LocationType.STATIONARY_TERRESTRIAL.getName().equals(locationType)) {
      return StationaryTerrestrialLocation.builder()
          .latitude(getPropertyAsFloat(propertyMap, "latitude", runtimeException))
          .longitude(getPropertyAsFloat(propertyMap, "longitude", runtimeException))
          .surfaceElevation(getPropertyAsFloat(propertyMap, "surfaceElevation", runtimeException))
          .instrumentElevation(getPropertyAsFloat(propertyMap, "instrumentElevation", runtimeException))
          .build();
    } else if (LocationType.MULTIPOINT_STATIONARY_MARINE.getName().equals(locationType)) {
      List<MarineInstrumentLocation> marineInstrumentLocations = new ArrayList<>(0);
      
      Map<Integer, List<Entry<String, Optional<String>>>> locationMap = propertyMap.entrySet().stream()
          .filter(e -> e.getKey().matches("^locations\\[\\d]\\.(?:latitude|longitude|seaFloorDepth|instrumentDepth)"))
          .collect(Collectors.groupingBy(
              e -> Integer.parseInt(String.valueOf(e.getKey().split("locations\\[")[1].charAt(0)))
          ));
      
      locationMap.forEach((key, value) -> {
        Map<String, Optional<String>> map = Map.ofEntries(value.toArray(Entry[]::new));
        marineInstrumentLocations.add(key, MarineInstrumentLocation.builder()
            .latitude(getPropertyAsFloat(map, String.format(
                "locations[%s].latitude", key 
            ), runtimeException))
            .longitude(getPropertyAsFloat(map, String.format(
                "locations[%s].longitude", key 
            ), runtimeException))
            .seaFloorDepth(getPropertyAsFloat(map, String.format(
                "locations[%s].seaFloorDepth", key 
            ), runtimeException))
            .instrumentDepth(getPropertyAsFloat(map, String.format(
                "locations[%s].instrumentDepth", key 
            ), runtimeException))
            .build());
      });
      
      return MultiPointStationaryMarineLocation.builder()
          .seaArea(getPropertyAsResource("seaArea", getProperty(propertyMap, "seaArea"), seaRepository, runtimeException))
          .locations(marineInstrumentLocations)
          .build();
    } else {
      runtimeException.addSuppressed(new FieldException(
          "type", String.format(
          "Invalid location type. Was not one of %s", Arrays.stream(LocationType.values())
              .map(LocationType::getName)
              .collect(Collectors.joining(", "))
          )
      ));
      return null;
    }
  }
  
  private static List<Channel> channelsFromMap(Map<String, Optional<String>> propertyMap, SensorRepository sensorRepository, RuntimeException runtimeException) {
    List<Channel> channels = new ArrayList<>(0);
    
    Map<Integer, List<Entry<String, Optional<String>>>> channelsMap = propertyMap.entrySet().stream()
        .filter(e -> e.getKey().matches("^channels\\[\\d].*"))
        .collect(Collectors.groupingBy(
            e -> Integer.parseInt(String.valueOf(
                e.getKey().split("channels\\[")[1].charAt(0)
            ))
        ));
    
    channelsMap.forEach((key, value) -> {
      Map<String, Optional<String>> map = Map.ofEntries(value.toArray(Entry[]::new));
      
      String propertyPrefix = String.format("channels[%s]", key);
      
      channels.add(key, Channel.builder()
              .sensor(getPropertyAsResource(
                  propertyPrefix + ".sensor", getProperty(map, propertyPrefix + ".sensor"), sensorRepository, runtimeException 
              ))
              .startTime(getPropertyAsDateTime(map, propertyPrefix + ".startTime", runtimeException))
              .endTime(getPropertyAsDateTime(map, propertyPrefix + ".endTime", runtimeException))
              .sampleRates(sampleRatesFromMap(map, key, runtimeException))
              .dutyCycles(dutyCyclesFromMap(map, key, runtimeException))
              .gains(gainsFromMap(map, key, runtimeException))
          .build());
    });
    
    return channels;
  }
  
  private static List<SampleRate> sampleRatesFromMap(Map<String, Optional<String>> propertyMap, Integer channelIndex, RuntimeException runtimeException) {
    List<SampleRate> sampleRates = new ArrayList<>(0);
    
    Map<Integer, List<Entry<String, Optional<String>>>> sampleRatesMap = propertyMap.entrySet().stream()
        .filter(e -> e.getKey().matches("^channels\\[\\d].sampleRates\\[\\d].*"))
        .collect(Collectors.groupingBy(
            e -> Integer.parseInt(String.valueOf(
                e.getKey().split("sampleRates\\[")[1].charAt(0)
            ))
        ));
    
    sampleRatesMap.forEach((key, value) -> {
      Map<String, Optional<String>> map = Map.ofEntries(value.toArray(Entry[]::new));
      
      String propertyPrefix = String.format(
          "channels[%s].sampleRates[%s]", channelIndex, key
      );
      
      sampleRates.add(key, SampleRate.builder()
              .startTime(getPropertyAsDateTime(map, propertyPrefix + ".startTime", runtimeException))
              .endTime(getPropertyAsDateTime(map, propertyPrefix + ".endTime", runtimeException))
              .sampleRate(getPropertyAsFloat(map, propertyPrefix + ".sampleRate", runtimeException))
              .sampleBits(getPropertyAsInteger(map, propertyPrefix + ".sampleBits", runtimeException))
          .build());
    });
    
    return sampleRates;
  }
  
  private static List<DutyCycle> dutyCyclesFromMap(Map<String, Optional<String>> propertyMap, Integer channelIndex, RuntimeException runtimeException) {
    List<DutyCycle> dutyCycles = new ArrayList<>(0);
    
    Map<Integer, List<Entry<String, Optional<String>>>> dutyCyclesMap = propertyMap.entrySet().stream()
        .filter(e -> e.getKey().matches("^channels\\[\\d].dutyCycles\\[\\d].*"))
        .collect(Collectors.groupingBy(
            e -> Integer.parseInt(String.valueOf(
                e.getKey().split("dutyCycles\\[")[1].charAt(0)
            ))
        ));
    
    dutyCyclesMap.forEach((key, value) -> {
      Map<String, Optional<String>> map = Map.ofEntries(value.toArray(Entry[]::new));
      
      String propertyPrefix = String.format(
        "channels[%s].dutyCycles[%s]", channelIndex, key  
      );
      
      dutyCycles.add(DutyCycle.builder()
              .startTime(getPropertyAsDateTime(map, propertyPrefix + ".startTime", runtimeException))
              .endTime(getPropertyAsDateTime(map, propertyPrefix + ".endTime", runtimeException))
              .interval(getPropertyAsFloat(map, propertyPrefix + ".interval", runtimeException))
              .duration(getPropertyAsFloat(map, propertyPrefix + ".duration", runtimeException))
          .build());
    });
    
    return dutyCycles;
  }
  
  private static List<Gain> gainsFromMap(Map<String, Optional<String>> propertyMap, Integer channelIndex, RuntimeException runtimeException) {
    List<Gain> gains = new ArrayList<>(0);
    
    Map<Integer, List<Entry<String, Optional<String>>>> gainsMap = propertyMap.entrySet().stream()
        .filter(e -> e.getKey().matches("^channels\\[\\d].gains\\[\\d].*"))
        .collect(Collectors.groupingBy(
            e -> Integer.parseInt(String.valueOf(
                e.getKey().split("gains\\[")[1].charAt(0)
            ))
        ));
    
    gainsMap.forEach((key, value) -> {
      Map<String, Optional<String>> map = Map.ofEntries(value.toArray(Entry[]::new));
      
      String propertyPrefix = String.format(
          "channels[%s].gains[%s]", channelIndex, key
      );
      
      gains.add(Gain.builder()
              .startTime(getPropertyAsDateTime(map, propertyPrefix + ".startTime", runtimeException))
              .endTime(getPropertyAsDateTime(map, propertyPrefix + ".endTime", runtimeException))
              .gain(getPropertyAsFloat(map, propertyPrefix + ".gain", runtimeException))
          .build());
    });
    
    return gains;
  }

}
