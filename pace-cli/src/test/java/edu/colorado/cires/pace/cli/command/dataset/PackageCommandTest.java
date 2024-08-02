package edu.colorado.cires.pace.cli.command.dataset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.TranslateCommandTest;
import edu.colorado.cires.pace.cli.error.ExecutionErrorHandler.CLIError;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DefaultTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.LocationDetailTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

abstract class PackageCommandTest<P extends Package, T extends PackageTranslator, L extends LocationDetailTranslator> extends TranslateCommandTest<P, T> {
  
  protected abstract void addPackageTypeSpecificFields(List<String> basePackageFields);
  protected abstract void addPackageTypeSpecificFields(List<String> fields, P object);
  protected abstract void addLocationDetailTypeSpecificFields(List<String> basePackageFields);
  protected abstract L createLocationDetailTranslator();
  protected abstract T createPackageTranslator(PackageTranslator packageTranslator);

  @Override
  protected String getSearchParameterArgumentName() {
    return "--package-ids";
  }

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
        "UUID",
        "dataCollectionName",
        "timeZone",
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
        .packageUUID("UUID")
        .dataCollectionName("dataCollectionName")
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
    List<String> fields = new ArrayList<>(List.of(
        object.getUuid() == null ? "" : object.getUuid().toString(),
        object.getDataCollectionName(),
        "UTC",
        object.getTemperaturePath().toString(),
        object.getBiologicalPath().toString(),
        object.getOtherPath().toString(),
        object.getDocumentsPath().toString(),
        object.getCalibrationDocumentsPath().toString(),
        object.getNavigationPath().toString(),
        object.getSourcePath().toString(),
        object.getSite(),
        object.getDeploymentId(),
        object.getDatasetPackager(),
        String.join(";", object.getProjectName()),
        object.getPublishDate().toString(),
        String.join(";", object.getScientists()),
        String.join(";", object.getSponsors()),
        String.join(";", object.getFunders()),
        object.getPlatformName(),
        object.getInstrumentType(),
        object.getStartTime().toString(),
        object.getEndTime().toString(),
        object.getPreDeploymentCalibrationDate().toString(),
        object.getPostDeploymentCalibrationDate().toString(),
        object.getCalibrationDescription(),
        object.getTitle(),
        object.getPurpose(),
        object.getDeploymentDescription(),
        object.getAlternateSiteName(),
        object.getDeploymentAlias()
    ));
    addPackageTypeSpecificFields(fields, object);
    addLocationDetailTypeSpecificFields(fields, object.getLocationDetail());
    return fields.toArray(String[]::new);
  }

  protected abstract void addLocationDetailTypeSpecificFields(List<String> fields, LocationDetail locationDetail);

  @Override
  protected String getRepositoryDirectory() {
    return "packages";
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

    assertEquals(expected.getSite(), actual.getSite());
    assertEquals(expected.getDeploymentId(), actual.getDeploymentId());
    assertEquals(expected.getDatasetPackager(), actual.getDatasetPackager());
    assertEquals(expected.getDataCollectionName(), actual.getDataCollectionName());

    for (int i = 0; i < expected.getProjectName().size(); i++) {
      assertEquals(
          expected.getProjectName().get(i),
          actual.getProjectName().get(i)
      );
    }
    
    assertEquals(expected.getPublishDate(), actual.getPublishDate());

    for (int i = 0; i < expected.getScientists().size(); i++) {
      assertEquals(
          expected.getScientists().get(i),
          actual.getScientists().get(i)
      );
    }

    for (int i = 0; i < expected.getSponsors().size(); i++) {
      assertEquals(
          expected.getSponsors().get(i),
          actual.getSponsors().get(i)
      );
    }

    for (int i = 0; i < expected.getFunders().size(); i++) {
      assertEquals(
          expected.getFunders().get(i),
          actual.getFunders().get(i)
      );
    }

    assertEquals(expected.getPlatformName(), actual.getPlatformName());
    assertEquals(expected.getInstrumentType(), actual.getInstrumentType());
    
    assertEquals(expected.getStartTime(), actual.getStartTime());
    assertEquals(expected.getEndTime(), actual.getEndTime());
    assertEquals(expected.getPreDeploymentCalibrationDate(), actual.getPreDeploymentCalibrationDate());
    assertEquals(expected.getPostDeploymentCalibrationDate(), actual.getPostDeploymentCalibrationDate());
    assertEquals(expected.getCalibrationDescription(), actual.getCalibrationDescription());
    assertEquals(expected.getTitle(), actual.getTitle());
    assertEquals(expected.getPurpose(), actual.getPurpose());
    assertEquals(expected.getDeploymentDescription(), actual.getDeploymentDescription());
    assertEquals(expected.getAlternateSiteName(), actual.getAlternateSiteName());
    assertEquals(expected.getDeploymentAlias(), actual.getDeploymentAlias());
   
    assertLocationDetailsEquals(expected.getLocationDetail(), actual.getLocationDetail());
    assertTypeSpecificPackagesEqual(expected, actual);
  }

  protected abstract void assertTypeSpecificPackagesEqual(P expected, P actual) throws JsonProcessingException;

  protected abstract void assertLocationDetailsEquals(LocationDetail expected, LocationDetail actual);

  @Override
  protected String getUniqueField(P object) {
    return object.getPackageId();
  }
  
  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void testPackage(boolean useSavedObject) throws IOException {

    Package p = writeObject(
        createObject("test")
    );
    clearOut();
    
    File packageFile = testPath.resolve("test.json").toFile();
    
    if (useSavedObject) {
      execute("package", "get-by-package-id", p.getUniqueField());
      p = objectMapper.readValue(getCommandOutput(), Package.class);
      clearOut();
      objectMapper.writeValue(packageFile, p);
    }
    
    createDirectoryAndWriteFile(p.getTemperaturePath());
    createDirectoryAndWriteFile(p.getBiologicalPath());
    createDirectoryAndWriteFile(p.getOtherPath());
    createDirectoryAndWriteFile(p.getDocumentsPath());
    createDirectoryAndWriteFile(p.getCalibrationDocumentsPath());
    createDirectoryAndWriteFile(p.getNavigationPath());
    createDirectoryAndWriteFile(p.getSourcePath());
    
    File outputDirectory = testPath.resolve("output").toFile();
    
    execute("package", "process", packageFile.toString(), outputDirectory.toString());

    Set<String> expectedPaths = Set.of(
      "target/test-dir/output/test/bagit.txt",
      "target/test-dir/output/test/bag-info.txt",
      "target/test-dir/output/test/process.log",
      "target/test-dir/output/test/tagmanifest-sha256.txt",
      "target/test-dir/output/test/data/nav_files/navigationPath.txt",
      "target/test-dir/output/test/data/calibration/calibrationDocumentsPath.txt",
      "target/test-dir/output/test/data/test.json",
      "target/test-dir/output/test/data/other/otherPath.txt",
      "target/test-dir/output/test/data/organizations.json",
      "target/test-dir/output/test/data/docs/documentsPath.txt",
      "target/test-dir/output/test/data/projects.json",
      "target/test-dir/output/test/data/acoustic_files/sourcePath.txt",
      "target/test-dir/output/test/data/biological/biologicalPath.txt",
      "target/test-dir/output/test/data/people.json",
      "target/test-dir/output/test/data/temperature/temperaturePath.txt",
      "target/test-dir/output/test/manifest-sha256.txt"
    );

    Set<String> actualPaths = Files.walk(outputDirectory.toPath())
        .filter(path -> path.toFile().isFile())
        .map(Path::toString)
        .collect(Collectors.toSet());
    
    assertEquals(expectedPaths, actualPaths);
    
    clearOut();

    execute(getCommandPrefix(), String.format(
        "get-by-%s", getUniqueFieldCommandSuffix()
    ), p.getUniqueField());

    String output = getCommandOutput();
    P object = objectMapper.readValue(output, getClazz());
    assertEquals(!useSavedObject, object.isVisible());
  }
  
  private void createDirectoryAndWriteFile(Path path) throws IOException {
    File directory = path.toFile();
    FileUtils.forceMkdir(directory);
    
    File file = path.resolve(String.format(
        "%s.txt", path.getFileName()
    )).toFile();
    
    FileUtils.writeStringToFile(file, "test", StandardCharsets.UTF_8);
  }
  
  @Test
  void testPackageError() throws IOException {
    Package p = writeObject(
        createObject("test")
    );
    createDirectoryAndWriteFile(p.getTemperaturePath());
    createDirectoryAndWriteFile(p.getBiologicalPath());
    createDirectoryAndWriteFile(p.getOtherPath());
    createDirectoryAndWriteFile(p.getDocumentsPath());
    createDirectoryAndWriteFile(p.getCalibrationDocumentsPath());
    createDirectoryAndWriteFile(p.getNavigationPath());

    File outputDirectory = testPath.resolve("output").toFile();
    clearOut();
    execute("package", "process", testPath.resolve("test.json").toFile().toString(), outputDirectory.toString());

    CLIError exception = getCLIException();
    assertEquals(String.format(
        "Failed to read file or directory: %s", p.getSourcePath().toAbsolutePath()
    ), exception.detail());
    assertEquals(String.format(
        "Failed to compute packaging destinations for %s", p.getSourcePath().toAbsolutePath()
    ), exception.message());
  }
  
  @Test
  void testCreateValidationException() throws IOException {
    P object = createObject("");
    execute(getCommandPrefix(), "list"); // initialize datastore
    clearOut();
    writeObject(object);

    CLIError exception = getCLIException();
    assertEquals(String.format(
        "%s validation failed", getClazz().getSimpleName()
    ), exception.message());

    ArrayList<?> detail = (ArrayList<?>) exception.detail();
    assertEquals(3, detail.size());
    
    Map<String, String> map = detail.stream()
        .map(o -> (Map<String, Object>) o)
        .collect(Collectors.toMap(
           d -> (String) d.get("field"),
           d -> (String) d.get("message") 
        ));
    
    assertEquals(3, map.keySet().size());
    
    assertEquals(
        Set.of("deploymentId","site","dataCollectionName"),
        map.keySet()
    );
    
    assertTrue(
        map.values().stream().allMatch(
            v -> v.equals("at least dataCollectionName, site, or deploymentId required")
        )
    );
  }
}