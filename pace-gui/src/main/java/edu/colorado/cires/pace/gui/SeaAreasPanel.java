package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.translator.SeaTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.repository.search.SeaSearchParameters;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import edu.colorado.cires.pace.translator.converter.SeaConverter;
import java.util.List;
import java.util.UUID;

public class SeaAreasPanel extends MetadataPanel<Sea, SeaTranslator> {
  
  public SeaAreasPanel(CRUDRepository<Sea> repository, TranslatorRepository translatorRepository) {
    super(
        "seaAreasPanel",
        repository,
        new String[]{"UUID", "Name"},
        sea -> new Object[]{sea.getUuid(), sea.getName()},
        Sea.class,
        (o) -> Sea.builder()
            .uuid((UUID) o[0])
            .name((String) o[1])
            .build(),
        SeaForm::new,
        translatorRepository,
        new SeaConverter(),
        SeaTranslator.class
    );
  }

  @Override
  protected SearchParameters<Sea> getSearchParameters(List<String> uniqueFieldSearchTerms) {
    return SeaSearchParameters.builder()
        .names(uniqueFieldSearchTerms)
        .build();
  }
}
