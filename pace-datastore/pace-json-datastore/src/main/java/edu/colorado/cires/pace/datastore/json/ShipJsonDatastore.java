package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.Ship;
import java.io.IOException;
import java.nio.file.Path;

public class ShipJsonDatastore extends JsonDatastore<Ship, String> {

  protected ShipJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory, objectMapper, Ship.class, Ship::getUUID, Ship::getName);
  }
}
