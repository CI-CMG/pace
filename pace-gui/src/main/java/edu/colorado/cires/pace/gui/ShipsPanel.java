package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.ship.Ship;
import edu.colorado.cires.pace.data.object.ship.translator.ShipTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.converter.ShipConverter;
import java.util.UUID;

/**
 * ShipsPanel extends MetadataPanel and provides structure relevant
 * to ships panel
 */
public class ShipsPanel extends MetadataPanel<Ship, ShipTranslator> {

  /**
   * Creates ships panel
   * @param repository holds existing ship objects
   * @param translatorRepository holds existing translators
   */
  public ShipsPanel(CRUDRepository<Ship> repository, TranslatorRepository translatorRepository) {
    super(
        "shipsPanel",
        repository,
        new String[] {
            "UUID", "Name", "Visible"
        },
        ship -> new Object[] {
            ship.getUuid(), ship.getName(), ship.isVisible()
        },
        Ship.class,
        objects -> Ship.builder()
            .uuid((UUID) objects[0])
            .name((String) objects[1])
            .visible((Boolean) objects[2])
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
}
