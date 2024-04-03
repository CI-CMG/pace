package edu.colorado.cires.pace.cli;

import picocli.CommandLine;

public class Application {
  
  public static void main(String[] args) {
    System.exit(
        new CommandLine(new PaceCLI()).execute(args)
    );
  }

}
