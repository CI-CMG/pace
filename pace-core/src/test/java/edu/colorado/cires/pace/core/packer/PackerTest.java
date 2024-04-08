package edu.colorado.cires.pace.core.packer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import edu.colorado.cires.pace.core.exception.PackingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class PackerTest {
  
  private final Path SOURCE_DIR = Paths.get("target").resolve("source").toAbsolutePath();
  private final Path TARGET_DIR = Paths.get("target").resolve("target").toAbsolutePath();
  
  @BeforeEach
  void beforeEach() throws IOException {
    FileUtils.deleteQuietly(SOURCE_DIR.toFile());
    FileUtils.deleteQuietly(TARGET_DIR.toFile());
    
    FileUtils.forceMkdir(SOURCE_DIR.toFile());
    
    writeDir();
  }
  
  @AfterEach
  void afterEach() {
    FileUtils.deleteQuietly(SOURCE_DIR.toFile());
    FileUtils.deleteQuietly(TARGET_DIR.toFile());
  }
  
  private void writeDir() throws IOException {
    for (int i = 0; i < 10; i++) {
      String fileName = String.format(
          "test-%s.txt", i
      );
      try (FileWriter writer = new FileWriter(SOURCE_DIR.resolve(fileName).toFile(), StandardCharsets.UTF_8, true)) {
        for (int j = 0; j < 10; j++) {
          writer.append(String.format(
              "test-content\t%s%s", i, j
          ));
        }
      }
    }
  }
  
  private Stream<PackageInstruction> getInstructionForSourceDir() throws IOException {
    return Files.walk(SOURCE_DIR)
        .filter(Files::isRegularFile)
        .map(p -> new PackageInstruction(
            p,
            TARGET_DIR.resolve("data").resolve(SOURCE_DIR.relativize(p))
        ));
  }
  
  @Test
  void testRun() throws IOException, PackingException {
    List<PackageInstruction> packageInstructions = getInstructionForSourceDir().toList();
    
    Packer.run(packageInstructions.stream(), TARGET_DIR);
    
    Path bagitFile = TARGET_DIR.resolve("bagit.txt");
    List<String> lines = FileUtils.readLines(bagitFile.toFile(), StandardCharsets.UTF_8);
    assertEquals(2, lines.size());
    assertEquals("BagIt-Version: 0.97", lines.get(0));
    assertEquals(String.format(
        "Tag-File-Character-Encoding: %s", StandardCharsets.UTF_8.displayName()
    ), lines.get(1));
    
    Path bagInfoFile = TARGET_DIR.resolve("bag-info.txt");
    lines = FileUtils.readLines(bagInfoFile.toFile(), StandardCharsets.UTF_8);
    assertEquals(1, lines.size());
    
    LocalDate localDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedString = localDate.format(formatter);
    assertEquals(
        String.format(
            "Bagging-Date: %s", formattedString
        ),
        lines.get(0)
    );
    
    Path manifestFile = TARGET_DIR.resolve("manifest-sha256.txt");
    lines = FileUtils.readLines(manifestFile.toFile(), StandardCharsets.UTF_8);
    assertEquals(10, lines.size());
    
    for (String line : lines) {
      String[] lineParts = line.split(" {2}");
      String relativePath = lineParts[0];
      String checksum = lineParts[1];
      
      assertTrue(relativePath.startsWith("data"));
      
      Path targetFile = TARGET_DIR.resolve(relativePath);
      assertTrue(targetFile.toFile().exists());
      assertTrue(targetFile.toFile().isFile());
      
      Path sourceFile = SOURCE_DIR.resolve(targetFile.toFile().getName());
      assertTrue(sourceFile.toFile().exists());
      assertTrue(sourceFile.toFile().isFile());
      
      try (InputStream inputStream = new FileInputStream(sourceFile.toFile())) {
        String expectedChecksum = DigestUtils.sha256Hex(inputStream);
        assertEquals(expectedChecksum, checksum);
      }
    }
    
    Path tagmanifestFile = TARGET_DIR.resolve("tagmanifest-sha256.txt");
    lines = FileUtils.readLines(tagmanifestFile.toFile(), StandardCharsets.UTF_8);
    assertEquals(3, lines.size());
    
    for (String line : lines) {
      String[] lineParts = line.split(" {2}");
      String relativePath = lineParts[0];
      String checksum = lineParts[1];
      
      assertFalse(relativePath.contains(File.separator));
      
      Path targetFile = TARGET_DIR.resolve(relativePath);
      assertTrue(targetFile.toFile().exists());
      assertTrue(targetFile.toFile().isFile());
      
      Path sourceFile = SOURCE_DIR.resolve(targetFile.toFile().getName());
      assertFalse(sourceFile.toFile().exists());
      
      try (InputStream inputStream = new FileInputStream(targetFile.toFile())) {
        String expectedChecksum = DigestUtils.sha256Hex(inputStream);
        assertEquals(expectedChecksum, checksum);
      }
    }
  }
  
  @Test
  void testMkdirDirectoryExists() throws IOException {
    FileUtils.forceMkdir(TARGET_DIR.toFile());

    Exception exception = assertThrows(PackingException.class, () -> Packer.mkdir(TARGET_DIR));
    assertEquals(String.format(
        "Failed to create directory: %s", TARGET_DIR
    ), exception.getMessage());
  }
  
  @Test
  void testWriteBagitFileDirectoryDoesNotExist() {
    Exception exception = assertThrows(PackingException.class, () -> Packer.writeBagItFile(TARGET_DIR));
    assertEquals(String.format(
        "Failed to write %s", TARGET_DIR.resolve("bagit.txt")
    ), exception.getMessage());
  }
  
  @Test
  void testWriteBagInfoFileDirectoryDoesNotExist() {
    Exception exception = assertThrows(PackingException.class, () -> Packer.writeBagInfoFile(TARGET_DIR));
    assertEquals(String.format(
        "Failed to write %s", TARGET_DIR.resolve("bag-info.txt")
    ), exception.getMessage());
  }
  
  @Test
  void testWriteTagManifestFileDoesNotExist() throws PackingException {
    Packer.mkdir(TARGET_DIR);
    Exception exception = assertThrows(PackingException.class, () -> Packer.writeTagManifestFile(
        TARGET_DIR.resolve("bag-info.txt"),
        TARGET_DIR.resolve("bagit.txt"),
        TARGET_DIR.resolve("manifest-sha256.txt"),
        TARGET_DIR
    ));
    assertEquals(String.format(
        "Failed to write %s", TARGET_DIR.resolve("tagmanifest-sha256.txt")
    ), exception.getMessage());
  }
  
  @Test
  void testFileProcessingFailure() throws IOException {
    try (MockedStatic<edu.colorado.cires.pace.core.util.FileUtils> mockedStatic = Mockito.mockStatic(edu.colorado.cires.pace.core.util.FileUtils.class)) {
      mockedStatic.when(() -> edu.colorado.cires.pace.core.util.FileUtils.appendChecksumToManifest(any(), any(), any())).thenThrow(
          new IOException("test file error")
      );
      mockedStatic.when(() -> edu.colorado.cires.pace.core.util.FileUtils.mkdir(any())).thenCallRealMethod();

      List<PackageInstruction> packageInstructions = getInstructionForSourceDir().toList();

      Exception exception = assertThrows(PackingException.class, () -> Packer.run(packageInstructions.stream(), TARGET_DIR));
      assertEquals("Packing failed", exception.getMessage());
      for (Throwable throwable : exception.getSuppressed()) {
        assertEquals("java.io.IOException: test file error", throwable.getMessage());
      }
    }
  }
  
  @Test
  void testWriteManifestDirectoryDoesNotExist() {
    Exception exception = assertThrows(PackingException.class, () -> Packer.copyFilesAndWriteManifest(Stream.empty(), TARGET_DIR));
    assertEquals(String.format(
        "Failed to write %s", TARGET_DIR.resolve("manifest-sha256.txt")
    ), exception.getMessage());
  }

}