package edu.colorado.cires.pace.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.Dataset;
import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.datastore.json.CSVTranslatorJsonDatastore;
import edu.colorado.cires.pace.datastore.json.DetectionTypeJsonDatastore;
import edu.colorado.cires.pace.datastore.json.ExcelTranslatorJsonDatastore;
import edu.colorado.cires.pace.datastore.json.FileTypeJsonDatastore;
import edu.colorado.cires.pace.datastore.json.InstrumentJsonDatastore;
import edu.colorado.cires.pace.datastore.json.OrganizationJsonDatastore;
import edu.colorado.cires.pace.datastore.json.PackageJsonDatastore;
import edu.colorado.cires.pace.datastore.json.PersonJsonDatastore;
import edu.colorado.cires.pace.datastore.json.PlatformJsonDatastore;
import edu.colorado.cires.pace.datastore.json.ProjectJsonDatastore;
import edu.colorado.cires.pace.datastore.json.SeaJsonDatastore;
import edu.colorado.cires.pace.datastore.json.SensorJsonDatastore;
import edu.colorado.cires.pace.datastore.json.ShipJsonDatastore;
import edu.colorado.cires.pace.repository.CSVTranslatorRepository;
import edu.colorado.cires.pace.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.repository.ExcelTranslatorRepository;
import edu.colorado.cires.pace.repository.FileTypeRepository;
import edu.colorado.cires.pace.repository.InstrumentRepository;
import edu.colorado.cires.pace.repository.OrganizationRepository;
import edu.colorado.cires.pace.repository.PackageRepository;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.pace.repository.PlatformRepository;
import edu.colorado.cires.pace.repository.ProjectRepository;
import edu.colorado.cires.pace.repository.SeaRepository;
import edu.colorado.cires.pace.repository.SensorRepository;
import edu.colorado.cires.pace.repository.ShipRepository;
import edu.colorado.cires.pace.translator.DatasetType;
import edu.colorado.cires.pace.translator.LocationType;
import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

final class DataPanelFactory {

  private static final ApplicationPropertyResolver propertyResolver = new ApplicationPropertyResolver();
  private static final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();
  private static final ExcelTranslatorRepository excelTranslatorRepository;
  private static final CSVTranslatorRepository csvTranslatorRepository;
  private static final ProjectRepository projectRepository;
  private static final PersonRepository personRepository;
  private static final OrganizationRepository organizationRepository;
  private static final PlatformRepository platformRepository;
  private static final InstrumentRepository instrumentRepository;
  private static final SensorRepository sensorRepository;
  private static final DetectionTypeRepository detectionTypeRepository;
  private static final SeaRepository seaRepository;
  private static final ShipRepository shipRepository;
  private static final FileTypeJsonDatastore fileTypeJsonDatastore;
  private static final FileTypeRepository fileTypeRepository;

