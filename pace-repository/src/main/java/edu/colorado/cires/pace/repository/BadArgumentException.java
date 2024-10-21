package edu.colorado.cires.pace.repository;

/**
 * BadArgumentException extends Exception and indicates
 * an invalid argument in a pace object
 */
public class BadArgumentException extends Exception {

  /**
   * Throws a new bad argument exception
   * @param message to attach to error output
   */
  public BadArgumentException(String message) {
    super(message);
  }
}
