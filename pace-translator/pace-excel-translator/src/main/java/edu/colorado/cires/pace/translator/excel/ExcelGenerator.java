package edu.colorado.cires.pace.translator.excel;

import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import edu.colorado.cires.pace.translator.SpreadsheetGenerator;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;

public class ExcelGenerator extends SpreadsheetGenerator<ExcelTranslatorField, ExcelTranslator> {
  
  private final String applicationVersion;

  public ExcelGenerator(String applicationVersion) {
    this.applicationVersion = applicationVersion;
  }

  @Override
  protected void writeFieldsToSpreadsheet(OutputStream outputStream, List<ExcelTranslatorField> translatorFields) throws IOException {
    try (Workbook workbook = new Workbook(outputStream, "PACE", applicationVersion)) {
      Map<Integer, List<ExcelTranslatorField>> sheetFields = translatorFields.stream()
          .collect(Collectors.groupingBy(
              ExcelTranslatorField::getSheetNumber
          ));
      
      sheetFields.forEach((sheetNumber, fields) -> {
        Worksheet worksheet = workbook.newWorksheet(String.format(
            "Sheet %s", sheetNumber
        ));
        worksheet.width(0, fields.size());
        worksheet.width(1, fields.size());
        
        String[] headers = getHeaderNames(fields);

        for (int i = 0; i < headers.length; i++) {
          worksheet.value(0, i, headers[i]);
        }
      });
    }
  }
}
