package edu.colorado.cires.pace.cli.util;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.packaging.ProgressIndicator;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CLIProgressIndicatorTest {
  
  private final PrintStream originalOut = System.out;
  private final ByteArrayOutputStream output = new ByteArrayOutputStream();
  
  @BeforeEach
  void beforeEach() {
    System.setOut(new PrintStream(output));
  }
  
  @AfterEach
  void afterEach() {
    System.setOut(originalOut);
  }

  @Test
  void indicateStatus() {
    ProgressIndicator progressIndicator = new CLIProgressIndicator();
    progressIndicator.setTotalRecords(10);
    for (int i = 0; i < 10; i++) {
      progressIndicator.incrementProcessedRecords();
      int complete = (i + 1) * 10;
      int remaining = (int) ((progressIndicator.getTotalRecords() * 10) - complete);
      
      String percentMessage = complete + "%";
      
      String expected = String.format(
          "%-100s", "█".repeat(complete) + "░".repeat(remaining) + " " + percentMessage + "\r"
      );
      
      assertEquals(
          expected, output.toString()
      );
      output.reset();
    }
    
    output.reset();
    
    progressIndicator = new CLIProgressIndicator();
    progressIndicator.incrementProcessedRecords();
    String expected = String.format(
        "%-100s", "░".repeat(100) + " Initializing...\r"
    );

    assertEquals(
        expected, output.toString()
    );
  }
}