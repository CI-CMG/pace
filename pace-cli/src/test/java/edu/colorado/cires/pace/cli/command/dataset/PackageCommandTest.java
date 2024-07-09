package edu.colorado.cires.pace.cli.command.dataset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.TranslateCommandTest;
import edu.colorado.cires.pace.cli.command.instrument.InstrumentCommandTest;
import edu.colorado.cires.pace.cli.command.organization.OrganizationCommandTest;
import edu.colorado.cires.pace.cli.command.person.PersonCommandTest;
import edu.colorado.cires.pace.cli.command.platform.PlatformCommandTest;
import edu.colorado.cires.pace.cli.command.project.ProjectCommandTest;
import edu.colorado.cires.pace.data.object.Dataset;
import edu.colorado.cires.pace.data.object.LocationDetail;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.translator.DateTranslator;
import edu.colorado.cires.pace.data.translator.DefaultTimeTranslator;
import edu.colorado.cires.pace.data.translator.LocationDetailTranslator;
import edu.colorado.cires.pace.data.translator.PackageTranslator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

abstract class PackageCommandTest<P extends Package, T extends PackageTranslator, L extends LocationDetailTranslator> extends TranslateCommandTest<P, T> {
  
  protected abstract void addPackageTypeSpecificFields(List<String> basePackageFields);
  protected abstract void addPackageTypeSpecificFields(List<String> fields, P object);
  protected abstract void addLocationDetailTypeSpecificFields(List<String> basePackageFields);
  protected abstract L createLocationDetailTranslator();
  protected abstract T createPackageTranslator(PackageTranslator packageTranslator);
  
  protected <O extends ObjectWithUniqueField> O saveObject(O object, String commandPrefix) throws IOException {
    String fileName = String.format("test-%s.json", UUID.randomUUID());
    File file = testPath.resolve(fileName).toFile();
    objectMapper.writeValue(file, object);
    
    execute(commandPrefix, "create", file.toString());
    
    O created = (O) objectMapper.readValue(getCommandOutput(), object.getClass());
    
    clearOut();
    
    return created;
  }

  @Override
  protected String getUniqueFieldCommandSuffix() {
    return "package-id";
  }

  @Override
  protected String[] getTranslatorFields() {
    List<String> fields = new ArrayList<>(List.of(
        "timeZone",
        "packageUUID",
        "temperaturePath",
        "biologicalPath",
        "otherPath",
        "documentsPath",
        "calibrationDocumentsPath",
        "navigationPath",
        "sourcePath",
        "siteOrCruiseName",
        "deploymentId",
        "datasetPackager",
        "projects",
        "publicReleaseDate",
        "scientists",
        "sponsors",
        "funders",
        "platform",
        "instrument",
        "startTime",
        "endTime",
        "preDeploymentCalibrationDate",
        "postDeploymentCalibrationDate",
        "calibrationDescription",
        "deploymentTitle",
        "deploymentPurpose",
        "deploymentDescription",
        "alternateSiteName",
        "alternateDeploymentName"
    ));
    addPackageTypeSpecificFields(fields);
    addLocationDetailTypeSpecificFields(fields);
    return fields.toArray(String[]::new);
  }

