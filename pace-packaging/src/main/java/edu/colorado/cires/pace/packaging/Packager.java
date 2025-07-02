package edu.colorado.cires.pace.packaging;

import edu.colorado.cires.pace.data.object.contact.person.Person;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.passivePacker.data.PassivePackerPerson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.TimeUnit;

/**
 * Packager provides the functionality for packaging data and
 * creating checksums for packages
 */
class Packager {

  /**
   * Packages data
   * @param moveInstructions stream of instructions to run for manifest
   * @param outputDir location to place files
   * @param logger logs outcomes of running
   * @param progressIndicators tracks progress of packaging
   * @throws PackagingException thrown in case of error to indicate packaging error
   */
  public static void run(Stream<PackageInstruction> moveInstructions, Path outputDir, List<Package> packages, List<Person> people, Logger logger, ProgressIndicator... progressIndicators) throws PackagingException {
    mkdir(outputDir);
    logger.info("Created output directory: {}", outputDir);
    
    Runnable incrementProgressFn = () -> incrementProgress(progressIndicators);
    
    Path manifestFile = copyFilesAndWriteManifest(
        moveInstructions, outputDir, incrementProgressFn, logger
    );
    
    Path bagInfoFile = writeBagInfoFile(outputDir, incrementProgressFn, logger, packages, people);
    Path bagitFile = writeBagItFile(outputDir, incrementProgressFn, logger);
    
    writeTagManifestFile(
        bagInfoFile, bagitFile, manifestFile, outputDir, incrementProgressFn, logger
    );
    
    logger.info("Package processing complete: {}", outputDir);
  }
  
  private static void incrementProgress(ProgressIndicator... progressIndicators) {
    for (ProgressIndicator progressIndicator : progressIndicators) {
      progressIndicator.incrementProcessedRecords();
    }
  }

  protected static void writeTagManifestFile(Path bagInfoFile, Path bagitFile, Path manifestFile, Path outputDir, Runnable incrementProgressFn, Logger logger) throws PackagingException {
    Path outputFile = outputDir.resolve("tagmanifest-md5.txt");
    try (FileWriter writer = new FileWriter(outputFile.toFile(), StandardCharsets.UTF_8, true)) {
      FileUtils.appendChecksumToManifest(writer, bagInfoFile, outputDir);
      logger.info("Appended {} checksum to {}", bagInfoFile, outputFile);
      FileUtils.appendChecksumToManifest(writer, bagitFile, outputDir);
      logger.info("Appended {} checksum to {}", bagitFile, outputFile);
      FileUtils.appendChecksumToManifest(writer, manifestFile, outputDir);
      logger.info("Appended {} checksum to {}", manifestFile, outputFile);
      
      incrementProgressFn.run();
      
    } catch (IOException e) {
      throw new PackagingException(String.format(
          "Failed to write %s", outputFile
      ), e);
    }
    logger.info("Wrote {}", outputFile);
  }

  protected static Path copyFilesAndWriteManifest(Stream<PackageInstruction> moveInstructions, Path outputDir, Runnable incrementProgressFn, Logger logger) throws PackagingException {
    Path outputFile = outputDir.resolve("manifest-md5.txt");
    File outputFileActual = new File(String.valueOf(outputFile));
    if (outputFileActual.exists()) {
      boolean deleted = outputFileActual.delete();
      if (!deleted) {
        throw new PackagingException("failed to delete" + outputFile.toString(), new Throwable("deletion failed"));
      }
    }
    
    try (FileWriter writer = new FileWriter(outputFile.toFile(), StandardCharsets.UTF_8, true)) {
      moveInstructions
          .filter(packageInstruction -> (!packageInstruction.target().toString().contains("acoustic_files/") || !packageInstruction.target().toString().contains("acoustic_files\\"))
              || isAudioFile(packageInstruction.target().getFileName()))
          .forEach(packageInstruction -> {
            int i = 0;
            while(true){
              IOException output;
              try {
                if (FileUtils.filterByChecksum(packageInstruction.source(), packageInstruction.target())){
                  output = copyFileAttempt(packageInstruction, logger);
                } else {
                  logger.warn("Identical file already exists: {}", packageInstruction.target());
                  output = null;
                }
              } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
              }
              if (output == null){
                break;
              } else {
                i++;
                if (i == 5){
                  throw new RuntimeException(output);
                }
              }
            }
            i = 0;
            while(true){
              IOException output;
              try {
                output = appendToManifestAttempt(packageInstruction, logger, writer,
                    outputDir, outputFile, incrementProgressFn);
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
              if (output == null){
                break;
              } else {
                i++;
                if (i == 5){
                  throw new RuntimeException(output);
                }
              }
            }
          });

    } catch (IOException | RuntimeException e) {
      throw new PackagingException(String.format(
          "Packaging failed: %s", e.getMessage()
      ), e);
    }
    
    incrementProgressFn.run();
    
    logger.info("Wrote {}", outputFile);
    
    return outputFile;
  }

