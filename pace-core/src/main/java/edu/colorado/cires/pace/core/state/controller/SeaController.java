package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.SeaRepository;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.core.state.service.SeaService;
import edu.colorado.cires.pace.core.validation.SeaValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.data.Sea;

public class SeaController extends CRUDController<Sea> {

  @Override
  protected Validator<Sea> getValidator() {
    return new SeaValidator();
  }

  @Override
  protected CRUDService<Sea> createService(Datastore<Sea> datastore, Datastore<?>... additionalDataStores) {
    return new SeaService(
        new SeaRepository(
            datastore
        )
    );
  }

  public SeaController(Datastore<Sea> datastore) {
    super(datastore);
  }
}
