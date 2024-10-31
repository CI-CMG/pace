package edu.colorado.cires.pace.translator;

/**
 * FieldException extends Exception and provides further information
 * about location in spreadsheet which caused error
 */
public class FieldException extends Exception {
  
  private final String property;
  private final String targetProperty;
  private final Integer column;
  private final Integer row;

  /**
   * Creates field exception
   * @param property property causing error
   * @param targetProperty goal property
   * @param message error message
   * @param column column location of error-causing field
   * @param row row location of error-causing field
   */
  public FieldException(String property, String targetProperty, String message, Integer column, Integer row) {
    super(message);
    this.property = property;
    this.targetProperty = targetProperty;
    this.column = column;
    this.row = row;
  }

  /**
   * Returns column number
   * @return Integer column
   */
  public Integer getColumn() {
    return column;
  }

  /**
   * Returns row number
   * @return Integer row
   */
  public Integer getRow() {
    return row;
  }

  /**
   * Returns property
   * @return String property
   */
  public String getProperty() {
    return property;
  }

  /**
   * Returns target property
   * @return String targetProperty
   */
  public String getTargetProperty() {
    return targetProperty;
  }
}
