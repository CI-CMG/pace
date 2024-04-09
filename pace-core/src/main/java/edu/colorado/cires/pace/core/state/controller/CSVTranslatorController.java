package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.CSVTranslatorRepository;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.core.state.service.CSVTranslatorService;
import edu.colorado.cires.pace.core.validation.CSVTranslatorValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.data.CSVTranslator;

public class CSVTranslatorController extends CRUDController<CSVTranslator> {

  public CSVTranslatorController(Datastore<CSVTranslator> datastore) {
    super(datastore);
  }

  @Override
  protected Validator<CSVTranslator> getValidator() {
    return new CSVTranslatorValidator();
  }

  @Override
  protected CRUDService<CSVTranslator> createService(Datastore<CSVTranslator> datastore, Datastore<?>... additionalDataStores) {
    return new CSVTranslatorService(
        new CSVTranslatorRepository(datastore)
    );
  }
}
