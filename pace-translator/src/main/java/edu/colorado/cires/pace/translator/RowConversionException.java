package edu.colorado.cires.pace.translator;

public class RowConversionException extends Exception {
  
  private final int row;

  public RowConversionException(String message, int row) {
    super(message);
    this.row = row;
  }

  public int getRow() {
    return row;
  }
}
