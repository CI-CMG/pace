package edu.colorado.cires.pace.core.state.service;

import edu.colorado.cires.pace.core.state.repository.CRUDRepository;
import edu.colorado.cires.pace.data.ExcelTranslator;

public class ExcelTranslatorService extends CRUDService<ExcelTranslator> {

  public ExcelTranslatorService(CRUDRepository<ExcelTranslator> crudRepository) {
    super(crudRepository);
  }
}
