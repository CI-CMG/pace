package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.object.base.AbstractObject;

public record ObjectWithRowError<O extends AbstractObject>(O object, int row, java.lang.Throwable throwable) {}
