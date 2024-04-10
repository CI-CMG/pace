package edu.colorado.cires.pace.core.packaging;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class FileUtils {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

  public static void copyFile(Path source, Path target) throws IOException {
    if (!Files.exists(target.getParent())) {
      Files.createDirectories(target.getParent());
      LOGGER.debug("Created new directory: {}", target.getParent());
    }

    Files.copy(source, target);
    LOGGER.info("Copied {} to {}", source, target);
  }
  
  public static void appendChecksumToManifest(FileWriter fileWriter, Path path, Path manifestParentDirectory) throws IOException {
    String checksum;
    try (InputStream inputStream = new FileInputStream(path.toFile())) {
      checksum = DigestUtils.sha256Hex(inputStream);
      LOGGER.info("Computed checksum for {}", path);
    }

    fileWriter.append(String.format(
        "%s  %s\n", manifestParentDirectory.relativize(path), checksum
    ));
    LOGGER.info("Appended checksum for {} to manifest", path);
  }
  
  public static void mkdir(Path path) throws IOException {
    Files.createDirectory(path);
  }

  public static boolean filterHidden(Path path) throws IOException {
    boolean isProcessableFile = Files.isRegularFile(path) && !Files.isHidden(path);
    logIfFiltered(path, !isProcessableFile);
    return isProcessableFile;
  }

  public static boolean filterTimeSize(Path source, Path target) throws IOException {
    boolean isProcessableFile = !Files.exists(target) || Files.getLastModifiedTime(source).toMillis() != Files.getLastModifiedTime(target).toMillis() ||
        Files.size(source) != Files.size(target);
    logIfFiltered(source, !isProcessableFile);
    return isProcessableFile;
  }
  
  private static void logIfFiltered(Path path, boolean filtered) {
    if (filtered) {
      LOGGER.warn("Filtered {}", path);
    }
  }

}
