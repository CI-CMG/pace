package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.sensor.audio.AudioSensor;
import edu.colorado.cires.pace.data.object.sensor.depth.DepthSensor;
import edu.colorado.cires.pace.data.object.sensor.other.OtherSensor;
import edu.colorado.cires.pace.data.object.sensor.base.Sensor;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.translator.SensorType;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.UUID;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SensorForm extends ObjectWithNameForm<Sensor> {
  
  private static Sensor initialSensor;
  
  private JTextField descriptionField;
  private JTextField hydrophoneIdField;
  private JTextField preampIdField;
  private JTextField sensorTypeField;
  private JTextField propertiesField;
  
  private SensorType sensorType;
  
  private JPanel specificFieldsPanel;

  private SensorForm(Sensor initialSensor) {
    super(initialSensor);
  }
  
  public static SensorForm create(Sensor sensor) {
    initialSensor = sensor;
    return new SensorForm(sensor);
  }
  
  private JComboBox<String> createSensorTypeComboBox() {
    JComboBox<String> comboBox = new JComboBox<>(new DefaultComboBoxModel<>(
        Arrays.stream(SensorType.values())
            .map(SensorType::name)
            .toArray(String[]::new)
    ));
    comboBox.setSelectedItem(null);
    
    comboBox.addItemListener(e -> {
      specificFieldsPanel.removeAll();
      
      String sensorType = (String) e.getItem();
      addSpecificFields(SensorType.valueOf(sensorType));
      
      revalidate();
      repaint();
    });
    
    return comboBox;
  }
  
  private void addSpecificFields(SensorType sensorType) {
    this.sensorType = sensorType;
    switch (sensorType) {
      case audio -> {
        specificFieldsPanel.add(new JLabel("Hydrophone ID"), configureLayout((c) -> { c.gridx = 0; c.gridy = 7; c.weightx = 1; }));
        specificFieldsPanel.add(hydrophoneIdField, configureLayout((c) -> { c.gridx = 0; c.gridy = 8; c.weightx = 1; }));
        specificFieldsPanel.add(new JLabel("Preamp ID"), configureLayout((c) -> { c.gridx = 0; c.gridy = 9; c.weightx = 1; }));
        specificFieldsPanel.add(preampIdField, configureLayout((c) -> { c.gridx = 0; c.gridy = 10; c.weightx = 1; }));
        specificFieldsPanel.add(new JPanel(), configureLayout((c) -> { c.gridx = 0; c.gridy = 11; c.weighty = 1; }));
      }
      case depth -> specificFieldsPanel.add(new JPanel(), configureLayout((c) -> {
        c.gridx = 0;
        c.gridy = 7;
        c.weighty = 1;
      }));
      case other -> {
        specificFieldsPanel.add(new JLabel("Sensor Type"), configureLayout((c) -> { c.gridx = 0; c.gridy = 7; c.weightx = 1; }));
        specificFieldsPanel.add(sensorTypeField, configureLayout((c) -> { c.gridx = 0; c.gridy = 8; c.weightx = 1; }));
        specificFieldsPanel.add(new JLabel("Properties"), configureLayout((c) -> { c.gridx = 0; c.gridy = 9; c.weightx = 1; }));
        specificFieldsPanel.add(propertiesField, configureLayout((c) -> { c.gridx = 0; c.gridy = 10; c.weightx = 1; }));
        specificFieldsPanel.add(new JPanel(), configureLayout((c) -> { c.gridx = 0; c.gridy = 11; c.weighty = 1; }));
      }
    }
  }

  @Override
  protected Sensor objectFromFormFields(UUID uuid, String uniqueField, boolean visible) {
    String description = descriptionField.getText();

    return switch (sensorType) {
      case other -> OtherSensor.builder()
          .uuid(uuid)
          .name(uniqueField)
          .description(description)
          .sensorType(sensorTypeField.getText())
          .properties(propertiesField.getText())
          .visible(visible)
          .build();
      case audio -> AudioSensor.builder()
          .uuid(uuid)
          .name(uniqueField)
          .description(description)
          .hydrophoneId(hydrophoneIdField.getText())
          .preampId(preampIdField.getText())
          .visible(visible)
          .build();
      case depth -> DepthSensor.builder()
          .uuid(uuid)
          .name(uniqueField)
          .description(description)
          .visible(visible)
          .build();
    };
  }

  @Override
  protected void addAdditionalFields(JPanel contentPanel, CRUDRepository<?>... dependencyRepositories) {
    descriptionField = new JTextField();
    hydrophoneIdField = new JTextField();
    preampIdField = new JTextField();
    sensorTypeField = new JTextField();
    propertiesField = new JTextField();

    specificFieldsPanel = new JPanel(new GridBagLayout());
    
    if (initialSensor == null) {
      JPanel sensorTypePanel = new JPanel(new GridBagLayout());
      sensorTypePanel.add(new JLabel("Sensor Type"), configureLayout(c -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
      sensorTypePanel.add(createSensorTypeComboBox(), configureLayout(c -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
      contentPanel.add(sensorTypePanel, configureLayout(c -> { c.gridx = 0; c.gridy = contentPanel.getComponentCount(); c.weightx = 1; }));
    } else {
      setSensorType(initialSensor);
      addSpecificFields(sensorType);
    }
    
    contentPanel.add(new JLabel("Description"), configureLayout((c) -> { c.gridx = 0; c.gridy = contentPanel.getComponentCount(); c.weightx = 1; }));
    contentPanel.add(descriptionField, configureLayout((c) -> { c.gridx = 0; c.gridy = contentPanel.getComponentCount(); c.weightx = 1; }));
    contentPanel.add(specificFieldsPanel, configureLayout(c -> { c.gridx = 0; c.gridy = contentPanel.getComponentCount(); c.weightx = 1; }));
  }

  @Override
  protected void initializeAdditionalFields(Sensor object, CRUDRepository<?>... dependencyRepositories) {
    descriptionField.setText(object.getDescription());

    if (object instanceof AudioSensor audioSensor) {
      hydrophoneIdField.setText((audioSensor).getHydrophoneId());
      preampIdField.setText((audioSensor).getPreampId());
    } else if (object instanceof OtherSensor otherSensor) {
      sensorTypeField.setText((otherSensor).getSensorType());
      propertiesField.setText((otherSensor).getProperties());
    }
  }

  private void setSensorType(Sensor object) {
    if (object instanceof AudioSensor) {
      sensorType = SensorType.audio;
    } else if (object instanceof OtherSensor) {
      sensorType = SensorType.other;
    } else {
      sensorType = SensorType.depth;
    }
  }
}
