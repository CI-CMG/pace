package edu.colorado.cires.pace.translator;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.TabularTranslationField;
import edu.colorado.cires.pace.data.object.TabularTranslator;
import jakarta.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpreadsheetGeneratorTest {
  
  private static class TestTranslatorField implements TabularTranslationField {
    
    private final String propertyName;
    private final int columnNumber;

    private TestTranslatorField(String propertyName, int columnNumber) {
      this.propertyName = propertyName;
      this.columnNumber = columnNumber;
    }

    @Override
    public String getPropertyName() {
      return propertyName;
    }

    @Override
    public Integer getColumnNumber() {
      return columnNumber;
    }
  }
  
  private static final class TestTranslator implements TabularTranslator<TestTranslatorField> {
    
    private final List<TestTranslatorField> fields;

    private TestTranslator(List<TestTranslatorField> fields) {
      this.fields = fields;
    }

    @Override
    public List<@Valid TestTranslatorField> getFields() {
      return fields;
    }

    @Override
    public String getName() {
      return "test";
    }

    @Override
    public UUID getUuid() {
      return null;
    }
  }
  
  private static class TestGenerator extends SpreadsheetGenerator<TestTranslatorField, TestTranslator> {

    @Override
    protected void writeFieldsToSpreadsheet(OutputStream outputStream, List<TestTranslatorField> translatorFields) throws IOException {
      String[] headerNames = getHeaderNames(translatorFields);
      
      try (OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
        for (String headerName : headerNames) {
          writer.write(String.format(
              "%s\n", headerName
          ));
        }
      }
    }
  }
  
  private final Path testPath = Paths.get("target").resolve("test-dir");
  private final TestGenerator generator = new TestGenerator();
  
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
    Path outputFile = testPath.resolve("test.txt");
    generator.generateSpreadsheet(outputFile, new TestTranslator(List.of(
        new TestTranslatorField("camelCaseProperty", 1),
        new TestTranslatorField("camelCaseProperty.withNestedField", 2),
        new TestTranslatorField("camelCaseProperty[0].withNestedField", 3),
        new TestTranslatorField("camelCaseProperty[0].withNestedField[0].withAnotherNestedField", 4)
    )));
    
    assertEquals(List.of(
      "Camel Case Property",
      "Camel Case Property With Nested Field",
      "Camel Case Property (0) With Nested Field",
      "Camel Case Property (0) With Nested Field (0) With Another Nested Field"
    ), FileUtils.readLines(outputFile.toFile(), StandardCharsets.UTF_8));
  }
}