package edu.colorado.cires.pace.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.function.Consumer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

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
  
  private static TitledBorder createTitledBorder(String title) {
    return new TitledBorder(title);
  }
  
  public static Border createEtchedBorder(String title) {
    TitledBorder border = createTitledBorder(title);
    border.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
    return border;
  }
  
  public static Insets createSquareInsets(int insetSize) {
    return new Insets(insetSize, insetSize, insetSize, insetSize);
  }

}
