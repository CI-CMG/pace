package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.ExcelTranslatorRepository;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.core.state.service.ExcelTranslatorService;
import edu.colorado.cires.pace.core.validation.ExcelTranslatorValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.data.ExcelTranslator;

public class ExcelTranslatorController extends CRUDController<ExcelTranslator> {

  public ExcelTranslatorController(Datastore<ExcelTranslator> datastore) {
    super(datastore);
  }

  @Override
  protected Validator<ExcelTranslator> getValidator() {
    return new ExcelTranslatorValidator();
  }

  @Override
  protected CRUDService<ExcelTranslator> createService(Datastore<ExcelTranslator> datastore,
      Datastore<?>... additionalDataStores) {
    return new ExcelTranslatorService(
        new ExcelTranslatorRepository(datastore)
    );
  }
}
