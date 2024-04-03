package edu.colorado.cires.pace.cli;

import java.util.concurrent.Callable;
import picocli.CommandLine.Command;

@Command(name = "pace", mixinStandardHelpOptions = true, description = "Passive Acoustic Collection Engine")
public class PaceCLI implements Callable<Integer> {

  @Override
  public Integer call() {
    return 1;
  }
}
