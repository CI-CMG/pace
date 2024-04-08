package edu.colorado.cires.pace.core.state.repository;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.ExcelTranslator;

public class ExcelTranslatorRepository extends CRUDRepository<ExcelTranslator, String> {

  public ExcelTranslatorRepository(Datastore<ExcelTranslator, String> datastore) {
    super(ExcelTranslator::getUUID, ExcelTranslator::getName, ExcelTranslator::setUUID, datastore);
  }

  @Override
  protected String getObjectName() {
    return "ExcelTranslator";
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }
}
