package edu.colorado.cires.pace.packaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.contact.organization.Organization;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.contact.person.Person;
import edu.colorado.cires.pace.data.object.project.Project;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.NotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.WriterAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 * PackageProcessor takes in packages and relevant data, processes
 * the data, and then writes it to the correct location
 */
public class PackageProcessor {
  
  private final ObjectMapper objectMapper;
  private final Validator validator;
  private final List<Person> people;
  private final List<Organization> organizations;
  private final List<Project> projects;
  private final ProgressIndicator[] progressIndicators;
  private final List<Package> packages;
  private final Path outputDir;
  private final PassivePackerFactory passivePackerFactory;

  /**
   * Creates a package processor
   * @param objectMapper builds json object out of object data
   * @param people list of relevant person objects
   * @param organizations list of relevant organization objects
   * @param projects list of relevant project objects
   * @param packages list of packages to process
   * @param outputDir location to output processed files
   * @param passivePackerFactory creates passive packer schema out of package data
   * @param progressIndicators tracks the progress of processing the packages
   */
  public PackageProcessor(ObjectMapper objectMapper, List<Person> people, List<Organization> organizations, List<Project> projects,
      List<Package> packages, Path outputDir, PassivePackerFactory passivePackerFactory,
      ProgressIndicator... progressIndicators) {
    this.objectMapper = objectMapper;
    this.people = Collections.unmodifiableList(people);
    this.organizations = Collections.unmodifiableList(organizations);
    this.projects = Collections.unmodifiableList(projects);
    this.packages = packages;
    this.outputDir = outputDir;
    this.passivePackerFactory = passivePackerFactory;
    this.progressIndicators = progressIndicators;
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  /**
   * Processes the package and places the output in the
   * relevant output directory
   * @return List of processed packages
   * @throws IOException thrown in case of error accessing file structure
   * @throws PackagingException thrown in case of error during packaging
   * @throws NotFoundException thrown in case of uuid missing
   * @throws DatastoreException thrown in case of error accessing datastore
   */
  public List<Package> process() throws IOException, PackagingException, NotFoundException, DatastoreException {
    FileUtils.mkdir(outputDir);

    for (Package aPackage : packages) {
      FileUtils.mkdir(getPackageOutputDir(aPackage));
    }

    new Thread(this::initializeProgressIndicators).start();

    List<Package> processedPackages = new ArrayList<>(0);
    
    for (Package aPackage : packages) {
      Path packageOutputDir = getPackageOutputDir(aPackage);

      WriterAppender writerAppender = WriterAppender.newBuilder()
          .setName(aPackage.getPackageId())
          .setLayout(PatternLayout.createDefaultLayout())
          .setTarget(new FileWriter(outputDir.resolve(aPackage.getPackageId()).resolve("process.log").toFile(), StandardCharsets.UTF_8))
          .build();
      writerAppender.start();

      Logger logger = (Logger) LogManager.getLogger("edu.colorado.cires.pace");
      logger.addAppender(writerAppender);
      processPackage(aPackage, packageOutputDir, logger);
      logger.removeAppender(writerAppender);
      
      processedPackages.add(aPackage.setVisible(false));
    }
    
    return processedPackages;
  }
  
  private void initializeProgressIndicators() {
    try {
      for (Package aPackage : packages) {
        Path packageOutputDir = getPackageOutputDir(aPackage);
        long instructionCount = PackageInstructionFactory.getInstructionCount(aPackage, packageOutputDir, LogManager.getLogger(aPackage.getPackageId()));
        for (ProgressIndicator progressIndicator : progressIndicators) {
          progressIndicator.setTotalRecords(
              progressIndicator.getTotalRecords() + instructionCount 
          );
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to establish total records for packaging job", e);
    } catch (PackagingException e) {
      throw new RuntimeException("Failed to establish total records for packaging job", e.getCause());
    }
  }

  private void processPackage(Package packingJob, Path packageOutputDir, Logger logger)
      throws PackagingException, IOException, NotFoundException, DatastoreException {
    validatePackingJob(packingJob);
    
    Path outputDirectory = packageOutputDir.resolve("data");

    Stream<PackageInstruction> instructionStream = PackageInstructionFactory.getPackageInstructions(
        packingJob,
        FileUtils.writeMetadata(passivePackerFactory.createPackage(packingJob), outputDirectory),
        FileUtils.writeObjectsBlob(people, objectMapper, outputDirectory, "people.json"),
        FileUtils.writeObjectsBlob(organizations, objectMapper, outputDirectory, "organizations.json"),
        FileUtils.writeObjectsBlob(projects, objectMapper, outputDirectory, "projects.json"),
        packageOutputDir,
        logger
    );
    
    Packager.run(
        instructionStream,
        packageOutputDir,
        logger,
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
    return outputDir.resolve(packingJob.getPackageId());
  }

}
