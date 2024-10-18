package edu.colorado.cires.pace.packaging;

/**
 * PackagingException extends Exception and additionally
 * provides a cause for the error
 */
public class PackagingException extends Exception {

  public PackagingException(String message, Throwable cause) {
    super(message, cause);
  }

}
