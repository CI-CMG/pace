package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.ShipRepository;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.core.state.service.ShipService;
import edu.colorado.cires.pace.core.validation.ShipValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.data.Ship;

public class ShipController extends CRUDController<Ship> {

  @Override
  protected Validator<Ship> getValidator() {
    return new ShipValidator();
  }

  @Override
  protected CRUDService<Ship> createService(Datastore<Ship> datastore, Datastore<?>... additionalDataStores) {
    return new ShipService(
        new ShipRepository(
            datastore
        )
    );
  }

  public ShipController(Datastore<Ship> datastore) {
    super(datastore);
  }
}
