package edu.colorado.cires.pace.packaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Dataset;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
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
  
  public static Path writeMetadata(Dataset dataset, ObjectMapper objectMapper, Path targetDirectory) throws IOException {
    String metadata = objectMapper.writerWithView(Dataset.class).writeValueAsString(dataset);
    
    mkdir(targetDirectory);
    
    Path targetPath = targetDirectory.resolve(String.format(
        "%s.json", dataset.getPackageId()
    ));
    
    Files.writeString(targetPath, metadata, StandardCharsets.UTF_8);
    
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
