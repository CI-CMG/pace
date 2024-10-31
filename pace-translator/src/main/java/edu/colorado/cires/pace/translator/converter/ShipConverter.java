package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;

import edu.colorado.cires.pace.data.object.ship.Ship;
import edu.colorado.cires.pace.data.object.ship.translator.ShipTranslator;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;

/**
 * ShipConverter extends Converter and provides convert function for
 * Ship objects
 */
public class ShipConverter extends Converter<ShipTranslator, Ship> {

  /**
   * Creates a ship object from the provided properties
   * @param translator translates to ship object
   * @param properties maps property names to values
   * @param row relevant row
   * @param runtimeException thrown in case of error mapping
   * @return Ship object
   */
  @Override
  public Ship convert(ShipTranslator translator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return Ship.builder()
        .uuid(uuidFromMap(properties, "UUID", translator.getShipUUID(), row, runtimeException))
        .name(stringFromMap(properties, translator.getShipName()))
        .build();
  }
}
