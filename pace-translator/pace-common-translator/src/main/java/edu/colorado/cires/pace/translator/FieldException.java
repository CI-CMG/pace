package edu.colorado.cires.pace.translator;

public class FieldException extends Exception {
  
  private final String property;
  private final int column;

  public FieldException(String property, String message, int column) {
    super(message);
    this.property = property;
    this.column = column;
  }

  public String getProperty() {
    return property;
  }
}
