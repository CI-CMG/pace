package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.core.state.service.DetectionTypeService;
import edu.colorado.cires.pace.core.validation.DetectionTypeValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.data.DetectionType;

public class DetectionTypeController extends CRUDController<DetectionType, String> {

  public DetectionTypeController(Datastore<DetectionType, String> datastore) {
    super(datastore);
  }

  @Override
  protected Validator<DetectionType> getValidator() {
    return new DetectionTypeValidator();
  }

  @Override
  protected CRUDService<DetectionType, String> createService(Datastore<DetectionType, String> datastore, Datastore<?, ?>... additionalDataStores) {
    return new DetectionTypeService(
        new DetectionTypeRepository(
            datastore
        )
    );
  }
}
