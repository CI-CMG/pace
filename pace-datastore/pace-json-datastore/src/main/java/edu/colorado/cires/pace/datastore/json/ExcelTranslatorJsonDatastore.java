package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.ExcelTranslator;
import java.io.IOException;
import java.nio.file.Path;

public class ExcelTranslatorJsonDatastore extends JsonDatastore<ExcelTranslator> {

  public ExcelTranslatorJsonDatastore(Path storageDirectory, ObjectMapper objectMapper) throws IOException {
    super(storageDirectory.resolve("excel-translators.json"), objectMapper, ExcelTranslator.class, ExcelTranslator::getName, new TypeReference<>() {});
  }

  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
