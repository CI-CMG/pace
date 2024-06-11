package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.translator.Translator;
import java.io.IOException;
import java.nio.file.Path;

public class TranslatorJsonDatastore extends JsonDatastore<Translator> {

  public TranslatorJsonDatastore(Path storageDirectory, ObjectMapper objectMapper) throws IOException {
    super(storageDirectory.resolve("translators.json"), objectMapper, Translator.class, Translator::getName, new TypeReference<>() {});
  }

  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
