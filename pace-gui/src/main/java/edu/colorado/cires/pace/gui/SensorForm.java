package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.OtherSensor;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.translator.SensorType;
import java.awt.GridBagLayout;
import java.util.UUID;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public class SensorForm extends Form<Sensor> {
  
  private final JTextField uuidField = new JTextField();
  private final JTextField nameField = new JTextField();
  private final JTextField xField = new JTextField();
  private final JTextField yField = new JTextField();
  private final JTextField zField = new JTextField();
  private final JTextField descriptionField = new JTextField();
  
  // audio
  private final JTextField hydrophoneIdField = new JTextField();
  private final JTextField preampIdField = new JTextField();
  
  // other
  private final JTextField sensorTypeField = new JTextField();
  private final JTextField propertiesField = new JTextField();
  
  private final SensorType sensorType;

  public SensorForm(Sensor initialSensor) {
    SensorType sensorType;
    sensorType = initialSensor == null ? null : SensorType.fromSensor(initialSensor);
    if (sensorType == null) {
      sensorType = (SensorType) JOptionPane.showInputDialog(
          this,
          null,
          "Choose sensor type",
          JOptionPane.PLAIN_MESSAGE,
          null,
          SensorType.values(),
          null
      ); 
    }
    
    this.sensorType = sensorType;
    
    setLayout(new GridBagLayout());
    
    add(new JLabel("UUID"), configureLayout((c) -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    add(uuidField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Name"), configureLayout((c) -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(nameField, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    JPanel positionPanel = new JPanel(new GridBagLayout());
    JPanel xPanel = new JPanel(new GridBagLayout());
    xPanel.add(new JLabel("Position (X)"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    xPanel.add(xField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    positionPanel.add(xPanel, configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    JPanel yPanel = new JPanel(new GridBagLayout());
    yPanel.add(new JLabel("Position (Y)"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    yPanel.add(yField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    positionPanel.add(yPanel, configureLayout((c) -> { c.gridx = 1; c.gridy = 0; c.weightx = 1; }));
    JPanel zPanel = new JPanel(new GridBagLayout());
    zPanel.add(new JLabel("Position (Z)"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    zPanel.add(zField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    positionPanel.add(zPanel, configureLayout((c) -> { c.gridx = 2; c.gridy = 0; c.weightx = 1; }));
    add(positionPanel, configureLayout((c) -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    add(new JLabel("Description"), configureLayout((c) -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
    add(descriptionField, configureLayout((c) -> { c.gridx = 0; c.gridy = 6; c.weightx = 1; }));
    
    switch (sensorType) {
      case audio -> {
        add(new JLabel("Hydrophone ID"), configureLayout((c) -> { c.gridx = 0; c.gridy = 7; c.weightx = 1; }));
        add(hydrophoneIdField, configureLayout((c) -> { c.gridx = 0; c.gridy = 8; c.weightx = 1; }));
        add(new JLabel("Preamp ID"), configureLayout((c) -> { c.gridx = 0; c.gridy = 9; c.weightx = 1; }));
        add(preampIdField, configureLayout((c) -> { c.gridx = 0; c.gridy = 10; c.weightx = 1; }));
        add(new JPanel(), configureLayout((c) -> { c.gridx = 0; c.gridy = 11; c.weighty = 1; }));
      }
      case depth -> add(new JPanel(), configureLayout((c) -> { c.gridx = 0; c.gridy = 7; c.weighty = 1; }));
      case other -> {
        add(new JLabel("Sensor Type"), configureLayout((c) -> { c.gridx = 0; c.gridy = 7; c.weightx = 1; }));
        add(sensorTypeField, configureLayout((c) -> { c.gridx = 0; c.gridy = 8; c.weightx = 1; }));
        add(new JLabel("Properties"), configureLayout((c) -> { c.gridx = 0; c.gridy = 9; c.weightx = 1; }));
        add(propertiesField, configureLayout((c) -> { c.gridx = 0; c.gridy = 10; c.weightx = 1; }));
        add(new JPanel(), configureLayout((c) -> { c.gridx = 0; c.gridy = 11; c.weighty = 1; }));
      } 
    }
    
    uuidField.setEnabled(false);
    
    initializeFields(initialSensor);
  }

  @Override
  protected void initializeFields(Sensor object) {
    if (object != null) {
      uuidField.setText(object.getUuid().toString());
      nameField.setText(object.getName());
      xField.setText(String.valueOf(object.getPosition().getX()));
      yField.setText(String.valueOf(object.getPosition().getY()));
      zField.setText(String.valueOf(object.getPosition().getZ()));
      descriptionField.setText(object.getDescription());

      switch (sensorType) {
        case depth -> {}
        case audio -> {
          hydrophoneIdField.setText(((AudioSensor) object).getHydrophoneId());
          preampIdField.setText(((AudioSensor) object).getPreampId());
        }
        case other -> {
          sensorTypeField.setText(((OtherSensor) object).getSensorType());
          propertiesField.setText(((OtherSensor) object).getProperties());
        }
      }
    }
  }

  @Override
  protected void save(CRUDRepository<Sensor> repository) throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    String uuidText = uuidField.getText();

    boolean update = !StringUtils.isBlank(uuidText);
    
    UUID uuid = !update ? null : UUID.fromString(uuidText);
    String name = nameField.getText();
    String description = descriptionField.getText();
    Position position = Position.builder()
        .x(Float.valueOf(xField.getText()))
        .y(Float.valueOf(yField.getText()))
        .z(Float.valueOf(zField.getText()))
        .build();
    
    Sensor sensor = switch (sensorType) {
      case other -> OtherSensor.builder()
          .uuid(uuid)
          .name(name)
          .position(position)
          .description(description)
          .sensorType(sensorTypeField.getText())
          .properties(propertiesField.getText())
          .build();
      case audio -> AudioSensor.builder()
          .uuid(uuid)
          .name(name)
          .position(position)
          .description(description)
          .hydrophoneId(hydrophoneIdField.getText())
          .preampId(preampIdField.getText())
          .build();
      case depth -> DepthSensor.builder()
          .uuid(uuid)
          .name(name)
          .position(position)
          .description(description)
          .build();
    };

    if (update) {
      repository.update(
          sensor.getUuid(),
          sensor
      );
    } else {
      repository.create(sensor);
    }
  }

  @Override
  protected void delete(CRUDRepository<Sensor> repository) throws NotFoundException, DatastoreException {
    String uuidText = uuidField.getText();

    if (!StringUtils.isBlank(uuidText)) {
      repository.delete(UUID.fromString(uuidText));
    }
  }
}
