package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.object.sensor.audio.translator.AudioSensorTranslator;
import edu.colorado.cires.pace.data.object.sensor.depth.translator.DepthSensorTranslator;
import edu.colorado.cires.pace.data.object.sensor.other.translator.OtherSensorTranslator;
import edu.colorado.cires.pace.data.object.sensor.base.translator.SensorTranslator;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class SensorTranslatorForm extends BaseTranslatorForm<SensorTranslator> {
  
  private final JComboBox<String> uuidField = new JComboBox<>();
  private final JComboBox<String> nameField = new JComboBox<>();
  private final JComboBox<String> descriptionField = new JComboBox<>();
  
  private final SensorTranslator initialTranslator;
  private JPanel formPanel;
  
  private JComboBox<String> sensorTypeComboBox;
  
  private SensorTypeSpecificTranslatorForm<?> sensorTypeSpecificTranslatorForm = new SensorTypeSpecificTranslatorForm<>() {
    @Override
    protected void addFields() {

    }

    @Override
    protected void initializeFields(SensorTranslator initialTranslator, String[] headerOptions) {

    }

    @Override
    protected void updateHeaderOptions(String[] headerOptions) {

    }

    @Override
    protected SensorTranslator toTranslator(UUID translatorUUID, String translatorName, JComboBox<String> uuidField,
        JComboBox<String> nameField, JComboBox<String> descriptionField) {
      return null;
    }
  };

  public SensorTranslatorForm(SensorTranslator initialTranslator, String[] headerOptions) {
    super(headerOptions);
    uuidField.setName("uuid");
    nameField.setName("name");
    descriptionField.setName("description");
    headerOptions = initialTranslator == null ? headerOptions : getInitialHeaderOptions(initialTranslator);
    setHeaderOptions(headerOptions);
    this.initialTranslator = initialTranslator;
    this.sensorTypeSpecificTranslatorForm = getSensorTypeSpecificTranslatorForm(initialTranslator, headerOptions);
    addUniqueFields();
    initializeUniqueFields(initialTranslator);
  }
  
  private SensorTypeSpecificTranslatorForm<?> getSensorTypeSpecificTranslatorForm(SensorTranslator initialTranslator, String[] headerOptions) {
    if (initialTranslator instanceof AudioSensorTranslator audioSensorTranslator) {
      return new AudioSensorTranslatorForm(audioSensorTranslator, headerOptions);
    } else if (initialTranslator instanceof DepthSensorTranslator depthSensorTranslator) {
      return new DepthSensorTranslatorForm(depthSensorTranslator, headerOptions);
    } else if (initialTranslator instanceof OtherSensorTranslator otherSensorTranslator) {
      return new OtherSensorTranslatorForm(otherSensorTranslator, headerOptions);
    } else {
      return new SensorTypeSpecificTranslatorForm<>() {
        @Override
        protected void addFields() {

        }

        @Override
        protected void initializeFields(SensorTranslator initialTranslator, String[] headerOptions) {

        }

        @Override
        protected void updateHeaderOptions(String[] headerOptions) {

        }

        @Override
        protected SensorTranslator toTranslator(UUID translatorUUID, String translatorName, JComboBox<String> uuidField,
            JComboBox<String> nameField, JComboBox<String> descriptionField) {
          return null;
        }
      };
    }
  }

  @Override
  protected void addUniqueFields() {
    setLayout(new BorderLayout());

    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    formPanel = new JPanel(new GridBagLayout());
    formPanel.add(new JLabel("UUID"), configureLayout(c -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    formPanel.add(uuidField, configureLayout(c -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    formPanel.add(new JLabel("Name"), configureLayout(c -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    formPanel.add(nameField, configureLayout(c -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    formPanel.add(new JLabel("Description"), configureLayout(c -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    formPanel.add(descriptionField, configureLayout(c -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
    formPanel.add(sensorTypeSpecificTranslatorForm, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 7; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    formPanel.add(new JPanel(), configureLayout(c -> { c.gridx = 0; c.gridy = 8; c.weighty = 1; }));
    
    if (initialTranslator != null) {
      tabbedPane.add("Sensor", new JScrollPane(formPanel));
    } else {
      JPanel sensorTypePanel = getSensorTypePanel(formPanel);
      
      tabbedPane.add("Sensor Type", sensorTypePanel);
      tabbedPane.add("Sensor", new JScrollPane(formPanel));
    }
    
    add(tabbedPane, BorderLayout.CENTER);
  }
  
  private JPanel getSensorTypePanel(JPanel formPanel) {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.add(new JLabel("Sensor Type"), configureLayout(c -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    sensorTypeComboBox = getSensorTypeComboBox(formPanel, headerOptions);
    panel.add(sensorTypeComboBox, configureLayout(c -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    panel.add(new JPanel(), configureLayout(c -> { c.gridx = 0; c.gridy = 2; c.weighty = 1; }));
    return panel;
  }
  
  private JComboBox<String> getSensorTypeComboBox(JPanel panel, String[] headerOptions) {
    JComboBox<String> comboBox = new JComboBox<>(new DefaultComboBoxModel<>(new String[] {
        "Audio", "Depth", "Other"
    }));
    comboBox.setName("sensorType");
    comboBox.setSelectedItem(null);
    
    comboBox.addItemListener(e -> updateSensorTypeSpecificPanel(panel, e, headerOptions));
    
    return comboBox;
  }
  
  private void updateSensorTypeSpecificPanel(JPanel panel, ItemEvent event, String[] headerOptions) {
    String choice = (String) event.getItem();
    if (choice == null) {
      return;
    }

    SensorTypeSpecificTranslatorForm<?> newSensorTypeTranslatorForm = switch (choice) {
      case "Audio" -> new AudioSensorTranslatorForm(null, headerOptions);
      case "Other" -> new OtherSensorTranslatorForm(null, headerOptions);
      case "Depth" -> new DepthSensorTranslatorForm(null, headerOptions);
      default -> new SensorTypeSpecificTranslatorForm<>() {
        @Override
        protected void addFields() {

        }

        @Override
        protected void initializeFields(SensorTranslator initialTranslator, String[] headerOptions) {

        }

        @Override
        protected void updateHeaderOptions(String[] headerOptions) {

        }

        @Override
        protected SensorTranslator toTranslator(UUID translatorUUID, String translatorName, JComboBox<String> uuidField,
            JComboBox<String> nameField, JComboBox<String> descriptionField) {
          return null;
        }
      };
    };

    panel.remove(sensorTypeSpecificTranslatorForm);
    sensorTypeSpecificTranslatorForm = newSensorTypeTranslatorForm;
    panel.add(sensorTypeSpecificTranslatorForm, configureLayout(c -> {
      c.gridx = 0; c.gridy = 7; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    revalidate();
  }
  
  private static String[] getInitialHeaderOptions(SensorTranslator initialTranslator) {
    List<String> options = new ArrayList<>(0);
    options.add(initialTranslator.getSensorUUID());
    options.add(initialTranslator.getSensorName());
    options.add(initialTranslator.getDescription());

    if (initialTranslator instanceof AudioSensorTranslator audioSensorTranslator) {
      options.add(audioSensorTranslator.getHydrophoneId());
      options.add(audioSensorTranslator.getPreampId());
    } else if (initialTranslator instanceof OtherSensorTranslator otherSensorTranslator) {
      options.add(otherSensorTranslator.getSensorType());
      options.add(otherSensorTranslator.getProperties());
    }
    
    return options.stream().filter(Objects::nonNull).toArray(String[]::new);
  }

  @Override
  protected void initializeUniqueFields(SensorTranslator initialTranslator) {
    updateHeaderOptions(getHeaderOptions());
    if (initialTranslator != null) {
      uuidField.setSelectedItem(initialTranslator.getSensorUUID());
      nameField.setSelectedItem(initialTranslator.getSensorName());
      descriptionField.setSelectedItem(initialTranslator.getDescription());
    }
  }

  @Override
  protected SensorTranslator toTranslator(UUID uuid, String name) {
    if (sensorTypeComboBox.getSelectedItem() == null) {
      JOptionPane.showMessageDialog(this, "Please select a sensor type", "Error", JOptionPane.ERROR_MESSAGE);
      throw new RuntimeException("Sensor type not selected");
    } else {
      return sensorTypeSpecificTranslatorForm.toTranslator(
          uuid, name, this.uuidField, this.nameField, descriptionField
      );
    }
  }

  @Override
  protected void updateHeaderOptions(String[] options) {
    updateComboBoxModel(uuidField, options);
    updateComboBoxModel(nameField, options);
    updateComboBoxModel(descriptionField, options);
    if (sensorTypeSpecificTranslatorForm != null) {
      sensorTypeSpecificTranslatorForm.updateHeaderOptions(options);
    }
    if (sensorTypeComboBox != null) {
      Arrays.stream(sensorTypeComboBox.getItemListeners()).forEach(
          sensorTypeComboBox::removeItemListener
      );
      sensorTypeComboBox.addItemListener(e -> updateSensorTypeSpecificPanel(formPanel, e, options));
    }
  }
}
