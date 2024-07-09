package edu.colorado.cires.pace.translator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExcelReaderTest {

  private final Path testPath = Paths.get("target").resolve("test-dir");

  private final String[] HEADERS = new String[] {
      "HEADER 1", "HEADER 2", "HEADER 3", "HEADER 4"
  };

  @BeforeEach
  void beforeEach() throws IOException {
    FileUtils.deleteQuietly(testPath.toFile());
    FileUtils.forceMkdir(testPath.toFile());
  }

  @AfterEach
  void afterEach() {
    FileUtils.deleteQuietly(testPath.toFile());
  }
  
  @Test
  void testRead() throws IOException {
    Path filePath = writeExcelFile();
    
    try (InputStream inputStream = new FileInputStream(filePath.toFile())) {
      List<MapWithRowNumber> results = ExcelReader.read(inputStream, 0)
          .sorted(Comparator.comparing(MapWithRowNumber::row))
          .toList();
      for (int i = 0; i < results.size(); i++) {
        MapWithRowNumber mapWithRowNumber = results.get(i);
        Map<String, ValueWithColumnNumber> map = mapWithRowNumber.map();
        int row = mapWithRowNumber.row();
        assertEquals(i + 2, row);
        assertEquals(Arrays.stream(HEADERS).collect(Collectors.toSet()), map.keySet());
        assertEquals(HEADERS.length, map.keySet().size());
        List<ValueWithColumnNumber> values = map.values().stream()
            .sorted(Comparator.comparing(ValueWithColumnNumber::column))
            .toList();
        for (int i1 = 0; i1 < values.size(); i1++) {
          ValueWithColumnNumber valueWithColumnNumber = values.get(i1);
          Optional<String> val = valueWithColumnNumber.value();
          assertTrue(val.isPresent());
          assertEquals(
              String.format(
                  "Value %s - %s", i1 + 1, i
              ),
              val.get()
          );
          assertEquals(i1, valueWithColumnNumber.column());
        }
      }
    }
  }
  
  private Path writeExcelFile() throws IOException {
    Path filePath = testPath.resolve("test.xlsx");
    try (OutputStream outputStream = new FileOutputStream(filePath.toFile()); Workbook workbook = new Workbook(outputStream, "test", "1.0")) {
      Worksheet worksheet = workbook.newWorksheet("test");
      
      for (int i = 0; i < 4; i++) {
        worksheet.value(0, i, HEADERS[i]);
      }
      
      for (int i = 1; i < 101; i++) {
        for (int j = 0; j < 4; j++) {
          worksheet.value(i, j, String.format(
              "Value %s - %s", j + 1, i - 1
          ));
        }
      }
    }
    
    return filePath;
  }

}