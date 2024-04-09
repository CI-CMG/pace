package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.core.state.service.DetectionTypeService;
import edu.colorado.cires.pace.core.validation.DetectionTypeValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.data.DetectionType;

public class DetectionTypeController extends CRUDController<DetectionType> {

  public DetectionTypeController(Datastore<DetectionType> datastore) {
    super(datastore);
  }

  @Override
  protected Validator<DetectionType> getValidator() {
    return new DetectionTypeValidator();
  }

  @Override
  protected CRUDService<DetectionType> createService(Datastore<DetectionType> datastore, Datastore<?>... additionalDataStores) {
    return new DetectionTypeService(
        new DetectionTypeRepository(
            datastore
        )
    );
  }
}
