package edu.colorado.cires.pace.cli.command;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.translator.Translator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.junit.jupiter.api.Test;

public abstract class TranslateCommandTest<O extends ObjectWithUniqueField, T extends Translator> extends CRUDCommandTest<O> {
  
  protected abstract String[] getTranslatorFields();
  protected abstract T createTranslator(String name);
  protected abstract String[] objectToRow(O object);
  
  private void writeTranslator(T translator) throws IOException {
    File file = testPath.resolve("test-translator.json").toFile();
    objectMapper.writeValue(file, translator);
    execute("translator", "create", file.toString());
  }
  
  private void writeExcel(String[] translatorFields, File file) throws IOException {
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
        for (int j = 0; j < rowData.length; j++) {
          worksheet.value(i, j, rowData[j]);
        }
      }
    }
  }
  
  private void writeCSV(String[] translatorFields, File file) throws IOException {
    CSVFormat format = CSVFormat.DEFAULT.builder()
        .setHeader(translatorFields)
        .build();

    try (
        FileWriter fileWriter = new FileWriter(file);
        CSVPrinter printer = new CSVPrinter(fileWriter, format)
    ) {
      for (int i = 0; i < 100; i++) {
        printer.printRecord(
            (Object[]) objectToRow(
                createObject(String.format(
                    "unique-field-%s", i
                ))
            )
        );
      }
    }
  }
  
  @Test
  void testTranslateCSV() throws IOException {
    File file = testPath.resolve("test.csv").toFile();
    writeCSV(getTranslatorFields(), file);
    
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
  void testTranslateExcel() throws IOException {
    File file = testPath.resolve("test.xlsx").toFile();
    writeExcel(getTranslatorFields(), file);

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

}
