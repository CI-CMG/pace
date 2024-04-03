package edu.colorado.cires.pace.core.service;

import edu.colorado.cires.pace.core.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Ship;
import java.util.function.Consumer;

public class ShipService extends CRUDService<Ship, String> {

  protected ShipService(CRUDRepository<Ship, String> shipRepository, Consumer<Ship> onSuccess,
      Consumer<Exception> onFailure) {
    super(shipRepository, onSuccess, onFailure);
  }
}