  @Override
  protected T createTranslator(String name) {
    PackageTranslator packageTranslator = PackageTranslator.builder()
        .name(name)
        .packageUUID("packageUUID")
        .temperaturePath("temperaturePath")
        .biologicalPath("biologicalPath")
        .otherPath("otherPath")
        .documentsPath("documentsPath")
        .calibrationDocumentsPath("calibrationDocumentsPath")
        .navigationPath("navigationPath")
        .sourcePath("sourcePath")
        .siteOrCruiseName("siteOrCruiseName")
        .deploymentId("deploymentId")
        .datasetPackager("datasetPackager")
        .projects("projects")
        .publicReleaseDate(DateTranslator.builder()
            .timeZone("timeZone")
            .date("publicReleaseDate")
            .build())
        .scientists("scientists")
        .sponsors("sponsors")
        .funders("funders")
        .platform("platform")
        .instrument("instrument")
        .startTime(DefaultTimeTranslator.builder()
            .timeZone("timeZone")
            .time("startTime")
            .build())
        .endTime(DefaultTimeTranslator.builder()
            .timeZone("timeZone")
            .time("endTime")
            .build())
        .preDeploymentCalibrationDate(DateTranslator.builder()
            .timeZone("timeZone")
            .date("preDeploymentCalibrationDate")
            .build())
        .postDeploymentCalibrationDate(DateTranslator.builder()
            .timeZone("timeZone")
            .date("postDeploymentCalibrationDate")
            .build())
        .calibrationDescription("calibrationDescription")
        .deploymentTitle("deploymentTitle")
        .deploymentPurpose("deploymentPurpose")
        .deploymentDescription("deploymentDescription")
        .alternateSiteName("alternateSiteName")
        .alternateDeploymentName("alternateDeploymentName")
        .locationDetailTranslator(createLocationDetailTranslator())
        .build(); 
    return createPackageTranslator(packageTranslator);
  }

  @Override
  protected String[] objectToRow(P object) {
    Dataset dataset = (Dataset) object;
    List<String> fields = new ArrayList<>(List.of(
        "UTC",
        object.getUuid() == null ? "" : object.getUuid().toString(),
        object.getTemperaturePath().toString(),
        object.getBiologicalPath().toString(),
        object.getOtherPath().toString(),
        object.getDocumentsPath().toString(),
        object.getCalibrationDocumentsPath().toString(),
        object.getNavigationPath().toString(),
        object.getSourcePath().toString(),
        dataset.getSiteOrCruiseName(),
        dataset.getDeploymentId(),
        dataset.getDatasetPackager().getName(),
        dataset.getProjects().stream()
            .map(Project::getName)
            .collect(Collectors.joining(";")),
        dataset.getPublicReleaseDate().toString(),
        dataset.getScientists().stream()
            .map(Person::getName)
            .collect(Collectors.joining(";")),
        dataset.getSponsors().stream()
            .map(Organization::getName)
            .collect(Collectors.joining(";")),
        dataset.getFunders().stream()
            .map(Organization::getName)
            .collect(Collectors.joining(";")),
        dataset.getPlatform().getName(),
        dataset.getInstrument().getName(),
        dataset.getStartTime().toString(),
        dataset.getEndTime().toString(),
        dataset.getPreDeploymentCalibrationDate().toString(),
        dataset.getPostDeploymentCalibrationDate().toString(),
        dataset.getCalibrationDescription(),
        dataset.getDeploymentTitle(),
        dataset.getDeploymentPurpose(),
        dataset.getDeploymentDescription(),
        dataset.getAlternateSiteName(),
        dataset.getAlternateDeploymentName()
    ));
    addPackageTypeSpecificFields(fields, object);
    addLocationDetailTypeSpecificFields(fields, ((Dataset) object).getLocationDetail());
    return fields.toArray(String[]::new);
  }

  protected abstract void addLocationDetailTypeSpecificFields(List<String> fields, LocationDetail locationDetail);

  @Override
  protected String getRepositoryFileName() {
    return "packages.json";
  }

  @Override
  protected String getCommandPrefix() {
    return "package";
  }

  @Override
  protected TypeReference<List<P>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Class<P> getClazz() {
    return (Class<P>) Package.class;
  }

  @Override
  protected String getUniqueFieldName() {
    return "packageId";
  }

  @Override
  protected void assertObjectsEqual(P expected, P actual, boolean checkUUID) throws JsonProcessingException {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }
    
