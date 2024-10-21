package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.base.Translator;
import edu.colorado.cires.pace.datastore.Datastore;

/**
 * TranslatorRepository extends CRUDRepository
 */
public class TranslatorRepository extends CRUDRepository<Translator> {

  public TranslatorRepository(Datastore<Translator> datastore) {
    super(datastore);
  }
}
