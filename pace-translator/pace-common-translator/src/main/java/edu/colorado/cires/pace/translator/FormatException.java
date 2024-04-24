package edu.colorado.cires.pace.translator;

public class FormatException extends Exception {
  
  private final String property;
  private final int row;

  public FormatException(String property, String message, Throwable cause, int row) {
    super(message, cause);
    this.property = property;
    this.row = row;
  }

  public String getProperty() {
    return property;
  }

  public int getRow() {
    return row;
  }
}
