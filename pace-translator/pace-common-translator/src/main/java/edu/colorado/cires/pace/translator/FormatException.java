package edu.colorado.cires.pace.translator;

public class FormatException extends Exception {
  
  private final String propertyName;

  public FormatException(String propertyName, String message, Throwable cause) {
    super(message, cause);
    this.propertyName = propertyName;
  }

  public String getProperty() {
    return propertyName;
  }
}
