package edu.colorado.cires.pace.translator;

/**
 * TranslatorValidationException extends Exception
 */
public class TranslatorValidationException extends Exception {

  /**
   * Creates new TranslatorValidationException
   * @param message to connect with error output
   */
  public TranslatorValidationException(String message) {
    super(message);
  }
}
