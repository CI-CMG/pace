package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.translator.AudioSensorTranslator;
import edu.colorado.cires.pace.data.translator.DepthSensorTranslator;
import edu.colorado.cires.pace.data.translator.OtherSensorTranslator;
import edu.colorado.cires.pace.data.translator.PositionTranslator;
import edu.colorado.cires.pace.data.translator.SensorTranslator;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class SensorTranslatorForm extends BaseTranslatorForm<SensorTranslator> {
  
  private final JComboBox<String> uuidField = new JComboBox<>();
  private final JComboBox<String> nameField = new JComboBox<>();
  private final JComboBox<String> descriptionField = new JComboBox<>();
  
  private final PositionTranslatorForm positionTranslatorForm;
  
  private final SensorTranslator initialTranslator;
  private JPanel formPanel;
  
  private JComboBox<String> sensorTypeComboBox;
  
  private SensorTypeSpecificTranslatorForm<?> sensorTypeSpecificTranslatorForm = new SensorTypeSpecificTranslatorForm<>(null, new String[0]) {
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
    protected SensorTranslator toTranslator(JTextField translatorUUIDField, JTextField translatorNameField, JComboBox<String> uuidField,
        JComboBox<String> nameField, JComboBox<String> descriptionField, PositionTranslatorForm positionTranslatorForm) {
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
    this.positionTranslatorForm = new PositionTranslatorForm(initialTranslator == null ? null : initialTranslator.getPositionTranslator(), headerOptions);
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
      return new SensorTypeSpecificTranslatorForm<>(null, new String[0]) {
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
        protected SensorTranslator toTranslator(JTextField translatorUUIDField, JTextField translatorNameField, JComboBox<String> uuidField,
            JComboBox<String> nameField, JComboBox<String> descriptionField, PositionTranslatorForm positionTranslatorForm) {
          return null;
        }
      };
    }
  }

  @Override
  protected void addUniqueFields() {
    setLayout(new BorderLayout());

    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
    formPanel = new JPanel(new GridBagLayout());
    formPanel.add(new JLabel("UUID"), configureLayout(c -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    formPanel.add(uuidField, configureLayout(c -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    formPanel.add(new JLabel("Name"), configureLayout(c -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    formPanel.add(nameField, configureLayout(c -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    formPanel.add(new JLabel("Description"), configureLayout(c -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    formPanel.add(descriptionField, configureLayout(c -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
    positionTranslatorForm.setBorder(createEtchedBorder("Position"));
    formPanel.add(positionTranslatorForm, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 6; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    formPanel.add(sensorTypeSpecificTranslatorForm, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 7; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    formPanel.add(new JPanel(), configureLayout(c -> { c.gridx = 0; c.gridy = 8; c.weighty = 1; }));
    
    if (initialTranslator != null) {
      tabbedPane.add("1. Sensor Info", new JScrollPane(formPanel));
    } else {
      JPanel sensorTypePanel = getSensorTypePanel(formPanel);
      
      tabbedPane.add("1. Sensor Type", sensorTypePanel);
      tabbedPane.add("2. Sensor Info", new JScrollPane(formPanel));
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
      default -> new SensorTypeSpecificTranslatorForm<>(null, new String[0]) {
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
        protected SensorTranslator toTranslator(JTextField translatorUUIDField, JTextField translatorNameField, JComboBox<String> uuidField,
            JComboBox<String> nameField, JComboBox<String> descriptionField, PositionTranslatorForm positionTranslatorForm) {
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
    List<String> options = new ArrayList<>(List.of(
        initialTranslator.getSensorUUID(),
        initialTranslator.getSensorName(),
        initialTranslator.getDescription()
    ));

    PositionTranslator positionTranslator = initialTranslator.getPositionTranslator();
    if (positionTranslator != null) {
      options.add(positionTranslator.getX());
      options.add(positionTranslator.getY());
      options.add(positionTranslator.getZ());
    }

    if (initialTranslator instanceof AudioSensorTranslator audioSensorTranslator) {
      options.add(audioSensorTranslator.getHydrophoneId());
      options.add(audioSensorTranslator.getPreampId());
    } else if (initialTranslator instanceof OtherSensorTranslator otherSensorTranslator) {
      options.add(otherSensorTranslator.getSensorType());
      options.add(otherSensorTranslator.getProperties());
    }
    
    return options.toArray(String[]::new);
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
  protected SensorTranslator toTranslator(JTextField uuidField, JTextField nameField) {
    return sensorTypeSpecificTranslatorForm.toTranslator(
        uuidField, nameField, this.uuidField, this.nameField, descriptionField, positionTranslatorForm
    );
  }

  @Override
  protected void updateHeaderOptions(String[] options) {
    updateComboBoxModel(uuidField, options);
    updateComboBoxModel(nameField, options);
    updateComboBoxModel(descriptionField, options);
    if (positionTranslatorForm != null) {
      positionTranslatorForm.updateHeaderOptions(options);
    }
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
