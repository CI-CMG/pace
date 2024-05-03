package edu.colorado.cires.pace.translator.csv;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.CSVTranslatorField;
import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVGeneratorTest {

  private final CSVGenerator generator = new CSVGenerator();
  private final Path testPath = Paths.get("target").resolve("test-dir");

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
  void generateSpreadsheet() throws IOException {
    Path outputPath = testPath.resolve("test.csv");
    
    generator.generateSpreadsheet(outputPath, CSVTranslator.builder()
            .fields(List.of(
                CSVTranslatorField.builder()
                    .propertyName("camelCaseProperty")
                    .columnNumber(1)
                    .build(),
                CSVTranslatorField.builder()
                    .propertyName("camelCaseProperty.withNestedField")
                    .columnNumber(2)
                    .build(),
                CSVTranslatorField.builder()
                    .propertyName("camelCaseProperty[0].withNestedField")
                    .columnNumber(3)
                    .build(),
                CSVTranslatorField.builder()
                    .propertyName("camelCaseProperty[0].withNestedField[0].withAnotherNestedField")
                    .columnNumber(4)
                    .build()
            ))
        .build());
    
    List<String> csvLines = FileUtils.readLines(outputPath.toFile(), StandardCharsets.UTF_8);
    assertEquals(1, csvLines.size());

    assertEquals(List.of(
      "Camel Case Property",
      "Camel Case Property With Nested Field",
      "Camel Case Property (0) With Nested Field",
      "Camel Case Property (0) With Nested Field (0) With Another Nested Field"
    ), Arrays.asList(csvLines.get(0).split(",")));
  }
}