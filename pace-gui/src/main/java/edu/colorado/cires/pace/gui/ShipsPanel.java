package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.data.translator.ShipTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import edu.colorado.cires.pace.repository.search.ShipSearchParameters;
import edu.colorado.cires.pace.translator.converter.ShipConverter;
import java.util.List;
import java.util.UUID;

public class ShipsPanel extends MetadataPanel<Ship, ShipTranslator> {

  public ShipsPanel(CRUDRepository<Ship> repository, TranslatorRepository translatorRepository) {
    super(
        "shipsPanel",
        repository,
        new String[] {
            "UUID", "Name"
        },
        ship -> new Object[] {
            ship.getUuid(), ship.getName()
        },
        Ship.class,
        objects -> Ship.builder()
            .uuid((UUID) objects[0])
            .name((String) objects[1])
            .build(),
        ShipForm::new,
        translatorRepository,
        new ShipConverter(),
        ShipTranslator.class
    );
  }

  @Override
  protected String getUniqueField(Ship object) {
    return object.getName();
  }

  @Override
  protected String getHumanReadableObjectName() {
    return "Ship";
  }

  @Override
  protected SearchParameters<Ship> getSearchParameters(List<String> uniqueFieldSearchTerms) {
    return ShipSearchParameters.builder()
        .names(uniqueFieldSearchTerms)
        .build();
  }
}
