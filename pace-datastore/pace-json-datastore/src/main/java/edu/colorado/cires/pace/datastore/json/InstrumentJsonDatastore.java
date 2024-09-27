package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.instrument.Instrument;
import java.io.IOException;
import java.nio.file.Path;

/**
 * InstrumentJsonDatastore extends JsonDatastore and provides name
 * as unique field name
 */
public class InstrumentJsonDatastore extends JsonDatastore<Instrument> {

  /**
   * Initializes instrument JSON datastore
   *
   * @param workDirectory location on device holding datastore
   * @param objectMapper relevant object mapper
   * @throws IOException in case of input or output error
   */
  public InstrumentJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("instruments"), objectMapper, Instrument.class, Instrument::getName);
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
