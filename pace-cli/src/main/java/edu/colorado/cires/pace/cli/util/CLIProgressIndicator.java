package edu.colorado.cires.pace.cli.util;

import edu.colorado.cires.pace.packaging.ProgressIndicator;

public class CLIProgressIndicator extends ProgressIndicator {
  
  private final StringBuilder sb = new StringBuilder();

  @Override
  protected void indicateStatus(int percentComplete) {
    boolean usePercentCompleteArgument = percentComplete != Integer.MAX_VALUE;
    int usablePercentComplete = !usePercentCompleteArgument ? 0 : percentComplete; 
    String message = !usePercentCompleteArgument ? "Initializing..." : (percentComplete + "%"); 
    
    sb.setLength(0);
    sb.append("█".repeat(usablePercentComplete));
    sb.append("░".repeat(100 - usablePercentComplete));
    System.out.print(String.format("%-100s", sb) + " " +  message);
    System.out.print("\r");
  }
}
