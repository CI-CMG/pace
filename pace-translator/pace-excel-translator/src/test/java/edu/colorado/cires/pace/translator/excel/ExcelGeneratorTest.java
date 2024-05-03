package edu.colorado.cires.pace.translator.excel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExcelGeneratorTest {
  
  private final ExcelGenerator generator = new ExcelGenerator("1.0");
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
    Path outputPath = testPath.resolve("test.xlsx");
    
    generator.generateSpreadsheet(outputPath, ExcelTranslator.builder()
            .fields(List.of(
                ExcelTranslatorField.builder()
                    .propertyName("camelCaseProperty")
                    .sheetNumber(1)
                    .columnNumber(1)
                    .build(),
                ExcelTranslatorField.builder()
                    .propertyName("camelCaseProperty.withNestedField")
                    .sheetNumber(1)
                    .columnNumber(2)
                    .build(),
                ExcelTranslatorField.builder()
                    .propertyName("camelCaseProperty[0].withNestedField")
                    .sheetNumber(2)
                    .columnNumber(1)
                    .build(),
                ExcelTranslatorField.builder()
                    .propertyName("camelCaseProperty[0].withNestedField[0].withAnotherNestedField")
                    .sheetNumber(2)
                    .columnNumber(2)
                    .build()
            ))
        .build());
    
    try (
        InputStream inputStream = new FileInputStream(outputPath.toFile()); 
        ReadableWorkbook workbook = new ReadableWorkbook(inputStream)
    ) {
      Sheet sheet = workbook.getSheet(0).orElseThrow();
      Row row = sheet.read().get(0);
      assertEquals(2, row.getPhysicalCellCount());
      assertEquals("Camel Case Property", row.getCellText(0));
      assertEquals("Camel Case Property With Nested Field", row.getCellText(1));

      sheet = workbook.getSheet(1).orElseThrow();
      row = sheet.read().get(0);
      assertEquals(2, row.getPhysicalCellCount());
      assertEquals("Camel Case Property (0) With Nested Field", row.getCellText(0));
      assertEquals("Camel Case Property (0) With Nested Field (0) With Another Nested Field", row.getCellText(1));
    }
  }
}