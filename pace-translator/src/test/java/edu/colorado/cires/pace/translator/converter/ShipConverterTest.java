package edu.colorado.cires.pace.translator.converter;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.ship.Ship;
import edu.colorado.cires.pace.data.object.ship.translator.ShipTranslator;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ShipConverterTest {
  
  private final Converter<ShipTranslator, Ship> converter = new ShipConverter();

  @Test
  void convert() throws TranslationException {
    UUID uuid = UUID.randomUUID();
    String name = "name-value";
    
    Ship ship = converter.convert(
        ShipTranslator.builder()
            .shipUUID("shipUUID")
            .shipName("shipName")
            .build(),
        Map.of(
            "shipUUID", new ValueWithColumnNumber(Optional.of(uuid.toString()), 1),
            "shipName",  new ValueWithColumnNumber(Optional.of(name), 2)
        ),
        1,
        new RuntimeException()
    );
    assertEquals(uuid, ship.getUuid());
    assertEquals(name, ship.getName());
  }
}