package edu.colorado.cires.pace.translator;

public class FieldException extends Exception {
  
  private final String property;
  private final String targetProperty;
  private final Integer column;
  private final Integer row;

  public FieldException(String property, String targetProperty, String message, Integer column, Integer row) {
    super(message);
    this.property = property;
    this.targetProperty = targetProperty;
    this.column = column;
    this.row = row;
  }

  public Integer getColumn() {
    return column;
  }

  public Integer getRow() {
    return row;
  }

  public String getProperty() {
    return property;
  }

  public String getTargetProperty() {
    return targetProperty;
  }
}
