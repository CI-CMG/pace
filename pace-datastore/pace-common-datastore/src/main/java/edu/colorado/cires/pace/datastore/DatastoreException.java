package edu.colorado.cires.pace.datastore;

/**
 * DatastoreException extends Exception
 */
public class DatastoreException extends Exception {

  /**
   * Generates an error statement using the message and cause
   * @param message to describe error
   * @param cause gives reason for error
   */
  public DatastoreException(String message, Throwable cause) {
    super(message, cause);
  }
}
