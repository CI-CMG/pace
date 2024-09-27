package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.ship.Ship;
import java.io.IOException;
import java.nio.file.Path;

/**
 * ShipJsonDatastore extends JsonDatastore and returns name
 * as the unique field name
 */
public class ShipJsonDatastore extends JsonDatastore<Ship> {

  /**
   * Initializes the ship JSON datastore
   *
   * @param workDirectory location of datastore on machine
   * @param objectMapper relevant object mapper
   * @throws IOException in case of input or output error
   */
  public ShipJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("ships"), objectMapper, Ship.class, Ship::getName);
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
