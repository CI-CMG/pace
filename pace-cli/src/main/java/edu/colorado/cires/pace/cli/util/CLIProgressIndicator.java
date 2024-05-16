package edu.colorado.cires.pace.cli.util;

import edu.colorado.cires.pace.packaging.ProgressIndicator;

public class CLIProgressIndicator extends ProgressIndicator {
  
  private final StringBuilder sb = new StringBuilder();
  
  private int previousPercentComplete = 0;

  @Override
  protected void onProcessedRecordsUpdate(long totalRecords, long processedRecords) {
    int percentComplete = (int) Math.ceil(100 * ((double) processedRecords / totalRecords));
    if (percentComplete == previousPercentComplete) {
      return;
    }
    
    previousPercentComplete = percentComplete;
    
    if (percentComplete == Integer.MAX_VALUE) {
      printStatusBar(0, "Initializing...");
    } else {
      printStatusBar(percentComplete, percentComplete + "%");
    }
  }
  
  private void printStatusBar(int percentComplete, String message) {
    sb.setLength(0);
    sb.append("█".repeat(percentComplete));
    sb.append("░".repeat(100 - percentComplete));
    System.out.print(String.format("%-100s", sb) + " " +  message);
    System.out.print("\r");
  }
}
