package edu.colorado.cires.pace.packaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import edu.colorado.cires.passivePacker.data.PassivePackerPackage;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * FileUtils provides functions for packaging
 */
public class FileUtils {

  /**
   * Moves a file from the source to the target location
   * @param source location to pull file from
   * @param target location to push file to
   * @throws IOException thrown in case of error moving file
   */
  public static void copyFile(Path source, Path target) throws IOException {
    Path parentPath = target.getParent();
    
    if (parentPath != null && !Files.exists(parentPath)) {
      Files.createDirectories(parentPath);
    }

    Files.copy(source, target);
  }

  /**
   * Creates checksum based on file data and appends it to packaging directory
   * @param fileWriter creates a file
   * @param path location to create checksum within
   * @param manifestParentDirectory location to place checksum
   * @throws IOException thrown in case of checksum creation and movement
   */
  public static void appendChecksumToManifest(FileWriter fileWriter, Path path, Path manifestParentDirectory) throws IOException {
    String checksum;
    try (InputStream inputStream = new FileInputStream(path.toFile())) {
      checksum = computeChecksum(inputStream);
    }

    fileWriter.append(String.format(
        "%s  %s", manifestParentDirectory.relativize(path), checksum
    )).append("\n");
  }

  /**
   * Creates directory at provided location
   * @param path location to build directory
   * @throws IOException thrown in case of error making directory
   */
  public static void mkdir(Path path) throws IOException {
    if (!path.toFile().exists()) {
      Files.createDirectory(path);
    }
  }

  /**
   * Returns whether the provided file is hidden
   * @param path file to check
   * @return boolean indicating if file is hidden
   * @throws IOException thrown in case of file access error
   */
  public static boolean filterHidden(Path path) throws IOException {
    return Files.isRegularFile(path) && !Files.isHidden(path);
  }

  /**
   * Checks the equality of checksums
   * @param source location of first file
   * @param target location of second file
   * @return boolean indicating the quality of the checksums of the two files
   * @throws IOException thrown in case of error accessing files
   */
  public static boolean filterByChecksum(Path source, Path target) throws IOException {
    if (Files.exists(target)) {
      try (InputStream sourceStream = new FileInputStream(source.toFile()); InputStream targetStream = new FileInputStream(target.toFile())) {
        return !computeChecksum(sourceStream).equals(computeChecksum(targetStream));
      }
    }
    
    return true;
  }
  
  private static String computeChecksum(InputStream inputStream) throws IOException {
    return DigestUtils.sha256Hex(inputStream);
  }

  /**
   * Creates metadata file for package
   * @param aPackage package to create metadata file of
   * @param targetDirectory location to place metadata file
   * @return Path target path
   * @throws IOException thrown in case of file access error
   */
  public static Path writeMetadata(PassivePackerPackage aPackage, Path targetDirectory) throws IOException {
    ObjectMapper objectMapper = SerializationUtils.createObjectMapper()
        .setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_SNAKE_CASE)
        .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    ObjectNode metadata = (ObjectNode) objectMapper
        .readTree(
            objectMapper.writeValueAsString(aPackage)
        );
    metadata.remove("UUID");
    metadata.remove("TEMPERATURE_PATH");
    metadata.remove("BIOLOGICAL_PATH");
    metadata.remove("OTHER_PATH");
    metadata.remove("DOCUMENTS_PATH");
    metadata.remove("CALIBRATION_DOCUMENTS_PATH");
    metadata.remove("NAVIGATION_PATH");
    metadata.remove("SOURCE_PATH");
    
    ObjectNode deployment = (ObjectNode) metadata.get("DEPLOYMENT");
    if (deployment != null) {
      ObjectNode location = (ObjectNode) deployment.get("LOCATION");
      if (location != null) {
        JsonNode files = location.get("FILES");
        if (files != null) {
          location.remove("FILES");
        }
      }
    }
    
    mkdir(targetDirectory);
    
    Path targetPath = targetDirectory.resolve(String.format(
        "%s.json", aPackage.getDataCollectionName()
    ));
    
    objectMapper.writerWithDefaultPrettyPrinter()
        .writeValue(targetPath.toFile(), metadata);
    
    return targetPath;
  }

  /**
   * Writes object blobs to specified location
   * @param objects list of objects to write to file
   * @param objectMapper maps object to file format
   * @param targetDirectory location to place blob
   * @param fileName name of file output
   * @return Path target path
   * @param <O> object type
   * @throws IOException thrown in case of file access errors
   */
  public static <O extends ObjectWithUniqueField> Path writeObjectsBlob(List<O> objects, ObjectMapper objectMapper, Path targetDirectory, String fileName) throws IOException {
    String jsonBlob = objectMapper.writeValueAsString(objects);
    
    mkdir(targetDirectory);
    
    Path targetPath = targetDirectory.resolve(fileName);
    
    Files.writeString(targetPath, jsonBlob, StandardCharsets.UTF_8);
    
    return targetPath;
  }

}
