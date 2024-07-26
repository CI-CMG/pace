package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.base.Translator;

abstract class TranslatorRepositoryTest extends CrudRepositoryTest<Translator> {

  @Override
  protected CRUDRepository<Translator> createRepository() {
    return new TranslatorRepository(createDatastore());
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected Class<Translator> getObjectClass() {
    return Translator.class;
  }
}
