package edu.colorado.cires.pace.translator.excel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.translator.ObjectWithRowConversionException;
import edu.colorado.cires.pace.translator.TranslatorValidationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExcelTranslatorExecutorTest {

  private final Path TEST_PATH = Path.of("target").resolve("test-dir");

  @BeforeEach
  void beforeEach() throws IOException {
    FileUtils.deleteQuietly(TEST_PATH.toFile());
    FileUtils.forceMkdir(TEST_PATH.toFile());
  }

  @AfterEach
  void afterEach() {
    FileUtils.deleteQuietly(TEST_PATH.toFile());
  }

  private ExcelTranslatorExecutor<Ship> createExecutor(ExcelTranslator translator) throws TranslatorValidationException {
    return new ExcelTranslatorExecutor<>(translator, Ship.class);
  }

  @Test
  void testTranslate() throws IOException, TranslatorValidationException {
    ExcelTranslator translator = ExcelTranslator.builder()
        .name("test")
        .fields(List.of(
            ExcelTranslatorField.builder()
                .propertyName("uuid")
                .columnNumber(1)
                .sheetNumber(1)
                .build(),
            ExcelTranslatorField.builder()
                .propertyName("name")
                .columnNumber(2)
                .sheetNumber(2)
                .build(),
            ExcelTranslatorField.builder()
                .propertyName("other")
                .columnNumber(3)
                .sheetNumber(3)
                .build()
        ))
        .build();

    Map<String, String> recordsMap = new HashMap<>(0);

    for (int i = 0; i < 10; i++) {
      recordsMap.put(
          UUID.randomUUID().toString(),
          String.format("name-%s", UUID.randomUUID())
      );
    }

    try (
        InputStream inputStream = new FileInputStream(prepareExcelFile(recordsMap))
    ) {
      Map<String, ObjectWithRowConversionException<Ship>> result = createExecutor(translator).translate(inputStream).collect(Collectors.toMap(
          ship -> ship.object().getUuid().toString(),
          ship -> ship
      ));
      assertEquals(recordsMap.size(), result.size()); // should not include empty rows

      for (Entry<String, String> entry : recordsMap.entrySet()) {
        ObjectWithRowConversionException<Ship> actual = result.get(entry.getKey());
        assertEquals(entry.getKey(), actual.object().getUuid().toString());
        assertEquals(entry.getValue(), actual.object().getName());
        assertNull(actual.rowConversionException());
      }
    }
  }

  @Test
  void testTranslateFromInputStreamNotImplemented() {
    ExcelTranslator translator = ExcelTranslator.builder()
        .name("test")
        .fields(List.of(
            ExcelTranslatorField.builder()
                .propertyName("uuid")
                .columnNumber(1)
                .sheetNumber(1)
                .build(),
            ExcelTranslatorField.builder()
                .propertyName("name")
                .columnNumber(2)
                .sheetNumber(2)
                .build()
        ))
        .build();

    NotImplementedException exception = assertThrows(NotImplementedException.class, () -> createExecutor(translator).translate((Reader) null));
    assertEquals("Reading excel sheets from Reader not implemented", exception.getMessage());
  }

  @Test
  void testStreamClosed() throws IOException {
    ExcelTranslator translator = ExcelTranslator.builder()
        .name("test")
        .fields(List.of(
            ExcelTranslatorField.builder()
                .propertyName("uuid")
                .columnNumber(1)
                .sheetNumber(1)
                .build(),
            ExcelTranslatorField.builder()
                .propertyName("name")
                .columnNumber(2)
                .sheetNumber(2)
                .build()
        ))
        .build();

    File file = TEST_PATH.resolve("test.txt").toFile();
    FileUtils.writeStringToFile(file, "test-data-1\ntest-data-2", StandardCharsets.UTF_8);

    InputStream inputStream;
    try (InputStream stream = new FileInputStream(file)) {
      inputStream = stream;
    }

    IOException exception = assertThrows(IOException.class, () -> createExecutor(translator).translate(inputStream).toList());
    assertEquals("Stream Closed", exception.getMessage());
  }

  private File prepareExcelFile(Map<String, String> recordsMap) throws IOException {
    File file = TEST_PATH.resolve("test.xlsx").toFile();
    
    try (
        OutputStream outputStream = new FileOutputStream(file);
        Workbook workbook = new Workbook(outputStream, "pace-test", "0.1")
    ) {
      Worksheet uuidSheet = workbook.newWorksheet("uuid-sheet");
      Worksheet nameSheet = workbook.newWorksheet("name-sheet");

      List<Entry<String, String>> entries = recordsMap.entrySet().stream().toList();
      for (int i = 0; i < entries.size(); i++) {
        Entry<String, String> entry = entries.get(i);
        uuidSheet.value(i, 0, entry.getKey());
        nameSheet.value(i, 1, entry.getValue());
      }
      // insert empty rows
      for (int i = entries.size(); i < entries.size() * 2; i++) {
        uuidSheet.value(i, 0,  (String) null);
        nameSheet.value(i, 1, (String) null);
      }
    }

    return file;
  }

}
