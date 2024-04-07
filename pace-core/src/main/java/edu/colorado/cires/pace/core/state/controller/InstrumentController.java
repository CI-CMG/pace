package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.InstrumentRepository;
import edu.colorado.cires.pace.core.state.service.InstrumentService;
import edu.colorado.cires.pace.core.validation.InstrumentValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.data.FileType;
import edu.colorado.cires.pace.data.Instrument;

public class InstrumentController extends CRUDController<Instrument, String> {

  public InstrumentController(Datastore<Instrument, String> datastore, Datastore<FileType, String> fileTypeDatastore) {
    super(datastore, fileTypeDatastore);
  }

  @Override
  protected Validator<Instrument> getValidator() {
    return new InstrumentValidator();
  }

  @Override
  protected CRUDService<Instrument, String> createService(Datastore<Instrument, String> datastore, Datastore<?, ?>... additionalDataStores) {
    return new InstrumentService(
        new InstrumentRepository(
            datastore,
            (Datastore<FileType, String>) additionalDataStores[0]
        )
    );
  }
}
