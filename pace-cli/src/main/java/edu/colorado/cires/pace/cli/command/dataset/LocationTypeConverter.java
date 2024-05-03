package edu.colorado.cires.pace.cli.command.dataset;

import edu.colorado.cires.pace.translator.LocationType;
import picocli.CommandLine.ITypeConverter;

public class LocationTypeConverter implements ITypeConverter<LocationType> {

  @Override
  public LocationType convert(String s) {
    return LocationType.fromName(s);
  }
}
