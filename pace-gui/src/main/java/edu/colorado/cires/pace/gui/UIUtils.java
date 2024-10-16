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

/**
 * UIUtils provides functions relevant to the greater
 * scope of the GUI
 */
final class UIUtils {

  /**
   * Returns the percentage of window dimensions
   * @param percentWidth percent of window width to fill up
   * @param percentHeight percent of window height to fill up
   * @return Dimension holding integer values for width and height of window
   */
  public static Dimension getPercentageOfWindowDimension(double percentWidth, double percentHeight) {
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    return new Dimension(
        (int) (dimension.width * percentWidth),
        (int) (dimension.height * percentHeight)
    );
  }

  /**
   * Returns relevant overall layout constraints
   * @param constraintsConsumer consumes the grid bag constraints object
   * @return GridBagConstraints of overall layout
   */
  public static GridBagConstraints configureLayout(Consumer<GridBagConstraints> constraintsConsumer) {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraintsConsumer.accept(constraints);
    return constraints;
  }

  /**
   * Returns relevant form layout constraints
   * @param x width constraint
   * @param y height constraint
   * @return GridBagConstraints limiting width and height
   */
  public static GridBagConstraints configureFormLayout(int x, int y) {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridx = x;
    constraints.gridy = y;
    constraints.weightx = 1;
    return constraints;
  }

  /**
   * Changes the selectable options in a combo box
   * @param comboBox combo box to change the options of
   * @param options new selectable options
   */
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

  /**
   * Returns border with title etched on
   * @param title string to etch on the border
   * @return Border with title etched on
   */
  public static Border createEtchedBorder(String title) {
    TitledBorder border = createTitledBorder(title);
    border.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
    return border;
  }

  /**
   * Returns image icon found at provided file location
   * @param fileName file location of image
   * @param clazz relevant image class
   * @return ImageIcon found at fileName
   */
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