  private static IOException appendToManifestAttempt(PackageInstruction packageInstruction, Logger logger, FileWriter writer, Path outputDir,
      Path outputFile, Runnable incrementProgressFn) throws InterruptedException {
    try {
      FileUtils.appendChecksumToManifest(writer, packageInstruction.target(), outputDir);
      logger.info("Appended {} checksum to {}", packageInstruction.target(), outputFile);

      incrementProgressFn.run();

      return null;
    } catch (IOException e) {
      TimeUnit.SECONDS.sleep(1);
      return e;
    }
  }

  private static IOException copyFileAttempt(PackageInstruction packageInstruction, Logger logger) throws InterruptedException {
    try {
      FileUtils.copyFile(packageInstruction.source(), packageInstruction.target());
      logger.info("Copied {} to {}", packageInstruction.source(), packageInstruction.target());
      return null;
    } catch (IOException e) {
      TimeUnit.SECONDS.sleep(1);
      return e;
    }
  }

  private static boolean isAudioFile(Path fileName) {
    return fileName.toString().endsWith(".aif") ||
        fileName.toString().endsWith(".wav") ||
        fileName.toString().endsWith(".flac") ||
        fileName.toString().endsWith(".aiff");
  }

  protected static Path writeBagInfoFile(Path outputDir, Runnable incrementProgressFn, Logger logger, List<Package> packages, List<Person> people) throws PackagingException {
    Path bagInfoFile = outputDir.resolve("bag-info.txt");
    
    try (FileWriter writer = new FileWriter(bagInfoFile.toFile(), StandardCharsets.UTF_8, true)) {
      LocalDate localDate = LocalDate.now();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      String formattedDateString = localDate.format(formatter);
      String packager = packages.get(0).getDatasetPackager();
      Person person = null;
      for (Person p : people) {
        if (p.getName().equals(packager)){
          person = p;
        }
      }

      writer.append(String.format(
          "Source-Organization: %s%n", packages.get(0).getSources().get(0)
      ));
      writer.append(String.format(
         "Bagging-Date: %s%n", formattedDateString
      ));
      writer.append(String.format(
          "Contact-Name: %s%n", packager
      ));
      writer.append(String.format(
          "Contact-Phone: %s%n", person.getPhone()
      ));
      writer.append(String.format(
          "Contact-Email: %s%n", person.getEmail()
      ));
      writer.append(String.format(
          "External-Description: %s%n", packages.get(0).getDeploymentTitle()
      ));
      writer.append(String.format(
          "External-Identifier: %s%n", packages.get(0).getDataCollectionName()
      ));
      writer.append(String.format(
          "Packager version: %s", "PACE"
      ));
    } catch (IOException e) {
      throw new PackagingException(String.format(
          "Failed to write %s", bagInfoFile
      ), e);
    }
    
    incrementProgressFn.run();
    
    logger.info("Wrote {}", bagInfoFile);
    
    return bagInfoFile;
  }
  
  protected static Path writeBagItFile(Path outputDir, Runnable incrementProgressFn, Logger logger) throws PackagingException {
    Path bagitFile = outputDir.resolve("bagit.txt");
    
    try (FileWriter writer = new FileWriter(bagitFile.toFile(), StandardCharsets.UTF_8, true)) {
      writer.append("BagIt-Version: 0.97\n");
      writer.append(String.format(
          "Tag-File-Character-Encoding: %s", StandardCharsets.UTF_8.displayName()
      ));
    } catch (IOException e) {
      throw new PackagingException(String.format(
          "Failed to write %s", bagitFile
      ), e);
    }
    
    incrementProgressFn.run();
    
    logger.info("Wrote {}", bagitFile);
    
    return bagitFile;
  }
  
  protected static void mkdir(Path path) throws PackagingException {
    try {
      FileUtils.mkdir(path);
    } catch (IOException e) {
      throw new PackagingException(String.format(
          "Failed to create directory: %s", path
      ), e);
    }
  }

}
