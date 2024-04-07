package edu.colorado.cires.pace.core.state.service;

import edu.colorado.cires.pace.core.state.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Ship;

public class ShipService extends CRUDService<Ship, String> {

  public ShipService(CRUDRepository<Ship, String> shipRepository) {
    super(shipRepository);
  }
}
