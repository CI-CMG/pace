package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.PlatformRepository;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.core.state.service.PlatformService;
import edu.colorado.cires.pace.core.validation.PlatformValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.data.Platform;

public class PlatformController extends CRUDController<Platform, String> {

  @Override
  protected Validator<Platform> getValidator() {
    return new PlatformValidator();
  }

  @Override
  protected CRUDService<Platform, String> createService(Datastore<Platform, String> datastore, Datastore<?, ?>... additionalDataStores) {
    return new PlatformService(
        new PlatformRepository(
            datastore
        )
    );
  }

  public PlatformController(Datastore<Platform, String> datastore) {
    super(datastore);
  }
}
