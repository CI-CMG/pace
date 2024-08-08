package edu.colorado.cires.pace.packaging;

import static edu.colorado.cires.pace.packaging.FileUtils.filterHidden;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Channel;
import edu.colorado.cires.pace.data.object.dataset.base.ProcessingLevel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.DutyCycle;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.Gain;
import edu.colorado.cires.pace.data.object.contact.organization.Organization;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import edu.colorado.cires.pace.data.object.contact.person.Person;
import edu.colorado.cires.pace.data.object.position.Position;
import edu.colorado.cires.pace.data.object.project.Project;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.QualityLevel;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.SampleRate;
import edu.colorado.cires.pace.data.object.sensor.audio.AudioSensor;
import edu.colorado.cires.pace.data.object.sensor.depth.DepthSensor;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.repository.OrganizationRepository;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.pace.repository.SensorRepository;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import edu.colorado.cires.passivePacker.data.AbstractObjectWithName;
import edu.colorado.cires.passivePacker.data.PassivePackerAudioSensor;
import edu.colorado.cires.passivePacker.data.PassivePackerCalibrationInfo;
import edu.colorado.cires.passivePacker.data.PassivePackerChannel;
import edu.colorado.cires.passivePacker.data.PassivePackerDatasetDetails;
import edu.colorado.cires.passivePacker.data.PassivePackerDeployment;
import edu.colorado.cires.passivePacker.data.PassivePackerDepthSensor;
import edu.colorado.cires.passivePacker.data.PassivePackerDutyCycle;
import edu.colorado.cires.passivePacker.data.PassivePackerGain;
import edu.colorado.cires.passivePacker.data.PassivePackerMobileMarineLocation;
import edu.colorado.cires.passivePacker.data.PassivePackerPackage;
import edu.colorado.cires.passivePacker.data.PassivePackerPerson;
import edu.colorado.cires.passivePacker.data.PassivePackerQualityDetails;
import edu.colorado.cires.passivePacker.data.PassivePackerQualityEntry;
import edu.colorado.cires.passivePacker.data.PassivePackerSampleRate;
import edu.colorado.cires.passivePacker.data.PassivePackerSamplingDetails;
import edu.colorado.cires.passivePacker.data.PassivePackerSensorType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PackagerProcessorTest {
  
  private static final List<Person> PEOPLE = List.of(Person.builder()
          .name("person")
          .organization("organization")
          .position("position")
      .build()); 
  
  private static final List<Organization> ORGANIZATIONS = List.of(Organization.builder()
          .name("organization")
      .build());
  
  private static final List<Project> PROJECTS = List.of(Project.builder()
          .name("project")
      .build());
  
  private final PersonRepository personRepository = mock(PersonRepository.class);
  private final OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
  private final SensorRepository sensorRepository = mock(SensorRepository.class);
  private final DetectionTypeRepository detectionTypeRepository = mock(DetectionTypeRepository.class);
  
  private final ObjectMapper objectMapper = SerializationUtils.createObjectMapper()
      .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
  private final Path testOutputPath = Paths.get("target").resolve("output");
  private final Path testSourcePath = Paths.get("target").resolve("source");

  private final PassivePackerFactory passivePackerFactory = new PassivePackerFactory(
      personRepository, organizationRepository, sensorRepository, detectionTypeRepository
  );
  
  @BeforeEach
  void beforeEach() {
    FileUtils.deleteQuietly(testOutputPath.toFile());
    FileUtils.deleteQuietly(testSourcePath.toFile());
  }
  
  @AfterEach
  void afterEach() {
    FileUtils.deleteQuietly(testOutputPath.toFile());
    FileUtils.deleteQuietly(testSourcePath.toFile());
  }
  
  @Test
  void testInvalidPackingJobNoSourcePath() throws IOException {
    Package packingJob = createPackingJob(
        null, null, null, null, null, null, null
    );
    
    ProgressIndicator progressIndicator = mock(ProgressIndicator.class);
    ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> new PackageProcessor(
        objectMapper, PEOPLE, ORGANIZATIONS, PROJECTS, Collections.singletonList(packingJob), testOutputPath, passivePackerFactory, progressIndicator
    ).process());
    assertEquals(String.format(
        "%s validation failed", Package.class.getSimpleName()
    ), exception.getMessage());
    
    assertEquals(1, exception.getConstraintViolations().size());
    ConstraintViolation<?> constraintViolation = exception.getConstraintViolations().iterator().next();
    assertEquals("sourcePath", constraintViolation.getPropertyPath().toString());
    assertEquals("must not be null", constraintViolation.getMessage());
    
    verify(progressIndicator, times(0)).incrementProcessedRecords();
  }

  @ParameterizedTest
  @CsvSource(value = {
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
      ",target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
      "target/source/bio,,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
      "target/source/bio,target/source/cal-docs,,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
      "target/source/bio,target/source/cal-docs,target/source/docs,,target/source/other,target/source/temperature,target/source/source-files",
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,,target/source/temperature,target/source/source-files",
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,,target/source/source-files",
      "target/source/bio,target/source/cal-docs,target/source/docs,target/source/nav,target/source/other,target/source/temperature,target/source/source-files",
  })
  void testProcess(
      String biologicalPath,
      String calibrationDocumentsPath,
      String documentsPath,
      String navigationPath,
      String otherPath,
      String temperaturePath,
      String sourcePath
  ) throws IOException, PackagingException, NotFoundException, DatastoreException {
    Path sp = sourcePath == null ? null : Path.of(sourcePath).toAbsolutePath();
    Path tp = temperaturePath == null ? null : Path.of(temperaturePath).toAbsolutePath();
    Path op = otherPath == null ? null : Path.of(otherPath).toAbsolutePath();
    Path np = navigationPath == null ? null : Path.of(navigationPath).toAbsolutePath();
    Path dp = documentsPath == null ? null : Path.of(documentsPath).toAbsolutePath();
    Path cdp = calibrationDocumentsPath == null ? null : Path.of(calibrationDocumentsPath).toAbsolutePath();
    Path bp = biologicalPath == null ? null : Path.of(biologicalPath).toAbsolutePath();

    writeFiles(bp);
    writeFiles(cdp);
    writeFiles(dp);
    writeFiles(np);
    writeFiles(op);
    writeFiles(tp);
    writeFiles(sp);
    
    Package packingJob = createPackingJob(
        sp,
        tp,
        op,
        np,
        dp,
        cdp,
        bp
    );
    
    packingJob = objectMapper.readValue(
        objectMapper.writeValueAsString(packingJob), Package.class
    );

    PassivePackerPackage expectedMetadataPackage = createExpectedMetadata(
        sp,
        np,
        cdp
    );
    String expectedMetadata = SerializationUtils.createObjectMapper()
        .setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_SNAKE_CASE)
        .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(expectedMetadataPackage);
    String expectedPeople = objectMapper.writeValueAsString(PEOPLE);
    String expectedOrganizations = objectMapper.writeValueAsString(ORGANIZATIONS);
    String expectedProjects = objectMapper.writeValueAsString(PROJECTS);
    
    int expectedNumberOfInvocations = 0;
    if (biologicalPath != null) {
      expectedNumberOfInvocations += 20;
    }
    if (calibrationDocumentsPath != null) {
      expectedNumberOfInvocations += 20;
    }
    if (documentsPath != null) {
      expectedNumberOfInvocations += 20;
    }
    if (navigationPath != null) {
      expectedNumberOfInvocations += 20;
    }
    if (otherPath != null) {
      expectedNumberOfInvocations += 20;
    }
    if (temperaturePath != null) {
      expectedNumberOfInvocations += 20;
    }
    if (sourcePath != null) {
      expectedNumberOfInvocations += 20;
    }
    
    ProgressIndicator progressIndicator = mock(ProgressIndicator.class);
    List<Package> packages = new PackageProcessor(
        objectMapper, PEOPLE, ORGANIZATIONS, PROJECTS, Collections.singletonList(packingJob), testOutputPath, passivePackerFactory, progressIndicator
    ).process();
    assertTrue(packages.stream().noneMatch(Package::isVisible));
    verify(progressIndicator, times(expectedNumberOfInvocations + 8)).incrementProcessedRecords();
    
    Path baseExpectedOutputPath = testOutputPath.resolve(packingJob.getPackageId()).resolve("data");

    checkTargetPaths(packingJob.getBiologicalPath(), baseExpectedOutputPath.resolve("biological"));
    checkTargetPaths(packingJob.getCalibrationDocumentsPath(), baseExpectedOutputPath.resolve("calibration"));
    checkTargetPaths(packingJob.getDocumentsPath(), baseExpectedOutputPath.resolve("docs"));

    LocationDetail locationDetail = packingJob.getLocationDetail();
    assertInstanceOf(MobileMarineLocation.class, locationDetail);
    MobileMarineLocation mobileMarineLocation = (MobileMarineLocation) locationDetail;
    Path expectedOutputNavPath = baseExpectedOutputPath.resolve("nav_files");
    for (Path path : mobileMarineLocation.getFileList()) {
      Path expectedPath = getExpectedNavPath(path, expectedOutputNavPath);
      assertTrue(expectedPath.toFile().exists());
    }
    
    checkTargetPaths(packingJob.getOtherPath(), baseExpectedOutputPath.resolve("other"));
    checkTargetPaths(packingJob.getTemperaturePath(), baseExpectedOutputPath.resolve("temperature"));
    checkTargetPaths(packingJob.getSourcePath(), baseExpectedOutputPath.resolve(
        "acoustic_files"
    ));
    
    String actualMetadata = FileUtils.readFileToString(baseExpectedOutputPath.resolve(String.format(
        "%s.json", packingJob.getPackageId()
    )).toFile(), StandardCharsets.UTF_8);
    
    assertEquals(expectedMetadata, actualMetadata);
    
    assertTrue(baseExpectedOutputPath.getParent().resolve("process.log").toFile().exists());
    
    String actualPeople = FileUtils.readFileToString(baseExpectedOutputPath.resolve("people.json").toFile(), StandardCharsets.UTF_8);
    assertEquals(expectedPeople, actualPeople);
    
    String actualOrganizations = FileUtils.readFileToString(baseExpectedOutputPath.resolve("organizations.json").toFile(), StandardCharsets.UTF_8);
    assertEquals(expectedOrganizations, actualOrganizations);
    
    String actualProjects = FileUtils.readFileToString(baseExpectedOutputPath.resolve("projects.json").toFile(), StandardCharsets.UTF_8);
    assertEquals(expectedProjects, actualProjects);
  }

  private static Path getExpectedNavPath(Path path, Path expectedOutputNavPath) {
    Path expectedPath;
    String fileName = FilenameUtils.getName(path.toString());
    if (path.toString().contains("subdir")) {
      String fileExtension = FilenameUtils.getExtension(fileName);
      String baseName = FilenameUtils.getBaseName(fileName);
      expectedPath = expectedOutputNavPath.resolve(String.format(
          "%s (1).%s", baseName, fileExtension
      ));
    } else {
      expectedPath = expectedOutputNavPath.resolve(fileName);
    }
    return expectedPath;
  }

  private PassivePackerPackage createExpectedMetadata(
      Path sourcePath,
      Path navigationPath,
      Path calibrationDocumentsPath
  ) throws NotFoundException, DatastoreException, IOException {
    PassivePackerPackage passivePackerPackage = PassivePackerPackage.builder()
        .dataCollectionName("project-name-1_siteOrCruiseName_deploymentId")
        .projectName(List.of(
            "project-name-1", "project-name-2"
        )).deploymentName("deploymentId")
        .deploymentAlias("alternate-deployment-name")
        .site("siteOrCruiseName")
        .siteAliases(List.of(
            "alternate-site-name"
        ))
        .publishDate(LocalDate.of(2024, 7, 29).plusDays(1).toString())
        .scientists(List.of(
            AbstractObjectWithName.builder()
                .name("scientist-1")
                .build(),
            AbstractObjectWithName.builder()
                .name("scientist-2")
                .build()
        )).sponsors(List.of(
            AbstractObjectWithName.builder()
                .name("organization-1")
                .build(),
            AbstractObjectWithName.builder()
                .name("organization-2")
                .build()
        )).funders(List.of(
            AbstractObjectWithName.builder()
                .name("organization-3")
                .build(),
            AbstractObjectWithName.builder()
                .name("organization-4")
                .build()
        )).qualityDetails(PassivePackerQualityDetails.builder()
            .analyst("qualityAnalyst")
            .method("quality-analysis-method")
            .objectives("quality-analysis-objectives")
            .description("quality-assessment-description")
            .qualityDetails(List.of(
                PassivePackerQualityEntry.builder()
                    .comments("comment-1")
                    .quality("Good")
                    .lowFreq("5.0")
                    .highFreq("10.0")
                    .channels("1")
                    .start("2024-07-29T11:51:00")
                    .end("2024-07-29T12:01:00")
                    .build(),
                PassivePackerQualityEntry.builder()
                    .comments("comment-2")
                    .quality("Unusable")
                    .lowFreq("5.0")
                    .highFreq("10.0")
                    .channels("1")
                    .start("2024-07-29T11:41:00")
                    .end("2024-07-29T11:51:00")
                    .build()
            ))
            .build())
        .metadataAuthor(PassivePackerPerson.builder()
            .name("dataset-packager")
            .build())
        .sensors(Map.of(
            PassivePackerSensorType.DEPTH, List.of(
                PassivePackerDepthSensor.builder()
                    .type("Depth Sensor")
                    .name("depth-sensor")
                    .number("2")
                    .positionX("4.0")
                    .positionY("5.0")
                    .positionZ("6.0")
                    .build()
            ),
            PassivePackerSensorType.AUDIO, List.of(
                PassivePackerAudioSensor.builder()
                    .name("audio-sensor")
                    .type("Audio Sensor")
                    .number("1")
                    .positionX("1.0")
                    .positionY("2.0")
                    .positionZ("3.0")
                    .build(),
                PassivePackerAudioSensor.builder()
                    .name("audioSensor")
                    .type("Audio Sensor")
                    .number("3")
                    .positionX("7.0")
                    .positionY("8.0")
                    .positionZ("9.0")
                    .build()
            )
        )).title("deployment-title")
        .purpose("deployment-purpose")
        .description("deployment-description")
        .platformName("platform")
        .instrumentType("instrument")
        .instrumentId("instrumentId")
        .datasetDetails(PassivePackerDatasetDetails.builder()
            .type("Raw")
            .subType("Audio")
            .sourcePath(sourcePath.toString())
            .dataComment("deployment-comments")
            .build())
        .calibrationInfo(PassivePackerCalibrationInfo.builder()
            .calDate(LocalDate.of(2024, 7, 29).minusDays(1).toString())
            .calDate2(LocalDate.of(2024, 7, 29).plusDays(1).toString())
            .calState("Calibrated Pre & Post Deployment")
            .frequency("1-5")
            .sensitivity("10.0")
            .gain("1.0")
            .comment("calibration-description")
            .calDocsPath(calibrationDocumentsPath == null ? "" : calibrationDocumentsPath.toString())
            .build())
        .deployment(PassivePackerDeployment.builder()
            .deploymentTime("2024-07-25T12:01:00")
            .recoveryTime("2024-07-28T12:01:00")
            .location(PassivePackerMobileMarineLocation.builder()
                .seaArea("seaArea")
                .deployShip("vessel")
                .positionDetails("the description of the location")
                .deployType("Mobile Marine")
                .files(navigationPath == null ? "" : Files.walk(navigationPath)
                    .filter(p -> p.toFile().isFile())
                    .filter(p -> {
                      try {
                        return filterHidden(p);
                      } catch (IOException e) {
                        throw new RuntimeException(e);
                      }
                    })
                    .map(Path::toString)
                    .collect(Collectors.joining(",")))
                .build())
            .build())
        .channels(Map.of(
            1, PassivePackerChannel.builder()
                    .channelStart("2024-07-29T11:59:00")
                    .channelEnd("2024-07-29T12:00:00")
                    .sensor("3")
                    .samplingDetails(PassivePackerSamplingDetails.builder()
                        .sampling(List.of(
                            PassivePackerSampleRate.builder()
                                .start("2024-07-29T12:00:00")
                                .end("2024-07-29T12:01:00")
                                .sampleRate("10.0")
                                .sampleBits("10")
                                .build()
                        )).gain(List.of(
                            PassivePackerGain.builder()
                                .start("2024-07-29T11:41:00")
                                .end("2024-07-29T11:56:00")
                                .gain("1000.0")
                                .build()
                        )).dutyCycle(List.of(
                            PassivePackerDutyCycle.builder()
                                .start("2024-07-29T11:51:00")
                                .end("2024-07-29T11:56:00")
                                .duration("100.0")
                                .interval("1000.0")
                                .build()
                        ))
                        .build())
                .build()
        ))
        .build();

    for (AbstractObjectWithName scientist : passivePackerPackage.getScientists()) {
      when(personRepository.getByUniqueField(scientist.getName())).thenReturn(Person.builder()
                      .name(scientist.getName())
              .build());
    }

    when(personRepository.getByUniqueField(passivePackerPackage.getQualityDetails().getAnalyst())).thenReturn(Person.builder()
            .name(passivePackerPackage.getQualityDetails().getAnalyst())
        .build());

    PassivePackerPerson person = passivePackerPackage.getMetadataAuthor();
    when(personRepository.getByUniqueField(person.getName())).thenReturn(Person.builder()
            .uuid(person.getUuid())
            .name(person.getName())
            .position(person.getPosition())
            .organization(person.getOrganization())
            .street(person.getStreet())
            .city(person.getCity())
            .state(person.getState())
            .zip(person.getZip())
            .country(person.getCountry())
            .phone(person.getPhone())
            .email(person.getEmail())
        .build());

    
    for (AbstractObjectWithName funder : passivePackerPackage.getFunders()) {
      when(organizationRepository.getByUniqueField(funder.getName())).thenReturn(Organization.builder()
                      .name(funder.getName())
              .build());
    }
    for (AbstractObjectWithName sponsor : passivePackerPackage.getSponsors()) {
      when(organizationRepository.getByUniqueField(sponsor.getName())).thenReturn(Organization.builder()
                      .name(sponsor.getName())
              .build());
    }

    when(sensorRepository.getByUniqueField("depth-sensor")).thenReturn(
        DepthSensor.builder()
            .name("depth-sensor")
            .build()
    );
    when(sensorRepository.getByUniqueField("audio-sensor")).thenReturn(
        AudioSensor.builder()
            .name("audio-sensor")
            .build()
    );
    when(sensorRepository.getByUniqueField("audioSensor")).thenReturn(
        AudioSensor.builder()
            .name("audioSensor")
            .build()
    );
    
    return passivePackerPackage;
  }

  private Package createPackingJob(
      Path sourcePath,
      Path temperaturePath,
      Path otherPath,
      Path navigationPath,
      Path documentsPath,
      Path calibrationDocumentsPath,
      Path biologicalPath
  ) throws IOException {
    return AudioPackage.builder()
        .uuid(UUID.randomUUID())
        .sourcePath(sourcePath)
        .processingLevel(ProcessingLevel.Raw)
        .temperaturePath(temperaturePath)
        .otherPath(otherPath)
        .documentsPath(documentsPath)
        .calibrationDocumentsPath(calibrationDocumentsPath)
        .biologicalPath(biologicalPath)
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager("dataset-packager")
        .projects(List.of(
            "project-name-1", "project-name-2"
        )).publicReleaseDate(LocalDate.of(2024, 7, 29).plusDays(1))
        .scientists(List.of(
            "scientist-1", "scientist-2"
        )).sponsors(List.of(
            "organization-1", "organization-2"
        )).funders(List.of(
            "organization-3", "organization-4"
        )).platform(
            "platform"
        ).instrument("instrument")
        .instrumentId("instrumentId")
        .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(1))
        .endTime(LocalDateTime.of(2024, 7, 29, 12, 1))
        .preDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).minusDays(1))
        .postDeploymentCalibrationDate(LocalDate.of(2024, 7, 29).plusDays(1))
        .calibrationDescription("calibration-description")
        .hydrophoneSensitivity(10f)
        .frequencyRange("1-5")
        .gain(1f)
        .deploymentTitle("deployment-title")
        .deploymentPurpose("deployment-purpose")
        .deploymentDescription("deployment-description")
        .alternateSiteName("alternate-site-name")
        .alternateDeploymentName("alternate-deployment-name")
        .qualityAnalyst("qualityAnalyst")
        .qualityAnalysisObjectives("quality-analysis-objectives")
        .qualityAnalysisMethod("quality-analysis-method")
        .qualityAssessmentDescription("quality-assessment-description")
        .qualityEntries(List.of(
            DataQualityEntry.builder()
                .comments("comment-1")
                .qualityLevel(QualityLevel.good)
                .maxFrequency(10f)
                .minFrequency(5f)
                .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(10))
                .endTime(LocalDateTime.of(2024, 7, 29, 12, 1))
                .channelNumbers(List.of(1))
                .build(),
            DataQualityEntry.builder()
                .comments("comment-2")
                .qualityLevel(QualityLevel.unusable)
                .maxFrequency(10f)
                .minFrequency(5f)
                .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(20))
                .endTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(10))
                .channelNumbers(List.of(1))
                .build()
        )).deploymentTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusDays(4))
        .recoveryTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusDays(1))
        .comments("deployment-comments")
        .sensors(List.of(
            PackageSensor.<String>builder()
                .sensor("audio-sensor")
                .position(Position.builder()
                    .x(1f)
                    .y(2f)
                    .z(3f)
                    .build())
                .build(),
            PackageSensor.<String>builder()
                .sensor("depth-sensor")
                .position(Position.builder()
                    .x(4f)
                    .y(5f)
                    .z(6f)
                    .build())
                .build()
        )).channels(List.of(
            Channel.<String>builder()
                .sensor(PackageSensor.<String>builder()
                    .sensor("audioSensor")
                    .position(Position.builder()
                        .x(7f)
                        .y(8f)
                        .z(9f)
                        .build())
                    .build())
                .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(2))
                .endTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(1))
                .sampleRates(List.of(
                    SampleRate.builder()
                        .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(1))
                        .endTime(LocalDateTime.of(2024, 7, 29, 12, 1))
                        .sampleBits(10)
                        .sampleRate(10f)
                        .build()
                )).dutyCycles(List.of(
                    DutyCycle.builder()
                        .duration(100f)
                        .interval(1000f)
                        .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(10))
                        .endTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(5))
                        .build()
                )).gains(List.of(
                    Gain.builder()
                        .startTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(20))
                        .endTime(LocalDateTime.of(2024, 7, 29, 12, 1).minusMinutes(5))
                        .gain(1000f)
                        .build()
                ))
                .build()
        )).locationDetail(MobileMarineLocation.builder()
            .seaArea("seaArea")
            .vessel("vessel")
            .locationDerivationDescription("the description of the location")
            .fileList(
                navigationPath == null ? Collections.emptyList() :
                    Files.walk(navigationPath)
                        .filter(p -> p.toFile().isFile())
                        .filter(p -> {
                          try {
                            return filterHidden(p);
                          } catch (IOException e) {
                            throw new RuntimeException(e);
                          }
                        })
                        .collect(Collectors.toList())
            )
            .build())
        .build();
  }

  private void writeFiles(Path value) throws IOException {
    if (value == null) {
      return;
    }

    Path path = value.toAbsolutePath();

    for (int i = 0; i < 10; i++) {
      Path filePath = path.resolve(String.format(
          "test-%s.txt", i
      ));
      Path hiddenFilePath = path.resolve(String.format(
          ".test-%s.hidden", i
      ));
      FileUtils.createParentDirectories(filePath.toFile());
      try (
          FileWriter writer = new FileWriter(filePath.toFile(), StandardCharsets.UTF_8, true);
          FileWriter hiddenFileWriter = new FileWriter(hiddenFilePath.toFile(), StandardCharsets.UTF_8, true)
      ) {
        for (int j = 0; j < 10; j++) {
          writer.append(String.format(
              "test-data\t%s-%s", i, j
          ));
          hiddenFileWriter.append(String.format(
              "test-data\t%s-%s", i, j
          ));
        }
      }
      try {
        Files.setAttribute(hiddenFilePath, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
      } catch (UnsupportedOperationException ignored) {} // not running on Windows OS

      Path fileDirectoryPath = path.resolve("subdir").resolve(String.format(
          "test-%s.txt", i
      ));
      Path hiddenFileDirectoryPath = path.resolve("subdir").resolve(String.format(
          ".test-%s.hidden", i
      ));
      FileUtils.createParentDirectories(fileDirectoryPath.toFile());
      try (
          FileWriter writer = new FileWriter(fileDirectoryPath.toFile(), StandardCharsets.UTF_8, true);
          FileWriter hiddenFileWriter = new FileWriter(hiddenFileDirectoryPath.toFile(), StandardCharsets.UTF_8, true)
      ) {
        for (int j = 0; j < 10; j++) {
          writer.append(String.format(
              "test-data\t%s-%s", i, j
          ));
          hiddenFileWriter.append(String.format(
              "test-data\t%s-%s", i, j
          ));
        }
      }
      try {
        Files.setAttribute(hiddenFileDirectoryPath, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
      } catch (UnsupportedOperationException ignored) {} // not running on Windows OS
    }
  }

  private void checkTargetPaths(Path inputDirectory, Path expectedOutputDirectory) throws IOException {
    if (inputDirectory == null) {
      assertFalse(expectedOutputDirectory.toFile().exists());
      return;
    }
    
    Set<Path> outputPaths = Files.walk(expectedOutputDirectory)
        .filter(p -> p.toFile().isFile())
        .map(expectedOutputDirectory::relativize)
        .collect(Collectors.toSet());
    
    Set<Path> inputPaths = Files.walk(inputDirectory)
        .filter(p -> p.toFile().isFile() && !p.toFile().isHidden())
        .map(inputDirectory::relativize)
        .collect(Collectors.toSet());

    assertEquals(20, outputPaths.size()); // should not contain hidden files. should contain subdirectory contents in tree structure

    assertEquals(inputPaths, outputPaths);
  }

}
