package edu.colorado.cires.pace.cli.util;

import edu.colorado.cires.pace.packaging.ProgressIndicator;

public class CLIProgressIndicator extends ProgressIndicator {
  
  private final StringBuilder sb = new StringBuilder();

  @Override
  protected void onProcessedRecordsUpdate(long totalRecords, long processedRecords) {
    int percentComplete = (int) Math.ceil(100 * ((double) processedRecords / totalRecords));

    sb.setLength(0);
    sb.append("█".repeat(percentComplete));
    sb.append("░".repeat(100 - percentComplete));
    System.out.print(String.format("%-100s", sb) + " " +  percentComplete + "%");
    System.out.print("\r");
  }
}
