package edu.colorado.cires.pace.packaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.dataset.base.DetailedPackage;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.codec.digest.DigestUtils;

final class FileUtils {

  public static void copyFile(Path source, Path target) throws IOException {
    Path parentPath = target.getParent();
    
    if (parentPath != null && !Files.exists(parentPath)) {
      Files.createDirectories(parentPath);
    }

    Files.copy(source, target);
  }
  
  public static void appendChecksumToManifest(FileWriter fileWriter, Path path, Path manifestParentDirectory) throws IOException {
    String checksum;
    try (InputStream inputStream = new FileInputStream(path.toFile())) {
      checksum = DigestUtils.sha256Hex(inputStream);
    }

    fileWriter.append(String.format(
        "%s  %s", manifestParentDirectory.relativize(path), checksum
    )).append("\n");
  }
  
  public static void mkdir(Path path) throws IOException {
    if (!path.toFile().exists()) {
      Files.createDirectory(path);
    }
  }

  public static boolean filterHidden(Path path) throws IOException {
    return Files.isRegularFile(path) && !Files.isHidden(path);
  }

  public static boolean filterTimeSize(Path source, Path target) throws IOException {
    return !Files.exists(target) || Files.getLastModifiedTime(source).toMillis() != Files.getLastModifiedTime(target).toMillis() ||
        Files.size(source) != Files.size(target);
  }
  
  public static Path writeMetadata(DetailedPackage aPackage, ObjectMapper objectMapper, Path targetDirectory) throws IOException {
    ObjectNode metadata = (ObjectNode) objectMapper
        .readTree(
            objectMapper.writeValueAsString(aPackage)
        );
    metadata.remove("uuid");
    metadata.remove("temperature_path");
    metadata.remove("biological_path");
    metadata.remove("other_path");
    metadata.remove("documents_path");
    metadata.remove("calibration_documents_path");
    metadata.remove("navigation_path");
    metadata.remove("source_path");
    
    mkdir(targetDirectory);
    
    Path targetPath = targetDirectory.resolve(String.format(
        "%s.json", aPackage.getPackageId()
    ));
    
    objectMapper.writerWithDefaultPrettyPrinter()
        .writeValue(targetPath.toFile(), metadata);
    
    return targetPath;
  }
  
  public static <O extends ObjectWithUniqueField> Path writeObjectsBlob(List<O> objects, ObjectMapper objectMapper, Path targetDirectory, String fileName) throws IOException {
    String jsonBlob = objectMapper.writeValueAsString(objects);
    
    mkdir(targetDirectory);
    
    Path targetPath = targetDirectory.resolve(fileName);
    
    Files.writeString(targetPath, jsonBlob, StandardCharsets.UTF_8);
    
    return targetPath;
  }

}
