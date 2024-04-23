package edu.colorado.cires.pace.cli;

import edu.colorado.cires.pace.cli.command.base.PaceCLI;
import picocli.CommandLine;

public class Application {
  
  public static void main(String[] args) {
    System.exit(
        new CommandLine(new PaceCLI()).execute(args)
    );
  }

}
