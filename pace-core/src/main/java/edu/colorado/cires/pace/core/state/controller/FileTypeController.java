package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.validation.FileTypeValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.data.FileType;

public class FileTypeController extends CRUDController<FileType, String> {

  public FileTypeController(CRUDService<FileType, String> service) {
    super(service);
  }

  @Override
  protected Validator<FileType> getValidator() {
    return new FileTypeValidator();
  }
}
