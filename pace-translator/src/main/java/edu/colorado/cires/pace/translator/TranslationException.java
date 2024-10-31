package edu.colorado.cires.pace.translator;

import java.io.IOException;

/**
 * TranslationException extends IOExceptio
 */
public class TranslationException extends IOException {

  /**
   * Creates new translation exception
   * @param message to connect with error output
   */
  public TranslationException(String message) {
    super(message);
  }
}
