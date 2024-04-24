package edu.colorado.cires.pace.translator;

import java.io.IOException;

public class TranslationException extends IOException {

  public TranslationException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public TranslationException(String message) {
    super(message);
  }
}
