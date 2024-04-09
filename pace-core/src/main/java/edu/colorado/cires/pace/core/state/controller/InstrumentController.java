package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.InstrumentRepository;
import edu.colorado.cires.pace.core.state.service.InstrumentService;
import edu.colorado.cires.pace.core.validation.InstrumentValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.data.FileType;
import edu.colorado.cires.pace.data.Instrument;

public class InstrumentController extends CRUDController<Instrument> {

  public InstrumentController(Datastore<Instrument> datastore, Datastore<FileType> fileTypeDatastore) {
    super(datastore, fileTypeDatastore);
  }

  @Override
  protected Validator<Instrument> getValidator() {
    return new InstrumentValidator();
  }

  @Override
  protected CRUDService<Instrument> createService(Datastore<Instrument> datastore, Datastore<?>... additionalDataStores) {
    return new InstrumentService(
        new InstrumentRepository(
            datastore,
            (Datastore<FileType>) additionalDataStores[0]
        )
    );
  }
}
