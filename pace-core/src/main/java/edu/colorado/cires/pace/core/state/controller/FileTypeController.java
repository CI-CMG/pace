package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.FileTypeRepository;
import edu.colorado.cires.pace.core.state.service.FileTypeService;
import edu.colorado.cires.pace.core.validation.FileTypeValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.data.FileType;

public class FileTypeController extends CRUDController<FileType, String> {

  public FileTypeController(Datastore<FileType, String> datastore) {
    super(datastore);
  }

  @Override
  protected Validator<FileType> getValidator() {
    return new FileTypeValidator();
  }

  @Override
  protected CRUDService<FileType, String> createService(Datastore<FileType, String> datastore, Datastore<?, ?>... additionalDataStores) {
    return new FileTypeService(
        new FileTypeRepository(
            datastore
        )
    );
  }
}
