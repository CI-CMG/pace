package edu.colorado.cires.pace.gui;

import java.awt.GridBagConstraints;
import java.util.function.Consumer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

final class UIUtils {

  public static GridBagConstraints configureLayout(Consumer<GridBagConstraints> constraintsConsumer) {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraintsConsumer.accept(constraints);
    return constraints;
  }

  public static void updateComboBoxModel(JComboBox<String> comboBox, String[] options) {
    String currentSelectedItem = (String) comboBox.getSelectedItem();
    comboBox.setModel(new DefaultComboBoxModel<>(options));
    comboBox.setSelectedItem(currentSelectedItem);
  }

}
