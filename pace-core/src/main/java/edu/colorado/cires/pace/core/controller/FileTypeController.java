package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.service.CRUDService;
import edu.colorado.cires.pace.data.FileType;
import java.util.Set;
import java.util.function.Consumer;

public class FileTypeController extends CRUDController<FileType, String> {

  protected FileTypeController(CRUDService<FileType, String> service,
      Validator<FileType> validator,
      Consumer<Set<ConstraintViolation>> onValidationError) {
    super(service, validator, onValidationError);
  }
}
