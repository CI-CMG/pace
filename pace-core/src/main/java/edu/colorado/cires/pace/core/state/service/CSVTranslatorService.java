package edu.colorado.cires.pace.core.state.service;

import edu.colorado.cires.pace.core.state.repository.CRUDRepository;
import edu.colorado.cires.pace.data.CSVTranslator;

public class CSVTranslatorService extends CRUDService<CSVTranslator, String> {

  public CSVTranslatorService(CRUDRepository<CSVTranslator, String> crudRepository) {
    super(crudRepository);
  }
}
