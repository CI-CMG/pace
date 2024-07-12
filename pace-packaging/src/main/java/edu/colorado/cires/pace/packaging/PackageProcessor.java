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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

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
      
      Configurator.reconfigure();

      ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
      builder.setStatusLevel(Level.INFO);
      builder.setConfigurationName(aPackage.getPackageId());

      AppenderComponentBuilder appenderComponentBuilder = builder.newAppender("PackageOut", "FILE")
          .addAttribute("fileName", outputDir.resolve(aPackage.getPackageId()).resolve("process.log"))
          .addAttribute("append", "true");

      appenderComponentBuilder.add(builder.newLayout("PatternLayout")
          .addAttribute("pattern", "%d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n"));

      RootLoggerComponentBuilder rootLoggerComponentBuilder = builder.newRootLogger(Level.INFO);
      rootLoggerComponentBuilder.add(builder.newAppenderRef("PackageOut"));

      builder.add(appenderComponentBuilder);
      builder.add(rootLoggerComponentBuilder);
      
      Configurator.reconfigure(builder.build());

      Logger logger = LogManager.getLogger(aPackage.getPackageId());

      processPackage(aPackage, packageOutputDir, logger);
    }

    Configurator.reconfigure();
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
      throws PackagingException, IOException {
    validatePackingJob(packingJob);
    
    Path outputDirectory = packageOutputDir.resolve("data");

    Stream<PackageInstruction> instructionStream = PackageInstructionFactory.getPackageInstructions(
        packingJob,
        FileUtils.writeMetadata((Dataset) packingJob, objectMapper, outputDirectory),
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
    return outputDir.resolve(((Dataset) packingJob).getPackageId());
  }

}
