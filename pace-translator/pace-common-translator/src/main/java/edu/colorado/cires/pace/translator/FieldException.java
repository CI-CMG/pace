package edu.colorado.cires.pace.translator;

public class FieldException extends Exception {
  
  private final String property;
  private final int column;
  private final int row;

  public FieldException(String property, String message, int column, int row) {
    super(message);
    this.property = property;
    this.column = column;
    this.row = row;
  }

  public int getColumn() {
    return column;
  }

  public int getRow() {
    return row;
  }

  public String getProperty() {
    return property;
  }
}
