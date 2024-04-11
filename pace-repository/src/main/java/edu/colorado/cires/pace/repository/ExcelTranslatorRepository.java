package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.validation.ValidationException;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.UUID;

public class ExcelTranslatorRepository extends CRUDRepository<ExcelTranslator> {

  public ExcelTranslatorRepository(Datastore<ExcelTranslator> datastore) {
    super(datastore);
  }

  @Override
  protected ExcelTranslator setUUID(ExcelTranslator object, UUID uuid) throws ValidationException {
    return object.toBuilder()
        .uuid(uuid)
        .build();
  }

}
