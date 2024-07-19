package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.translator.Translator;
import edu.colorado.cires.pace.datastore.Datastore;

public class TranslatorRepository extends CRUDRepository<Translator> {

  public TranslatorRepository(Datastore<Translator> datastore) {
    super(datastore);
  }
}
