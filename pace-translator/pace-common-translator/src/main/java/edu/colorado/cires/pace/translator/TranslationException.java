package edu.colorado.cires.pace.translator;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TranslationException extends IOException {
  
  private final List<Throwable> exceptions;

  public TranslationException(String message, Throwable throwable) {
    super(message, throwable);
    this.exceptions = Collections.emptyList();
  }

  public TranslationException(String message) {
    super(message);
    this.exceptions = Collections.emptyList();
  }

  public TranslationException(String message, List<Throwable> exceptions) {
    super(message);
    this.exceptions = exceptions;
  }

  public List<Throwable> getExceptions() {
    return exceptions;
  }
}
