package edu.colorado.cires.pace.cli.command.dataset;

import edu.colorado.cires.pace.translator.DatasetType;
import picocli.CommandLine.ITypeConverter;

public class DatasetTypeConverter implements ITypeConverter<DatasetType> {

  @Override
  public DatasetType convert(String s) {
    return DatasetType.fromName(s);
  }
}
