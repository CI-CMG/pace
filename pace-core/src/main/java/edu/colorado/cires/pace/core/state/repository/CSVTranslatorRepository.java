package edu.colorado.cires.pace.core.state.repository;


import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.CSVTranslator;

public class CSVTranslatorRepository extends CRUDRepository<CSVTranslator, String> {

  public CSVTranslatorRepository(Datastore<CSVTranslator, String> datastore) {
    super(CSVTranslator::getUUID, CSVTranslator::getName, CSVTranslator::setUUID, datastore);
  }

  @Override
  protected String getObjectName() {
    return "CSVTranslator";
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }
}