    assertEquals(expected.getTemperaturePath(), actual.getTemperaturePath());
    assertEquals(expected.getBiologicalPath(), actual.getBiologicalPath());
    assertEquals(expected.getOtherPath(), actual.getOtherPath());
    assertEquals(expected.getDocumentsPath(), actual.getDocumentsPath());
    assertEquals(expected.getCalibrationDocumentsPath(), actual.getCalibrationDocumentsPath());
    assertEquals(expected.getNavigationPath(), actual.getNavigationPath());
    assertEquals(expected.getSourcePath(), actual.getSourcePath());
    assertEquals(expected.getPackageId(), actual.getPackageId());
    
    Dataset expectedDataset = (Dataset) expected;
    Dataset actualDataset = (Dataset) actual;
    assertEquals(expectedDataset.getSiteOrCruiseName(), actualDataset.getSiteOrCruiseName());
    assertEquals(expectedDataset.getDeploymentId(), actualDataset.getDeploymentId());
    PersonCommandTest.assertPeopleEqual(expectedDataset.getDatasetPackager(), actualDataset.getDatasetPackager(), true);

    for (int i = 0; i < expectedDataset.getProjects().size(); i++) {
      ProjectCommandTest.assertProjectsEqual(
          expectedDataset.getProjects().get(i),
          actualDataset.getProjects().get(i),
          true
      );
    }
    
    assertEquals(expectedDataset.getPublicReleaseDate(), actualDataset.getPublicReleaseDate());

    for (int i = 0; i < expectedDataset.getScientists().size(); i++) {
      PersonCommandTest.assertPeopleEqual(
          expectedDataset.getScientists().get(i),
          actualDataset.getScientists().get(i),
          true
      );
    }

    for (int i = 0; i < expectedDataset.getSponsors().size(); i++) {
      OrganizationCommandTest.assertOrganizationsEqual(
          expectedDataset.getSponsors().get(i),
          actualDataset.getSponsors().get(i),
          true
      );
    }

    for (int i = 0; i < expectedDataset.getFunders().size(); i++) {
      OrganizationCommandTest.assertOrganizationsEqual(
          expectedDataset.getFunders().get(i),
          actualDataset.getFunders().get(i),
          true
      );
    }

    PlatformCommandTest.assertPlatformsEqual(expectedDataset.getPlatform(), actualDataset.getPlatform(), true);
    InstrumentCommandTest.assertInstrumentsEqual(expectedDataset.getInstrument(), actualDataset.getInstrument(), true);
    
    assertEquals(expectedDataset.getStartTime(), actualDataset.getStartTime());
    assertEquals(expectedDataset.getEndTime(), actualDataset.getEndTime());
    assertEquals(expectedDataset.getPreDeploymentCalibrationDate(), actualDataset.getPreDeploymentCalibrationDate());
    assertEquals(expectedDataset.getPostDeploymentCalibrationDate(), actualDataset.getPostDeploymentCalibrationDate());
    assertEquals(expectedDataset.getCalibrationDescription(), actualDataset.getCalibrationDescription());
    assertEquals(expectedDataset.getDeploymentTitle(), actualDataset.getDeploymentTitle());
    assertEquals(expectedDataset.getDeploymentPurpose(), actualDataset.getDeploymentPurpose());
    assertEquals(expectedDataset.getDeploymentDescription(), actualDataset.getDeploymentDescription());
    assertEquals(expectedDataset.getAlternateSiteName(), actualDataset.getAlternateSiteName());
    assertEquals(expectedDataset.getAlternateDeploymentName(), actualDataset.getAlternateDeploymentName());
   
    assertLocationDetailsEquals(expectedDataset.getLocationDetail(), actualDataset.getLocationDetail());
    assertTypeSpecificPackagesEqual(expected, actual);
  }

  protected abstract void assertTypeSpecificPackagesEqual(P expected, P actual) throws JsonProcessingException;

  protected abstract void assertLocationDetailsEquals(LocationDetail expected, LocationDetail actual);

  @Override
  protected String getUniqueField(P object) {
    return object.getPackageId();
  }
}