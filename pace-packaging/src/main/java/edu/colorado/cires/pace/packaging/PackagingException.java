package edu.colorado.cires.pace.packaging;

public class PackagingException extends Exception {

  public PackagingException(String message, Throwable cause) {
    super(message, cause);
  }

  public PackagingException(String message, Throwable[] causes) {
    super(message);
    for (Throwable cause : causes) {
      super.addSuppressed(cause);
    }
  }
}