  static {
    try {
      excelTranslatorRepository = new ExcelTranslatorRepository(
          new ExcelTranslatorJsonDatastore(propertyResolver.getWorkDir(), objectMapper)
      );
      csvTranslatorRepository = new CSVTranslatorRepository(
          new CSVTranslatorJsonDatastore(propertyResolver.getWorkDir(), objectMapper)
      );
      fileTypeJsonDatastore = new FileTypeJsonDatastore(propertyResolver.getWorkDir(), objectMapper);
      fileTypeRepository = new FileTypeRepository(fileTypeJsonDatastore);
      projectRepository = new ProjectRepository(new ProjectJsonDatastore(propertyResolver.getWorkDir(), objectMapper));
      personRepository = new PersonRepository(new PersonJsonDatastore(propertyResolver.getWorkDir(), objectMapper));
      organizationRepository = new OrganizationRepository(new OrganizationJsonDatastore(propertyResolver.getWorkDir(), objectMapper));
      platformRepository = new PlatformRepository(new PlatformJsonDatastore(propertyResolver.getWorkDir(), objectMapper));
      instrumentRepository = new InstrumentRepository(new InstrumentJsonDatastore(propertyResolver.getWorkDir(), objectMapper), fileTypeJsonDatastore);
      sensorRepository = new SensorRepository(new SensorJsonDatastore(propertyResolver.getWorkDir(), objectMapper));
      detectionTypeRepository = new DetectionTypeRepository(new DetectionTypeJsonDatastore(propertyResolver.getWorkDir(), objectMapper));
      seaRepository = new SeaRepository(new SeaJsonDatastore(propertyResolver.getWorkDir(), objectMapper));
      shipRepository = new ShipRepository(new ShipJsonDatastore(propertyResolver.getWorkDir(), objectMapper));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static DataPanel<Project> createProjectsPanel() {
    return new MetadataPanel<>(
        projectRepository, new String[]{
        "UUID", "Name"
    }, (project -> new Object[] {
        project.getUuid(), project.getName()
    }), excelTranslatorRepository, csvTranslatorRepository, Project.class, (o) -> Project.builder()
        .uuid((UUID) o[0])
        .name((String) o[1])
        .build(), ProjectForm::new);
  }
  
  public static DataPanel<Platform> createPlatformPanel() {
    return new MetadataPanel<>(
        platformRepository,
        new String[]{"UUID", "Name"},
        (platform) -> new Object[]{platform.getUuid(), platform.getName()},
        excelTranslatorRepository,
        csvTranslatorRepository,
        Platform.class,
        (o) -> Platform.builder()
            .uuid((UUID) o[0])
            .name((String) o[1])
            .build(),
        PlatformForm::new
    );
  }
  
  public static DataPanel<FileType> createFileTypesPanel() {
    return new MetadataPanel<>(
        fileTypeRepository,
        new String[]{"UUID", "Type", "Comment"},
        (fileType) -> new Object[]{fileType.getUuid(), fileType.getType(), fileType.getComment()},
        excelTranslatorRepository,
        csvTranslatorRepository,
        FileType.class,
        (objects) -> FileType.builder()
            .uuid((UUID) objects[0])
            .type((String) objects[1])
            .comment((String) objects[2])
            .build(),
        FileTypeForm::new
    );
  }
  
  public static DataPanel<Person> createPeoplePanel() {
    return new MetadataPanel<>(
        personRepository,
        new String[]{"UUID", "Name", "Organization", "Position", "Street", "City", "State", "Zip", "Country", "Email",
            "Phone", "Orcid"},
        (person) -> new Object[]{person.getUuid(), person.getName(), person.getOrganization(), person.getPosition(),
            person.getStreet(), person.getCity(), person.getState(), person.getZip(), person.getCountry(), person.getEmail(),
            person.getPhone(), person.getOrcid()},
        excelTranslatorRepository,
        csvTranslatorRepository,
        Person.class,
        (objects) -> Person.builder()
            .uuid((UUID) objects[0])
            .name((String) objects[1])
            .organization((String) objects[2])
            .position((String) objects[3])
            .street((String) objects[4])
            .city((String) objects[5])
            .state((String) objects[6])
            .zip((String) objects[7])
            .country((String) objects[8])
            .email((String) objects[9])
            .phone((String) objects[10])
            .orcid((String) objects[11])
            .build(),
        PersonForm::new
    );
  }
  
  public static DataPanel<Organization> createOrganizationsPanel() {
    return new MetadataPanel<>(
        organizationRepository,
        new String[]{"UUID", "Name", "Street", "City", "State", "Zip", "Country", "Email", "Phone"},
        (person) -> new Object[]{person.getUuid(), person.getName(), person.getStreet(), person.getCity(),
            person.getState(), person.getZip(), person.getCountry(), person.getEmail(), person.getPhone()},
        excelTranslatorRepository,
        csvTranslatorRepository,
        Organization.class,
        (objects) -> Organization.builder()
            .uuid((UUID) objects[0])
            .name((String) objects[1])
            .street((String) objects[2])
            .city((String) objects[3])
            .state((String) objects[4])
            .zip((String) objects[5])
            .country((String) objects[6])
            .email((String) objects[7])
            .phone((String) objects[8])
            .build(),
        OrganizationForm::new
    );
  }
  
  public static DataPanel<Package> createPackagesPanel() throws IOException {
    return new PackagesPanel(
        new PackageRepository(new PackageJsonDatastore(propertyResolver.getWorkDir(), objectMapper)),
        new String[] { "UUID", "Package ID", "Dataset Type", "Location Type", "", "Object" },
        (p) -> new Object[] { p.getUuid(), p.getPackageId(), DatasetType.fromPackage(p).getName(), LocationType.fromLocationDetail(((Dataset) p).getLocationDetail()).getName(), false, p },
        excelTranslatorRepository,
        csvTranslatorRepository,
        Package.class,
        objectMapper,
        propertyResolver.getOutputDir(),
        projectRepository,
        personRepository,
        organizationRepository,
        platformRepository,
        instrumentRepository,
        sensorRepository,
        detectionTypeRepository,
        seaRepository,
        shipRepository
    );
  }
  
  public static DataPanel<ExcelTranslator> createExcelTranslatorsPanel() {
    return new TranslatorsPanel<>(
        excelTranslatorRepository,
        new String[] { "UUID", "Name", "Object" },
        (t) -> new Object[] { t.getUuid(), t.getName(), t },
        (o) -> (ExcelTranslator) o[2],
        ExcelTranslatorForm::new
    );
  }
  
  public static DataPanel<CSVTranslator> createCSVTranslatorsPanel() {
    return new TranslatorsPanel<>(
        csvTranslatorRepository,
        new String[] { "UUID", "Name", "Object" },
        (t) -> new Object[] { t.getUuid(), t.getName(), t },
        (o) -> (CSVTranslator) o[2],
        CSVTranslatorForm::new
    );
  }
  
  public static DataPanel<Instrument> createInstrumentsPanel() {
    return new MetadataPanel<>(
        instrumentRepository,
        new String[]{"UUID", "Name", "File Types", "Object"},
        (i) -> new Object[]{i.getUuid(), i.getName(), i.getFileTypes().stream()
            .map(FileType::getType)
            .collect(Collectors.joining(", ")), i},
        excelTranslatorRepository,
        csvTranslatorRepository,
        Instrument.class,
        (o) -> (Instrument) o[3],
        (i) -> new InstrumentForm(i, fileTypeRepository),
        fileTypeRepository
    );
  }
  
  public static DataPanel<Sensor> createSensorsPanel() {
    return new MetadataPanel<>(
        sensorRepository,
        new String[]{"UUID", "Name", "Position", "Description", "Object"},
        (s) -> new Object[]{s.getUuid(), s.getName(), String.format("(%s, %s, %s)", s.getPosition().getX(), s.getPosition().getY(), s.getPosition().getZ()), s.getDescription(), s},
        excelTranslatorRepository,
        csvTranslatorRepository,
        Sensor.class,
        (o) -> (Sensor) o[4],
        SensorForm::new
    );
  }

  public static DataPanel<Sea> createSeaAreasPanel() {
    return new MetadataPanel<>(
        seaRepository, new String[]{
        "UUID", "Name"
    }, (sea -> new Object[]{
        sea.getUuid(), sea.getName()
    }), excelTranslatorRepository, csvTranslatorRepository, Sea.class, (o) -> Sea.builder()
        .uuid((UUID) o[0])
        .name((String) o[1])
        .build(), SeaForm::new);
  }

}
