package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.translator.Translator;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import edu.colorado.cires.pace.repository.search.TranslatorSearchParameters;
import java.util.List;
import java.util.function.Function;

abstract class TranslatorRepositoryTest extends CrudRepositoryTest<Translator> {

  @Override
  protected SearchParameters<Translator> createSearchParameters(List<Translator> objects) {
    return TranslatorSearchParameters.builder()
        .names(objects.stream()
            .map(Translator::getName)
            .toList())
        .build();
  }

  @Override
  protected CRUDRepository<Translator> createRepository() {
    return new TranslatorRepository(createDatastore());
  }

  @Override
  protected Function<Translator, String> uniqueFieldGetter() {
    return Translator::getName;
  }
}
