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
import java.nio.file.Paths;
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

  public PackageProcessor(ObjectMapper objectMapper, List<Person> people, List<Organization> organizations, List<Project> projects) {
    this.objectMapper = objectMapper;
    this.people = Collections.unmodifiableList(people);
    this.organizations = Collections.unmodifiableList(organizations);
    this.projects = Collections.unmodifiableList(projects);
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  public void process(Package packingJob, Path outputDir, ProgressIndicator... progressIndicators)
      throws PackagingException, IOException {
    validatePackingJob(packingJob);
    
    FileUtils.mkdir(outputDir);
    Path packageOutputDir = outputDir.resolve(((Dataset) packingJob).getPackageId());
    FileUtils.mkdir(packageOutputDir);

    Stream<PackageInstruction> instructionStream = PackageInstructionFactory.getPackageInstructions(
        packingJob,
        Paths.get("metadata"),
        Paths.get("people"),
        Paths.get("organizations"),
        Paths.get("projects"),
        packageOutputDir
    );
    long totalRecords = instructionStream.count() + 4L; // accounting for generated files

    for (ProgressIndicator progressIndicator : progressIndicators) {
      progressIndicator.setTotalRecords(totalRecords);
    }
    
    Path outputDirectory = packageOutputDir.resolve("data");

    instructionStream = PackageInstructionFactory.getPackageInstructions(
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

}
