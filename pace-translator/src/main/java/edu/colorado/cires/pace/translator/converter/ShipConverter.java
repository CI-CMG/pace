package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;

import edu.colorado.cires.pace.data.object.ship.Ship;
import edu.colorado.cires.pace.data.object.ship.translator.ShipTranslator;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;

public class ShipConverter extends Converter<ShipTranslator, Ship> {

  @Override
  public Ship convert(ShipTranslator translator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return Ship.builder()
        .uuid(uuidFromMap(properties, "UUID", translator.getShipUUID(), row, runtimeException))
        .name(stringFromMap(properties, translator.getShipName()))
        .build();
  }
}
