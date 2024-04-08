package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.CSVTranslator;
import java.io.IOException;
import java.nio.file.Path;

public class CSVTranslatorJsonDatastore extends JsonDatastore<CSVTranslator, String> {

  public CSVTranslatorJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("csv-translations"), objectMapper, CSVTranslator.class, CSVTranslator::getUUID, CSVTranslator::getName);
  }
}
