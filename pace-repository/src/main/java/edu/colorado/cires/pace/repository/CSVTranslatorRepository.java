package edu.colorado.cires.pace.repository;


import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.UUID;

public class CSVTranslatorRepository extends CRUDRepository<CSVTranslator> {

  public CSVTranslatorRepository(Datastore<CSVTranslator> datastore) {
    super(datastore);
  }

  @Override
  protected CSVTranslator setUUID(CSVTranslator object, UUID uuid) {
    return object.toBuilder()
        .uuid(uuid)
        .build();
  }

}
