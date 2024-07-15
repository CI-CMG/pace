package edu.colorado.cires.pace.cli.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.colorado.cires.pace.cli.error.ExecutionErrorHandler.CLIError;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.translator.ProjectTranslator;
import edu.colorado.cires.pace.data.translator.Translator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.junit.jupiter.api.Test;

public abstract class TranslateCommandTest<O extends ObjectWithUniqueField, T extends Translator> extends CRUDCommandTest<O> {
  
  protected abstract String[] getTranslatorFields();
  protected abstract T createTranslator(String name);
  protected abstract String[] objectToRow(O object);
  
  private void writeTranslator(Translator translator) throws IOException {
    File file = testPath.resolve("test-translator.json").toFile();
    objectMapper.writeValue(file, translator);
    execute("translator", "create", file.toString());
  }
  
  private void writeExcel(String[] translatorFields, File file, boolean generateInvalidUUID) throws IOException {
    try (OutputStream outputStream = new FileOutputStream(file); Workbook workbook = new Workbook(outputStream, "test", "1.0")) {
      Worksheet worksheet = workbook.newWorksheet("test");

      for (int i = 0; i < translatorFields.length; i++) {
        worksheet.value(0, i, translatorFields[i]);
      }

      for (int i = 1; i < 101; i++) {
        String[] rowData = objectToRow(
            createObject(String.format(
                "unique-field-%s", i
            ))
        );
        if (generateInvalidUUID) {
          rowData[0] = "invalid-uuid";
        }
        for (int j = 0; j < rowData.length; j++) {
          worksheet.value(i, j, rowData[j]);
        }
      }
    }
  }
  
  private void writeCSV(String[] translatorFields, File file, boolean generateInvalidUUID) throws IOException {
    CSVFormat format = CSVFormat.DEFAULT.builder()
        .setHeader(translatorFields)
        .build();

    try (
        FileWriter fileWriter = new FileWriter(file);
        CSVPrinter printer = new CSVPrinter(fileWriter, format)
    ) {
      for (int i = 0; i < 100; i++) {
        Object[] row = objectToRow(
            createObject(String.format(
                "unique-field-%s", i
            ))
        );
        
        if (generateInvalidUUID) {
          row[0] = "bad-uuid";
        }
        
        printer.printRecord(
            row
        );
      }
    }
  }
  
  @Test
  void testTranslateCSV() throws IOException {
    File file = testPath.resolve("test.csv").toFile();
    writeCSV(getTranslatorFields(), file, false);
    
    String translatorName = "test-translator";
    writeTranslator(
        createTranslator(translatorName)
    );

    clearOut();
    execute(getCommandPrefix(), "translate", "-tf", "csv", "-tn", translatorName, file.toString());
    
    String output = getCommandOutput();
    List<O> results = objectMapper.readValue(output, getTypeReference());

    for (int i = 0; i < results.size(); i++) {
      O expected = createObject(String.format(
          "unique-field-%s", i
      ));
      assertObjectsEqual(expected, results.get(i), true); // null uuids, not saved
    }
  }
  
  @Test
  void testTranslateCSVError() throws IOException {
    File file = testPath.resolve("test.csv").toFile();
    writeCSV(getTranslatorFields(), file, true);

    String translatorName = "test-translator";
    writeTranslator(
        createTranslator(translatorName)
    );

    clearOut();
    execute(getCommandPrefix(), "translate", "-tf", "csv", "-tn", translatorName, file.toString());

    CLIError exception = getCLIException();
    assertEquals("Translation failed", exception.message());
    ArrayList<?> detail = (ArrayList<?>) exception.detail();
    assertEquals(100, detail.size());
    for (int i = 0; i < detail.size(); i++) {
      Map<String, Object> map = (Map<String, Object>) detail.get(i);
      assertEquals(i + 2, (Integer) map.get("row"));
      ArrayList<?> violations = (ArrayList<?>) map.get("violations");
      assertEquals(1, violations.size());
      Map<String, Object> violation = (Map<String, Object>) violations.get(0);
      assertEquals(0, violation.get("column"));
      assertEquals("UUID", violation.get("field"));
      assertEquals("Invalid UUID format", violation.get("message"));
    }
  }
  
  @Test
  void testTranslateExcel() throws IOException {
    File file = testPath.resolve("test.xlsx").toFile();
    writeExcel(getTranslatorFields(), file, false);

    String translatorName = "test-translator";
    writeTranslator(
        createTranslator(translatorName)
    );

    clearOut();
    execute(getCommandPrefix(), "translate", "-tf", "excel", "-tn", translatorName, file.toString());

    String output = getCommandOutput();
    List<O> results = objectMapper.readValue(output, getTypeReference());

    for (int i = 0; i < results.size(); i++) {
      O expected = createObject(String.format(
          "unique-field-%s", i + 1
      ));
      assertObjectsEqual(expected, results.get(i), true); // null uuids, not saved
    }
  }

  @Test
  void testTranslateExcelError() throws IOException {
    File file = testPath.resolve("test.xlsx").toFile();
    writeExcel(getTranslatorFields(), file, true);

    String translatorName = "test-translator";
    writeTranslator(
        createTranslator(translatorName)
    );

    clearOut();
    execute(getCommandPrefix(), "translate", "-tf", "excel", "-tn", translatorName, file.toString());

    CLIError exception = getCLIException();
    assertEquals("Translation failed", exception.message());
    ArrayList<?> detail = (ArrayList<?>) exception.detail();
    assertEquals(100, detail.size());
    for (int i = 0; i < detail.size(); i++) {
      Map<String, Object> map = (Map<String, Object>) detail.get(i);
      assertEquals(i + 2, (Integer) map.get("row"));
      ArrayList<?> violations = (ArrayList<?>) map.get("violations");
      assertEquals(1, violations.size());
      Map<String, Object> violation = (Map<String, Object>) violations.get(0);
      assertEquals(0, violation.get("column"));
      assertEquals("UUID", violation.get("field"));
      assertEquals("Invalid UUID format", violation.get("message"));
    }
  }

  @Test
  void testTranslateCSVTranslatorNotFound() throws IOException {
    File file = testPath.resolve("test.csv").toFile();
    writeCSV(getTranslatorFields(), file, false);

    String translatorName = "test-translator";

    clearOut();
    execute(getCommandPrefix(), "translate", "-tf", "csv", "-tn", translatorName, file.toString());

    CLIError exception = getCLIException();
    assertNull(exception.detail());
    assertEquals(String.format(
        "Translator with name = %s not found", translatorName
    ), exception.message());
  }
  
  @Test
  void testTranslateCSVInvalidTranslator() throws IOException {
    File file = testPath.resolve("test.csv").toFile();
    writeCSV(getTranslatorFields(), file, false);

    String translatorName = "test-translator";
    writeTranslator(
        createInvalidTranslator(translatorName)
    );

    clearOut();
    execute(getCommandPrefix(), "translate", "-tf", "csv", "-tn", translatorName, file.toString());

    CLIError exception = getCLIException();
    assertNull(exception.detail());
    assertEquals(String.format(
        "Translator with name = %s is not applicable to %s objects", translatorName, getClazz().getSimpleName()
    ), exception.message());
  }
  
  protected Translator createInvalidTranslator(String name) {
    return ProjectTranslator.builder()
        .name(name)
        .projectUUID("UUID")
        .projectName("NAME")
        .build();
  }

}
