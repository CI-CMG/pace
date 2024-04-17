package edu.colorado.cires.pace.translator.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.CSVTranslatorField;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.translator.TranslationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVTranslatorExecutorTest {
  
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
  
  private CSVTranslatorExecutor<Ship> createExecutor(CSVTranslator translator) throws TranslationException {
    return new CSVTranslatorExecutor<>(translator, Ship.class);
  }
  
  @Test
  void testTranslate() throws IOException {
    CSVTranslator translator = CSVTranslator.builder()
        .name("test")
        .fields(List.of(
            CSVTranslatorField.builder()
                .propertyName("uuid")
                .columnNumber(1)
                .build(),
            CSVTranslatorField.builder()
                .propertyName("name")
                .columnNumber(2)
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
        InputStream inputStream = new FileInputStream(prepareCSVFile(recordsMap));
        Reader reader = new InputStreamReader(inputStream)
    ) {
      Map<String, Ship> result = createExecutor(translator).translate(reader).collect(Collectors.toMap(
          ship -> ship.getUuid().toString(),
          ship -> ship
      ));
      
      for (Entry<String, String> entry : recordsMap.entrySet()) {
        Ship actual = result.get(entry.getKey());
        assertEquals(entry.getKey(), actual.getUuid().toString());
        assertEquals(entry.getValue(), actual.getName());
      }
    }
  }
  
  @Test
  void testTranslateFromInputStreamNotImplemented() {
    CSVTranslator translator = CSVTranslator.builder()
        .name("test")
        .fields(List.of(
            CSVTranslatorField.builder()
                .propertyName("uuid")
                .columnNumber(1)
                .build(),
            CSVTranslatorField.builder()
                .propertyName("name")
                .columnNumber(2)
                .build()
        ))
        .build();
    
    NotImplementedException exception = assertThrows(NotImplementedException.class, () -> createExecutor(translator).translate((InputStream) null));
    assertEquals("Reading CSV rows from InputStream not implemented", exception.getMessage());
  }
  
  @Test
  void testStreamClosed() throws IOException {
    CSVTranslator translator = CSVTranslator.builder()
        .name("test")
        .fields(List.of(
            CSVTranslatorField.builder()
                .propertyName("uuid")
                .columnNumber(1)
                .build(),
            CSVTranslatorField.builder()
                .propertyName("name")
                .columnNumber(2)
                .build()
        ))
        .build();
    
    File file = TEST_PATH.resolve("test.txt").toFile();
    FileUtils.writeStringToFile(file, "test-data-1\ntest-data-2", StandardCharsets.UTF_8);
    
    InputStream inputStream;
    try (InputStream stream = new FileInputStream(file)) {
      inputStream = stream;
    }
    
    try (
        Reader reader = new InputStreamReader(inputStream)
    ) {
      TranslationException exception = assertThrows(TranslationException.class, () -> createExecutor(translator).translate(reader).toList());
      assertEquals("Translation failed", exception.getMessage());
      assertInstanceOf(IOException.class, exception.getCause());
    }
  }
  
  private File prepareCSVFile(Map<String, String> recordsMap) throws IOException {
    CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
        .setHeader("uuid","name")
        .build();
    
    File file = TEST_PATH.resolve("test.csv").toFile();
    
    try (
        OutputStream outputStream = new FileOutputStream(file);
        Writer writer = new OutputStreamWriter(outputStream);
        CSVPrinter printer = new CSVPrinter(writer, csvFormat)
    ) {
      for (Entry<String, String> entry : recordsMap.entrySet()) {
        printer.printRecord(entry.getKey(), entry.getValue());
      }
    }
    
    return file;
  }
}
