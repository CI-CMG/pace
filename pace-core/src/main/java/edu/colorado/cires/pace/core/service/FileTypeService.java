package edu.colorado.cires.pace.core.service;

import edu.colorado.cires.pace.core.repository.CRUDRepository;
import edu.colorado.cires.pace.data.FileType;
import java.util.function.Consumer;

public class FileTypeService extends CRUDService<FileType, String> {

  protected FileTypeService(CRUDRepository<FileType, String> fileTypeRepository,
      Consumer<FileType> onSuccess, Consumer<Exception> onFailure) {
    super(fileTypeRepository, onSuccess, onFailure);
  }
}
