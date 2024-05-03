package edu.colorado.cires.pace.translator.csv;

import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.CSVTranslatorField;
import edu.colorado.cires.pace.translator.SpreadsheetGenerator;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVGenerator extends SpreadsheetGenerator<CSVTranslatorField, CSVTranslator> {

  @Override
  protected void writeFieldsToSpreadsheet(OutputStream outputStream, List<CSVTranslatorField> translatorFields) throws IOException {
    CSVFormat format = CSVFormat.DEFAULT.builder()
        .setHeader(getHeaderNames(translatorFields))
        .build();
    
    try (
        Writer writer = new OutputStreamWriter(outputStream);
        CSVPrinter csvPrinter = new CSVPrinter(writer, format)
    ) {}
  }
}
