package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;
import static edu.colorado.cires.pace.gui.UIUtils.createSquareInsets;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.translator.ChannelTranslator;
import edu.colorado.cires.pace.data.translator.DutyCycleTranslator;
import edu.colorado.cires.pace.data.translator.GainTranslator;
import edu.colorado.cires.pace.data.translator.SampleRateTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChannelTranslatorForm extends JPanel {
  
  private static final int INSET_SIZE = 10;
  
  private final JComboBox<String> sensorField = new JComboBox<>();
  private final TimeTranslatorForm startTimeForm;
  private final TimeTranslatorForm endTimeForm;
  private final JPanel sampleRateTranslatorsPanel = new JPanel(new GridBagLayout());
  private final JPanel dutyCycleTranslatorsPanel = new JPanel(new GridBagLayout());
  private final JPanel gainTranslatorsPanel = new JPanel(new GridBagLayout());
  
  private final JButton addSampleRateButton;
  private final JButton addDutyCycleButton;
  private final JButton addGainButton;
  
  private final Consumer<ChannelTranslatorForm> removeAction;

  public ChannelTranslatorForm(String[] headerOptions, ChannelTranslator initialTranslator, Consumer<ChannelTranslatorForm> removeAction) {
    this.addSampleRateButton = getAddButton(() -> addSampleRate(headerOptions, null));
    this.addDutyCycleButton = getAddButton(() -> addDutyCycle(headerOptions, null));
    this.addGainButton = getAddButton(() -> addGain(headerOptions, null));
    this.removeAction = removeAction;
    this.startTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getStartTimeTranslator());
    this.endTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getEndTimeTranslator());
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Sensor"), configureLayout(c -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    add(sensorField, configureLayout(c -> {
      c.gridx = 0; c.gridy = 1; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    startTimeForm.setBorder(createEtchedBorder("Start Time"));
    add(startTimeForm, configureLayout(c -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    endTimeForm.setBorder(createEtchedBorder("End Time"));
    add(endTimeForm, configureLayout(c -> { c.gridx = 1; c.gridy = 2; c.weightx = 1; }));
    sampleRateTranslatorsPanel.setBorder(createEtchedBorder("Sample Rates"));
    add(sampleRateTranslatorsPanel, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 3; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(addSampleRateButton, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 4; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    dutyCycleTranslatorsPanel.setBorder(createEtchedBorder("Duty Cycles"));
    add(dutyCycleTranslatorsPanel, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 5; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(addDutyCycleButton, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 6; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    gainTranslatorsPanel.setBorder(createEtchedBorder("Gain Translators"));
    add(gainTranslatorsPanel, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 7; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(addGainButton, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 8; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(getRemoveButton(), configureLayout(c -> {
      c.gridx = 0; c.gridy = 9; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
  }
  
  private JButton getRemoveButton() {
    JButton button = new JButton("Remove");
    button.addActionListener(e -> removeAction.accept(this));
    return button;
  }
  
  private JButton getAddButton(Runnable addAction) {
    JButton button = new JButton("Add");
    button.addActionListener(e -> addAction.run());
    return button;
  }
  
  private void initializeFields(String[] headerOptions, ChannelTranslator initialTranslator) {
    updateComboBoxModel(sensorField, headerOptions);
    
    if (initialTranslator != null) {
      sensorField.setSelectedItem(initialTranslator.getSensor());
      initialTranslator.getSampleRateTranslators().forEach(
          t -> addSampleRate(headerOptions, t)
      );
      initialTranslator.getDutyCycleTranslators().forEach(
          t -> addDutyCycle(headerOptions, t)
      );
      initialTranslator.getGainTranslators().forEach(
          t -> addGain(headerOptions, t)
      );
    }
  }
  
  private void addSampleRate(String[] headerOptions, SampleRateTranslator initialTranslator) {
    SampleRateForm sampleRateForm = new SampleRateForm(headerOptions, initialTranslator, f -> {
      sampleRateTranslatorsPanel.remove(f);
      revalidate();
    });
    sampleRateForm.setBorder(createEtchedBorder(String.format(
        "#%s", sampleRateTranslatorsPanel.getComponentCount() + 1
    )));
    
    sampleRateTranslatorsPanel.add(sampleRateForm, configureLayout(c -> {
      c.gridx = 0; c.gridy = sampleRateTranslatorsPanel.getComponentCount(); c.weightx = 1; c.insets = createSquareInsets(INSET_SIZE);
    }));
    revalidate();
  }
  
  private void addDutyCycle(String[] headerOptions, DutyCycleTranslator initialTranslator) {
    DutyCycleForm dutyCycleForm = new DutyCycleForm(headerOptions, initialTranslator, f -> {
      dutyCycleTranslatorsPanel.remove(f);
      revalidate();
    });
    dutyCycleForm.setBorder(createEtchedBorder(String.format(
        "#%s", dutyCycleTranslatorsPanel.getComponentCount() + 1
    )));
    
    dutyCycleTranslatorsPanel.add(dutyCycleForm, configureLayout(c -> {
      c.gridx = 0; c.gridy = dutyCycleTranslatorsPanel.getComponentCount(); c.weightx = 1; c.insets = createSquareInsets(INSET_SIZE);
    }));
    revalidate();
  }
  
  private void addGain(String[] headerOptions, GainTranslator initialTranslator) {
    GainForm gainForm = new GainForm(headerOptions, initialTranslator, f -> {
      gainTranslatorsPanel.remove(f);
      revalidate();
    });
    gainForm.setBorder(createEtchedBorder(String.format(
        "#%s", gainTranslatorsPanel.getComponentCount() + 1
    )));
    
    gainTranslatorsPanel.add(gainForm, configureLayout(c -> {
      c.gridx = 0; c.gridy = gainTranslatorsPanel.getComponentCount(); c.weightx = 1; c.insets = createSquareInsets(INSET_SIZE);
    }));
    revalidate();
  }
  
  public void updateHeaderOptions(String[] headerOptions) {
    updateComboBoxModel(sensorField, headerOptions);
    startTimeForm.updateHeaderOptions(headerOptions);
    endTimeForm.updateHeaderOptions(headerOptions);
    Arrays.stream(sampleRateTranslatorsPanel.getComponents())
        .filter(p -> p instanceof SampleRateForm)
        .map(p -> (SampleRateForm) p)
        .forEach(p -> p.updateHeaderOptions(headerOptions));
    Arrays.stream(dutyCycleTranslatorsPanel.getComponents())
        .filter(p -> p instanceof DutyCycleForm)
        .map(p -> (DutyCycleForm) p)
        .forEach(p -> p.updateHeaderOptions(headerOptions));
    Arrays.stream(gainTranslatorsPanel.getComponents())
        .filter(p -> p instanceof GainForm)
        .map(p -> (GainForm) p)
        .forEach(p -> p.updateHeaderOptions(headerOptions));

    Arrays.stream(addSampleRateButton.getActionListeners())
      .forEach(addSampleRateButton::removeActionListener);
    Arrays.stream(addDutyCycleButton.getActionListeners())
      .forEach(addDutyCycleButton::removeActionListener);
    Arrays.stream(addGainButton.getActionListeners())
      .forEach(addGainButton::removeActionListener);

    addSampleRateButton.addActionListener(l -> addSampleRate(headerOptions, null));
    addDutyCycleButton.addActionListener(l -> addDutyCycle(headerOptions, null));
    addGainButton.addActionListener(l -> addGain(headerOptions, null));
  }
  
  public ChannelTranslator toTranslator() {
    return ChannelTranslator.builder()
        .sensor((String) sensorField.getSelectedItem())
        .startTimeTranslator(startTimeForm.toTranslator())
        .endTimeTranslator(endTimeForm.toTranslator())
        .sampleRateTranslators(Arrays.stream(sampleRateTranslatorsPanel.getComponents())
            .filter(p -> p instanceof SampleRateForm)
            .map(p -> (SampleRateForm) p)
            .map(SampleRateForm::toTranslator)
            .toList()
        ).dutyCycleTranslators(Arrays.stream(dutyCycleTranslatorsPanel.getComponents())
            .filter(p -> p instanceof DutyCycleForm)
            .map(p -> (DutyCycleForm) p)
            .map(DutyCycleForm::toTranslator)
            .toList()
        ).gainTranslators(Arrays.stream(gainTranslatorsPanel.getComponents())
            .filter(p -> p instanceof GainForm)
            .map(p -> (GainForm) p)
            .map(GainForm::toTranslator)
            .toList()
        )
        .build();
  }
}
