package edu.colorado.cires.pace.core.exception;

public class PackingException extends Exception {

  public PackingException(String message, Throwable cause) {
    super(message, cause);
  }

  public PackingException(String message, Throwable[] causes) {
    super(message);
    for (Throwable cause : causes) {
      super.addSuppressed(cause);
    }
  }
}
