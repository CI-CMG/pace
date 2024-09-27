package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.base.Translator;
import java.io.IOException;
import java.nio.file.Path;

/**
 * TranslatorJsonDatastore extends JsonDatastore and returns name
 * as the unique field name
 */
public class TranslatorJsonDatastore extends JsonDatastore<Translator> {

  /**
   * Initializes the translator JSON datastore
   *
   * @param storageDirectory location of datastore on machine
   * @param objectMapper relevant object mapper
   * @throws IOException in case of input or output error
   */
  public TranslatorJsonDatastore(Path storageDirectory, ObjectMapper objectMapper) throws IOException {
    super(storageDirectory.resolve("translators"), objectMapper, Translator.class, Translator::getName);
  }

  /**
   * Returns unique field name
   * @return String name
   */
  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
