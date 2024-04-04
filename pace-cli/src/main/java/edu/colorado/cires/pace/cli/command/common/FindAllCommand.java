package edu.colorado.cires.pace.cli.command.common;

import java.util.Collections;

public abstract class FindAllCommand<O, U> extends CRUDCommand<O, U> {

  @Override
  protected Object runCommand() throws Exception {
    return createController().readAll(Collections.emptyList())
        .toList();
  }

}
