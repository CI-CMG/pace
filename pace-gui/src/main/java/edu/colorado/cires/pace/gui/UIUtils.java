package edu.colorado.cires.pace.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

final class UIUtils {
  
  public static Dimension getPercentageOfWindowDimension(double percentWidth, double percentHeight) {
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    return new Dimension(
        (int) (dimension.width * percentWidth),
        (int) (dimension.height * percentHeight)
    );
  }

  public static GridBagConstraints configureLayout(Consumer<GridBagConstraints> constraintsConsumer) {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraintsConsumer.accept(constraints);
    return constraints;
  }

  public static GridBagConstraints configureFormLayout(int x, int y) {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridx = x;
    constraints.gridy = y;
    constraints.weightx = 1;
    return constraints;
  }

  public static void updateComboBoxModel(JComboBox<String> comboBox, String[] options) {
    String currentSelectedItem = (String) comboBox.getSelectedItem();
    comboBox.setModel(new DefaultComboBoxModel<>(options));
    if (Arrays.asList(options).contains(currentSelectedItem)) {
      comboBox.setSelectedItem(currentSelectedItem); 
    } else {
      comboBox.setSelectedItem(null);
    }
  }
  
  private static TitledBorder createTitledBorder(String title) {
    return new TitledBorder(title);
  }
  
  public static Border createEtchedBorder(String title) {
    TitledBorder border = createTitledBorder(title);
    border.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
    return border;
  }
  
  public static ImageIcon getImageIcon(String fileName, Class<?> clazz) {
    Image image = readImage(fileName, clazz);
    Image newImg = image.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH);
    return new ImageIcon(newImg);
  }

  private static Image readImage(String fileName, Class<?> clazz) {
    try {
      return ImageIO.read(
          Objects.requireNonNull(
              clazz.getResourceAsStream(String.format("/%s", fileName))
          )
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
