package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;

public abstract class FindAllCommand<O extends ObjectWithUniqueField> extends CRUDCommand<O> {

  @Override
  protected Object runCommand() throws Exception {
    return createRepository().findAll()
        .toList();
  }

}
