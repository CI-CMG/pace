package edu.colorado.cires.pace.translator;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TranslationException extends IOException {
  
  private final List<FormatException> formatExceptions;

  public TranslationException(String message, Throwable throwable) {
    super(message, throwable);
    this.formatExceptions = Collections.emptyList();
  }

  public TranslationException(String message) {
    super(message);
    this.formatExceptions = Collections.emptyList();
  }

  public TranslationException(String message, List<FormatException> formatExceptions) {
    super(message);
    this.formatExceptions = formatExceptions;
  }

  public List<FormatException> getFormatExceptions() {
    return formatExceptions;
  }
}
