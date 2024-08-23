package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;

import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.ChannelTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.DutyCycleTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.GainTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.SampleRateTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ChannelTranslatorForm extends JPanel implements AuxiliaryTranslatorForm<ChannelTranslator> {
  
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
    this.addSampleRateButton = getAddButton("Add Sample Rate", () -> addSampleRate(headerOptions, null));
    this.addDutyCycleButton = getAddButton("Add Duty Cycle", () -> addDutyCycle(headerOptions, null));
    this.addGainButton = getAddButton("Add Gain", () -> addGain(headerOptions, null));
    this.removeAction = removeAction;
    this.startTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getStartTime());
    this.endTimeForm = new TimeTranslatorForm(headerOptions, initialTranslator == null ? null : initialTranslator.getEndTime());
    startTimeForm.setName("startTime");
    endTimeForm.setName("endTime");
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    startTimeForm.setBorder(createEtchedBorder("Start Time"));
    add(startTimeForm, configureLayout(c -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    endTimeForm.setBorder(createEtchedBorder("End Time"));
    add(endTimeForm, configureLayout(c -> { c.gridx = 1; c.gridy = 0; c.weightx = 1; }));
    JPanel sampleRatesPanel = new JPanel(new GridBagLayout());
    sampleRatesPanel.setName("sampleRates");
    sampleRatesPanel.add(sampleRateTranslatorsPanel, configureLayout(c -> {
      c.gridx = 0; c.gridy = 0; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    sampleRatesPanel.add(addSampleRateButton, configureLayout(c -> {
      c.gridx = 0; c.gridy = 1; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    sampleRatesPanel.setBorder(createEtchedBorder("Sample Rates"));
    add(sampleRatesPanel, configureLayout(c -> {
      c.gridx = 0; c.gridy = 2; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    
    JPanel dutyCyclesPanel = new JPanel(new GridBagLayout());
    dutyCyclesPanel.setName("dutyCycles");
    dutyCyclesPanel.add(dutyCycleTranslatorsPanel, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 0; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    dutyCyclesPanel.add(addDutyCycleButton, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 1; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    dutyCyclesPanel.setBorder(createEtchedBorder("Duty Cycles"));
    add(dutyCyclesPanel, configureLayout(c -> {
      c.gridx = 0; c.gridy = 3; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    JPanel gainsPanel = new JPanel(new GridBagLayout());
    gainsPanel.setName("gains");
    gainsPanel.add(gainTranslatorsPanel, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 0; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    gainsPanel.add(addGainButton, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 1; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    gainsPanel.setBorder(createEtchedBorder("Gains"));
    add(gainsPanel, configureLayout(c -> {
      c.gridx = 0; c.gridy = 4; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(getRemoveButton("Remove Channel"), configureLayout(c -> {
      c.gridx = 0; c.gridy = 8; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
  }
  
  private JButton getRemoveButton(String buttonTitle) {
    JButton button = new JButton(buttonTitle);
    button.addActionListener(e -> removeAction.accept(this));
    return button;
  }
  
  private JButton getAddButton(String buttonTitle, Runnable addAction) {
    JButton button = new JButton(buttonTitle);
    button.addActionListener(e -> addAction.run());
    return button;
  }
  
  private void initializeFields(String[] headerOptions, ChannelTranslator initialTranslator) {
    if (initialTranslator != null) {
      initialTranslator.getSampleRates().forEach(
          t -> addSampleRate(headerOptions, t)
      );
      initialTranslator.getDutyCycles().forEach(
          t -> addDutyCycle(headerOptions, t)
      );
      initialTranslator.getGains().forEach(
          t -> addGain(headerOptions, t)
      );
    }
  }
  
  private void addSampleRate(String[] headerOptions, SampleRateTranslator initialTranslator) {
    SampleRateForm sampleRateForm = new SampleRateForm(headerOptions, initialTranslator, f -> {
      sampleRateTranslatorsPanel.remove(f.getParent());
      revalidate();
    });
    
    CollapsiblePanel<SampleRateForm> collapsiblePanel = new CollapsiblePanel<>(
        String.format(
            "Sample Rate %s", sampleRateTranslatorsPanel.getComponentCount() + 1
        ),
        sampleRateForm
    );
    collapsiblePanel.getContentPanel().setVisible(false);
    
    sampleRateTranslatorsPanel.add(collapsiblePanel, configureLayout(c -> {
      c.gridx = 0; c.gridy = sampleRateTranslatorsPanel.getComponentCount(); c.weightx = 1;
    }));
    revalidate();
  }
  
  private void addDutyCycle(String[] headerOptions, DutyCycleTranslator initialTranslator) {
    DutyCycleForm dutyCycleForm = new DutyCycleForm(headerOptions, initialTranslator, f -> {
      dutyCycleTranslatorsPanel.remove(f.getParent());
      revalidate();
    });
    
    CollapsiblePanel<DutyCycleForm> collapsiblePanel = new CollapsiblePanel<>(
        String.format(
            "Duty Cycle %s", dutyCycleTranslatorsPanel.getComponentCount() + 1
        ),
        dutyCycleForm
    );
    collapsiblePanel.getContentPanel().setVisible(false);
    
    dutyCycleTranslatorsPanel.add(collapsiblePanel, configureLayout(c -> {
      c.gridx = 0; c.gridy = dutyCycleTranslatorsPanel.getComponentCount(); c.weightx = 1;
    }));
    revalidate();
  }
  
  private void addGain(String[] headerOptions, GainTranslator initialTranslator) {
    GainForm gainForm = new GainForm(headerOptions, initialTranslator, f -> {
      gainTranslatorsPanel.remove(f.getParent());
      revalidate();
    });
    
    CollapsiblePanel<GainForm> collapsiblePanel = new CollapsiblePanel<>(
        String.format(
            "Gain %s", gainTranslatorsPanel.getComponentCount() + 1
        ),
        gainForm
    );
    collapsiblePanel.getContentPanel().setVisible(false);
    
    gainTranslatorsPanel.add(collapsiblePanel, configureLayout(c -> {
      c.gridx = 0; c.gridy = gainTranslatorsPanel.getComponentCount(); c.weightx = 1;
    }));
    revalidate();
  }
  
  public void updateHeaderOptions(String[] headerOptions) {
    startTimeForm.updateHeaderOptions(headerOptions);
    endTimeForm.updateHeaderOptions(headerOptions);
    Arrays.stream(sampleRateTranslatorsPanel.getComponents())
        .filter(p -> p instanceof CollapsiblePanel<?>)
        .map(p -> (CollapsiblePanel<?>) p)
        .map(p -> (SampleRateForm) p.getContentPanel())
        .forEach(p -> p.updateHeaderOptions(headerOptions));
    Arrays.stream(dutyCycleTranslatorsPanel.getComponents())
        .filter(p -> p instanceof CollapsiblePanel<?>)
        .map(p -> (CollapsiblePanel<?>) p)
        .map(p -> (DutyCycleForm) p.getContentPanel())
        .forEach(p -> p.updateHeaderOptions(headerOptions));
    Arrays.stream(gainTranslatorsPanel.getComponents())
        .filter(p -> p instanceof CollapsiblePanel<?>)
        .map(p -> (CollapsiblePanel<?>) p)
        .map(p -> (GainForm) p.getContentPanel())
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
        .startTime(startTimeForm.toTranslator())
        .endTime(endTimeForm.toTranslator())
        .sampleRates(Arrays.stream(sampleRateTranslatorsPanel.getComponents())
            .filter(p -> p instanceof CollapsiblePanel<?>)
            .map(p -> (CollapsiblePanel<?>) p)
            .map(p -> (SampleRateForm) p.getContentPanel())
            .map(SampleRateForm::toTranslator)
            .toList()
        ).dutyCycles(Arrays.stream(dutyCycleTranslatorsPanel.getComponents())
            .filter(p -> p instanceof CollapsiblePanel<?>)
            .map(p -> (CollapsiblePanel<?>) p)
            .map(p -> (DutyCycleForm) p.getContentPanel())
            .map(DutyCycleForm::toTranslator)
            .toList()
        ).gains(Arrays.stream(gainTranslatorsPanel.getComponents())
            .filter(p -> p instanceof CollapsiblePanel<?>)
            .map(p -> (CollapsiblePanel<?>) p)
            .map(p -> (GainForm) p.getContentPanel())
            .map(GainForm::toTranslator)
            .toList()
        )
        .build();
  }
}
