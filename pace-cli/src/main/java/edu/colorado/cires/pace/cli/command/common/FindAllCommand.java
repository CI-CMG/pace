package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.ObjectWithUniqueField;
import java.util.Collections;

public abstract class FindAllCommand<O extends ObjectWithUniqueField> extends CRUDCommand<O> {

  @Override
  protected Object runCommand() throws Exception {
    return createController().readAll(Collections.emptyList())
        .toList();
  }

}
