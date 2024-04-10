package edu.colorado.cires.pace.core.packaging;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileUtilTest {
  
  private static final Path TEST_PATH = Paths.get("target").resolve("test");
  
  @BeforeEach
  void beforeEach() {
    org.apache.commons.io.FileUtils.deleteQuietly(TEST_PATH.toFile());
  }
  
  @AfterEach
  void afterEach() throws IOException {
    org.apache.commons.io.FileUtils.deleteQuietly(TEST_PATH.toFile());
  }
  
  @Test
  void testFilterHidden() throws IOException {
    Path path = TEST_PATH.resolve("test.txt");
    org.apache.commons.io.FileUtils.createParentDirectories(path.toFile());
    Files.createFile(path);
    
    assertTrue(FileUtils.filterHidden(path)); // regular, visible file = pass
    assertFalse(FileUtils.filterHidden(path.getParent())); // directory = fail
    
    path = TEST_PATH.resolve(".hidden").resolve(".test.txt"); // hidden file = fail
    org.apache.commons.io.FileUtils.createParentDirectories(path.toFile());
    Files.createFile(path);
    assertFalse(FileUtils.filterHidden(path));
  }
  
  @Test
  void testFilterTimeSize() throws IOException, InterruptedException {
    Path path = TEST_PATH.resolve("test.txt");
    org.apache.commons.io.FileUtils.createParentDirectories(path.toFile());
    Files.createFile(path);
    
    assertFalse(FileUtils.filterTimeSize(path, path)); // same file = fail
    
    Path path2 = TEST_PATH.resolve("test1.txt");
    assertTrue(FileUtils.filterTimeSize(path, path2)); // target does not exist = pass

    Thread.sleep(10);    
    org.apache.commons.io.FileUtils.createParentDirectories(path2.toFile());
    Files.createFile(path2);
    assertTrue(FileUtils.filterTimeSize(path, path2)); // modified times not equal = pass

    org.apache.commons.io.FileUtils.writeStringToFile(path2.toFile(), "Hello World", StandardCharsets.UTF_8);
    assertTrue(FileUtils.filterTimeSize(path, path2)); // size not equal = pass
  }

}
