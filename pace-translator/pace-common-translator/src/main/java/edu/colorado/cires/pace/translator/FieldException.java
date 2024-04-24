package edu.colorado.cires.pace.translator;

public class FieldException extends Exception {
  
  private final String property;

  public FieldException(String property, String message) {
    super(message);
    this.property = property;
  }

  public String getProperty() {
    return property;
  }
}
