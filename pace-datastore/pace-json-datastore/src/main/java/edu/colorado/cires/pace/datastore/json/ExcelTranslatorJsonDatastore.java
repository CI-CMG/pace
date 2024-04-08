package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.ExcelTranslator;
import java.io.IOException;
import java.nio.file.Path;

public class ExcelTranslatorJsonDatastore extends JsonDatastore<ExcelTranslator, String> {

  public ExcelTranslatorJsonDatastore(Path storageDirectory, ObjectMapper objectMapper) throws IOException {
    super(storageDirectory, objectMapper, ExcelTranslator.class, ExcelTranslator::getUUID, ExcelTranslator::getName);
  }
}
