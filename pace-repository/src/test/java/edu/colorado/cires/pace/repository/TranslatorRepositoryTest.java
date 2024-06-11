package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.translator.Translator;
import java.util.function.Function;

abstract class TranslatorRepositoryTest extends CrudRepositoryTest<Translator> {

  @Override
  protected CRUDRepository<Translator> createRepository() {
    return new TranslatorRepository(createDatastore());
  }

  @Override
  protected Function<Translator, String> uniqueFieldGetter() {
    return Translator::getName;
  }
}
