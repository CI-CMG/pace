package edu.colorado.cires.pace.core.service;

import edu.colorado.cires.pace.core.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Ship;

public class ShipService extends CRUDService<Ship, String> {

  public ShipService(CRUDRepository<Ship, String> shipRepository) {
    super(shipRepository);
  }
}
