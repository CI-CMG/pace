package edu.colorado.cires.pace.gui;

import java.awt.GridBagConstraints;
import java.util.function.Consumer;

final class UIUtils {

  public static GridBagConstraints configureLayout(Consumer<GridBagConstraints> constraintsConsumer) {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraintsConsumer.accept(constraints);
    return constraints;
  }

}
