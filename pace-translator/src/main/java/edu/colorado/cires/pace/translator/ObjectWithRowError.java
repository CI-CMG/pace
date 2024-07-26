package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;

public record ObjectWithRowError<O extends ObjectWithUniqueField>(O object, int row, java.lang.Throwable throwable) {}
