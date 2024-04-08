package edu.colorado.cires.pace.core.state.service;

import edu.colorado.cires.pace.core.state.repository.CRUDRepository;
import edu.colorado.cires.pace.data.ExcelTranslator;

public class ExcelTranslatorService extends CRUDService<ExcelTranslator, String> {

  public ExcelTranslatorService(CRUDRepository<ExcelTranslator, String> crudRepository) {
    super(crudRepository);
  }
}
