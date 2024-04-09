package edu.colorado.cires.pace.core.state.repository;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.ExcelTranslator;

public class ExcelTranslatorRepository extends CRUDRepository<ExcelTranslator> {

  public ExcelTranslatorRepository(Datastore<ExcelTranslator> datastore) {
    super(datastore);
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
