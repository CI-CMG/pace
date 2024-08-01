package edu.colorado.cires.pace.packaging;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import org.apache.logging.log4j.Logger;

class Packager {
  
  public static void run(Stream<PackageInstruction> moveInstructions, Path outputDir, Logger logger, ProgressIndicator... progressIndicators) throws PackagingException {
    mkdir(outputDir);
    logger.info("Created output directory: {}", outputDir);
    
    Runnable incrementProgressFn = () -> incrementProgress(progressIndicators);
    
    Path manifestFile = copyFilesAndWriteManifest(
        moveInstructions, outputDir, incrementProgressFn, logger
    );
    
    Path bagInfoFile = writeBagInfoFile(outputDir, incrementProgressFn, logger);
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
    Path outputFile = outputDir.resolve("tagmanifest-sha256.txt");
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
    Path outputFile = outputDir.resolve("manifest-sha256.txt");
    
    try (FileWriter writer = new FileWriter(outputFile.toFile(), StandardCharsets.UTF_8, true)) {
      moveInstructions
          .forEach(packageInstruction -> {
            try {
              FileUtils.copyFile(packageInstruction.source(), packageInstruction.target());
              logger.info("Copied {} to {}", packageInstruction.source(), packageInstruction.target());
              FileUtils.appendChecksumToManifest(writer, packageInstruction.target(), outputDir);
              logger.info("Appended {} checksum to {}", packageInstruction.target(), outputFile);
              
              incrementProgressFn.run();
            } catch (IOException e) {
              throw new RuntimeException(e);
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
  
  protected static Path writeBagInfoFile(Path outputDir, Runnable incrementProgressFn, Logger logger) throws PackagingException {
    Path bagInfoFile = outputDir.resolve("bag-info.txt");
    
    try (FileWriter writer = new FileWriter(bagInfoFile.toFile(), StandardCharsets.UTF_8, true)) {
      LocalDate localDate = LocalDate.now();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      String formattedString = localDate.format(formatter);
      writer.append(String.format(
         "Bagging-Date: %s", formattedString
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
