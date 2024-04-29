package edu.colorado.cires.pace.packaging;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Packager {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(Packager.class);
  
  public static void run(Stream<PackageInstruction> moveInstructions, Path outputDir, ProgressIndicator... progressIndicators) throws PackagingException {
    mkdir(outputDir);
    
    Runnable incrementProgressFn = () -> incrementProgress(progressIndicators);
    
    Path manifestFile = copyFilesAndWriteManifest(
        moveInstructions, outputDir, incrementProgressFn
    );
    
    Path bagInfoFile = writeBagInfoFile(outputDir, incrementProgressFn);
    Path bagitFile = writeBagItFile(outputDir, incrementProgressFn);
    
    writeTagManifestFile(
        bagInfoFile, bagitFile, manifestFile, outputDir, incrementProgressFn
    );
  }
  
  private static void incrementProgress(ProgressIndicator... progressIndicators) {
    for (ProgressIndicator progressIndicator : progressIndicators) {
      progressIndicator.incrementProcessedRecords();
    }
  }

  protected static void writeTagManifestFile(Path bagInfoFile, Path bagitFile, Path manifestFile, Path outputDir, Runnable incrementProgressFn) throws PackagingException {
    Path outputFile = outputDir.resolve("tagmanifest-sha256.txt");
    try (FileWriter writer = new FileWriter(outputFile.toFile(), StandardCharsets.UTF_8, true)) {
      FileUtils.appendChecksumToManifest(writer, bagInfoFile, outputDir);
      FileUtils.appendChecksumToManifest(writer, bagitFile, outputDir);
      FileUtils.appendChecksumToManifest(writer, manifestFile, outputDir);
      
      incrementProgressFn.run();
    } catch (IOException e) {
      throw new PackagingException(String.format(
          "Failed to write %s", outputFile
      ), e);
    }
  }

  protected static Path copyFilesAndWriteManifest(Stream<PackageInstruction> moveInstructions, Path outputDir, Runnable incrementProgressFn) throws PackagingException {
    Path outputFile = outputDir.resolve("manifest-sha256.txt");
    
    try (FileWriter writer = new FileWriter(outputFile.toFile(), StandardCharsets.UTF_8, true)) {
      RuntimeException exception = moveInstructions
          .map(packageInstruction -> {
            try {
              FileUtils.copyFile(packageInstruction.source(), packageInstruction.target());
              FileUtils.appendChecksumToManifest(writer, packageInstruction.target(), outputDir);
              
              incrementProgressFn.run();
              return null;
            } catch (IOException e) {
              return new RuntimeException(e);
            }
          }).filter(Objects::nonNull)
          .reduce(new RuntimeException("Packing failed"), (o1, o2) -> {
            o1.addSuppressed(o2);
            return o1;
          });
      if (exception.getSuppressed().length != 0) {
        throw new PackagingException(
            exception.getMessage(),
            exception.getSuppressed()
        );
      }
      
    } catch (IOException e) {
      throw new PackagingException(String.format(
          "Failed to write %s", outputFile
      ), e);
    }
    
    LOGGER.info("Wrote manifest file to {}", outputFile);
    
    incrementProgressFn.run();
    
    return outputFile;
  }
  
  protected static Path writeBagInfoFile(Path outputDir, Runnable incrementProgressFn) throws PackagingException {
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
    
    LOGGER.info("Wrote bag info file {}", bagInfoFile);
    
    incrementProgressFn.run();
    
    return bagInfoFile;
  }
  
  protected static Path writeBagItFile(Path outputDir, Runnable incrementProgressFn) throws PackagingException {
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
    
    LOGGER.info("Wrote bagit file {}", bagitFile);
    
    incrementProgressFn.run();
    
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
