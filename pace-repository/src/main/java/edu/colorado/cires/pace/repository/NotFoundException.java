package edu.colorado.cires.pace.repository;

/**
 * NotFoundException extends Exception
 */
public class NotFoundException extends Exception {

  /**
   * Creates a not found exception
   * @param message message to provide with error
   */
  public NotFoundException(String message) {
    super(message);
  }
}
