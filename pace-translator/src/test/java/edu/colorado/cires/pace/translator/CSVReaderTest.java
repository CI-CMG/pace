package edu.colorado.cires.pace.translator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVReaderTest {
  
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
  void read() throws IOException {
    Path filePath = writeCSVFile();
    
    try (InputStream inputStream = new FileInputStream(filePath.toFile()); Reader reader = new InputStreamReader(inputStream)) {
      List<MapWithRowNumber> mapWithRowNumbers = CSVReader.read(reader)
          .sorted(Comparator.comparing(MapWithRowNumber::row))
          .toList();
      for (int i = 0; i < mapWithRowNumbers.size(); i++) {
        MapWithRowNumber mapWithRowNumber = mapWithRowNumbers.get(i);
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
  
  private Path writeCSVFile() throws IOException {
    CSVFormat format = CSVFormat.DEFAULT.builder()
        .setHeader(HEADERS)
        .build();
    
    Path filePath = testPath.resolve("test.csv");
    
    try (
        FileWriter fileWriter = new FileWriter(filePath.toFile());
        CSVPrinter printer = new CSVPrinter(fileWriter, format)
    ) {
      for (int i = 0; i < 100; i++) {
        printer.printRecord((Object[]) createRowValue(i));
      }
    }
    
    return filePath;
  }
  
  private String[] createRowValue(int i) {
    return new String[] {
        createCellValue("Value 1", i),
        createCellValue("Value 2", i),
        createCellValue("Value 3", i),
        createCellValue("Value 4", i)
    };
  }
  
  private String createCellValue(String prefix, int i) {
    return String.format(
        "%s - %s", prefix, i
    );
  }
}