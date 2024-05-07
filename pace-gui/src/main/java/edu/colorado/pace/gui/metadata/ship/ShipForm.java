package edu.colorado.pace.gui.metadata.ship;

import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.pace.gui.metadata.common.ObjectForm;
import java.util.UUID;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public class ShipForm extends ObjectForm<Ship> {

  private JPanel formPanel;
  private JTextField uuidField;
  private JTextField nameField;
  private JLabel uuidLabel;

  public ShipForm(Ship initialShip) {
    uuidLabel.setVisible(false);
    uuidField.setVisible(false);

    if (initialShip != null) {
      uuidField.setText(initialShip.getUuid().toString());
      nameField.setText(initialShip.getName());
    }
  }

  public JPanel getFormPanel() {
    return formPanel;
  }

  @Override
  protected Ship fieldsToObject() {
    String uuidText = uuidField.getText();
    return Ship.builder()
        .uuid(StringUtils.isBlank(uuidText) ? null : UUID.fromString(uuidText))
        .name(nameField.getText())
        .build();
  }
}
