package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.translator.SeaTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.converter.SeaConverter;
import java.util.UUID;

public class SeaAreasPanel extends MetadataPanel<Sea, SeaTranslator> {
  
  public SeaAreasPanel(CRUDRepository<Sea> repository, TranslatorRepository translatorRepository) {
    super(
        "seaAreasPanel",
        repository,
        new String[]{"UUID", "Name", "Visible"},
        sea -> new Object[]{sea.getUuid(), sea.getName(), sea.isVisible()},
        Sea.class,
        (o) -> Sea.builder()
            .uuid((UUID) o[0])
            .name((String) o[1])
            .visible((Boolean) o[2])
            .build(),
        SeaForm::new,
        translatorRepository,
        new SeaConverter(),
        SeaTranslator.class
    );
  }

  @Override
  protected String getUniqueField(Sea object) {
    return object.getName();
  }

  @Override
  protected String getHumanReadableObjectName() {
    return "Sea Area";
  }
}
