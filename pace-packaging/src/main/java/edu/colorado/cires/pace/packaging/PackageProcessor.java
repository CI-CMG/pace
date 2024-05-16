package edu.colorado.cires.pace.packaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Dataset;
import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.object.Project;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class PackageProcessor {
  
  private final ObjectMapper objectMapper;
  private final Validator validator;
  private final List<Person> people;
  private final List<Organization> organizations;
  private final List<Project> projects;
  private final ProgressIndicator[] progressIndicators;
  private final List<Package> packages;
  private final Path outputDir;

  public PackageProcessor(ObjectMapper objectMapper, List<Person> people, List<Organization> organizations, List<Project> projects,
      List<Package> packages, Path outputDir,
      ProgressIndicator... progressIndicators) {
    this.objectMapper = objectMapper;
    this.people = Collections.unmodifiableList(people);
    this.organizations = Collections.unmodifiableList(organizations);
    this.projects = Collections.unmodifiableList(projects);
    this.packages = packages;
    this.outputDir = outputDir;
    this.progressIndicators = progressIndicators;
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
  }
  
  public void process() throws IOException, PackagingException {
    FileUtils.mkdir(outputDir);

    for (Package aPackage : packages) {
      FileUtils.mkdir(getPackageOutputDir(aPackage));
    }

    new Thread(this::initializeProgressIndicators).start();

    for (Package aPackage : packages) {
      Path packageOutputDir = getPackageOutputDir(aPackage);

      processPackage(aPackage, packageOutputDir);
    }
  }
  
  private void initializeProgressIndicators() {
    try {
      for (Package aPackage : packages) {
        Path packageOutputDir = getPackageOutputDir(aPackage);
        long instructionCount = PackageInstructionFactory.getInstructionCount(aPackage, packageOutputDir);
        for (ProgressIndicator progressIndicator : progressIndicators) {
          progressIndicator.setTotalRecords(
              progressIndicator.getTotalRecords() + instructionCount 
          );
        }
      }
    } catch (IOException | PackagingException exception) {
      throw new RuntimeException("Failed to establish total records for packaging job");
    }
  }

  private void processPackage(Package packingJob, Path packageOutputDir)
      throws PackagingException, IOException {
    validatePackingJob(packingJob);
    
    Path outputDirectory = packageOutputDir.resolve("data");

    Stream<PackageInstruction> instructionStream = PackageInstructionFactory.getPackageInstructions(
        packingJob,
        FileUtils.writeMetadata((Dataset) packingJob, objectMapper, outputDirectory),
        FileUtils.writeObjectsBlob(people, objectMapper, outputDirectory, "people.json"),
        FileUtils.writeObjectsBlob(organizations, objectMapper, outputDirectory, "organizations.json"),
        FileUtils.writeObjectsBlob(projects, objectMapper, outputDirectory, "projects.json"),
        packageOutputDir
    );
    
    Packager.run(
        instructionStream,
        packageOutputDir,
        progressIndicators
    );
  }
  
  private void validatePackingJob(Package packingJob) {
    Set<ConstraintViolation<Package>> violations = validator.validate(packingJob);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(String.format(
          "%s validation failed", Package.class.getSimpleName()
      ), violations);
    }
  }
  
  private Path getPackageOutputDir(Package packingJob) throws IOException {
    return outputDir.resolve(((Dataset) packingJob).getPackageId());
  }

}
