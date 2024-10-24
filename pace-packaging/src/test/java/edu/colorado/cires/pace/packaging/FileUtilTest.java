package edu.colorado.cires.pace.packaging;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
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
  void afterEach() {
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
    try {
      Files.setAttribute(path, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
    } catch (UnsupportedOperationException ignored) {} // not running on Windows OS
    assertFalse(FileUtils.filterHidden(path));
  }
  
  @Test
  void testFilterByChecksum() throws IOException {
    Path path = TEST_PATH.resolve("test.txt");
    org.apache.commons.io.FileUtils.createParentDirectories(path.toFile());
    Files.createFile(path);
    
    assertFalse(FileUtils.filterByChecksum(path, path)); // same file = fail
    
    Path path2 = TEST_PATH.resolve("test1.txt");
    assertTrue(FileUtils.filterByChecksum(path, path2)); // target does not exist = pass

    org.apache.commons.io.FileUtils.writeStringToFile(path2.toFile(), "Hello World", StandardCharsets.UTF_8);
    assertTrue(FileUtils.filterByChecksum(path, path2)); // size not equal = pass
  }

}
