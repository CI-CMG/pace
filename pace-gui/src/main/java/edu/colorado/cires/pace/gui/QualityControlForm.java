package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createEtchedBorder;
import static edu.colorado.cires.pace.gui.UIUtils.updateComboBoxModel;

import edu.colorado.cires.pace.data.translator.DataQualityEntryTranslator;
import edu.colorado.cires.pace.data.translator.QualityControlDetailTranslator;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class QualityControlForm extends JPanel {
  
  private final JComboBox<String> qualityAnalystField = new JComboBox<>();
  private final JComboBox<String> qualityAnalysisObjectivesField = new JComboBox<>();
  private final JComboBox<String> qualityAnalysisMethodField = new JComboBox<>();
  private final JComboBox<String> qualityAssessmentDescriptionField = new JComboBox<>();
  private final JPanel qualityEntryTranslatorsPanel = new JPanel(new GridBagLayout());
  
  private final JButton addEntryButton;

  public QualityControlForm(String[] headerOptions, QualityControlDetailTranslator initialTranslator) {
    this.addEntryButton = getAddEntryButton(headerOptions);
    addFields();
    initializeFields(headerOptions, initialTranslator);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    
    add(new JLabel("Quality Analyst"), configureLayout(c -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    add(qualityAnalystField, configureLayout(c -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    add(new JLabel("Quality Analysis Objectives"), configureLayout(c -> { c.gridx = 0; c.gridy = 2; c.weightx = 1; }));
    add(qualityAnalysisObjectivesField, configureLayout(c -> { c.gridx = 0; c.gridy = 3; c.weightx = 1; }));
    add(new JLabel("Quality Analysis Method"), configureLayout(c -> { c.gridx = 0; c.gridy = 4; c.weightx = 1; }));
    add(qualityAnalysisMethodField, configureLayout(c -> { c.gridx = 0; c.gridy = 5; c.weightx = 1; }));
    add(new JLabel("Quality Assessment Description"), configureLayout(c -> { c.gridx = 0; c.gridy = 6; c.weightx = 1; }));
    add(qualityAssessmentDescriptionField, configureLayout(c -> { c.gridx = 0; c.gridy = 7; c.weightx = 1; }));
    
    JPanel entriesPanel = new JPanel(new GridBagLayout());
    entriesPanel.add(qualityEntryTranslatorsPanel, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 0; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    entriesPanel.add(addEntryButton, configureLayout(c -> { 
      c.gridx = 0; c.gridy = 1; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    entriesPanel.setBorder(createEtchedBorder("Entries"));
    add(entriesPanel, configureLayout(c -> {
      c.gridx = 0; c.gridy = 9; c.weightx = 1; c.gridwidth = GridBagConstraints.REMAINDER;
    }));
    add(new JPanel(), configureLayout(c -> { c.gridx = 0; c.gridy = 10; c.weighty = 1; }));
  }
  
  private void initializeFields(String[] headerOptions, QualityControlDetailTranslator initialTranslator) {
    updateComboBoxModel(qualityAnalystField, headerOptions);
    updateComboBoxModel(qualityAnalysisObjectivesField, headerOptions);
    updateComboBoxModel(qualityAnalysisMethodField, headerOptions);
    updateComboBoxModel(qualityAssessmentDescriptionField, headerOptions);
    
    if (initialTranslator != null) {
      qualityAnalystField.setSelectedItem(initialTranslator.getQualityAnalyst());
      qualityAnalysisObjectivesField.setSelectedItem(initialTranslator.getQualityAnalysisObjectives());
      qualityAnalysisMethodField.setSelectedItem(initialTranslator.getQualityAnalysisMethod());
      qualityAssessmentDescriptionField.setSelectedItem(initialTranslator.getQualityAssessmentDescription());
      initialTranslator.getQualityEntryTranslators().forEach(
          t -> addQualityEntry(headerOptions, t)
      );
    }
  }
  
  private JButton getAddEntryButton(String[] headerOptions) {
    JButton button = new JButton("Add Entry");
    button.addActionListener(e -> addQualityEntry(headerOptions, null));
    return button;
  }
  
  private void addQualityEntry(String[] headerOptions, DataQualityEntryTranslator initialTranslator) {
    QualityEntryForm qualityEntryForm = new QualityEntryForm(headerOptions, initialTranslator, (p) -> {
      qualityEntryTranslatorsPanel.remove(p.getParent());
      revalidate();
    });
    
    CollapsiblePanel<QualityEntryForm> collapsiblePanel = new CollapsiblePanel<>(
        String.format(
            "Entry %s", qualityEntryTranslatorsPanel.getComponentCount() + 1
        ),
        qualityEntryForm
    );
    collapsiblePanel.getContentPanel().setVisible(false);
    
    qualityEntryTranslatorsPanel.add(collapsiblePanel, configureLayout(c -> {
      c.gridx = 0; c.gridy = qualityEntryTranslatorsPanel.getComponentCount(); c.weightx = 1;
    }));
    revalidate();
  }
  
  public void updateHeaderOptions(String[] headerOptions) {
    updateComboBoxModel(qualityAnalystField, headerOptions);
    updateComboBoxModel(qualityAnalysisObjectivesField, headerOptions);
    updateComboBoxModel(qualityAnalysisMethodField, headerOptions);
    updateComboBoxModel(qualityAssessmentDescriptionField, headerOptions);

    Arrays.stream(qualityEntryTranslatorsPanel.getComponents())
        .filter(p -> p instanceof CollapsiblePanel<?>)
        .map(p -> (CollapsiblePanel<?>) p)
        .map(p -> (QualityEntryForm) p.getContentPanel())
        .forEach(p -> p.updateHeaderOptions(headerOptions));

    Arrays.stream(addEntryButton.getActionListeners()).forEach(
        addEntryButton::removeActionListener
    );
    addEntryButton.addActionListener(e -> addQualityEntry(headerOptions, null));
  }
  
  public QualityControlDetailTranslator toTranslator() {
    return QualityControlDetailTranslator.builder()
        .qualityAnalyst((String) qualityAnalystField.getSelectedItem())
        .qualityAnalysisObjectives((String) qualityAnalysisObjectivesField.getSelectedItem())
        .qualityAnalysisMethod((String) qualityAnalysisMethodField.getSelectedItem())
        .qualityAssessmentDescription((String) qualityAssessmentDescriptionField.getSelectedItem())
        .qualityEntryTranslators(Arrays.stream(qualityEntryTranslatorsPanel.getComponents())
            .filter(p -> p instanceof CollapsiblePanel<?>)
            .map(p -> (CollapsiblePanel<?>) p)
            .map(p -> (QualityEntryForm) p.getContentPanel())
            .map(QualityEntryForm::toTranslator)
            .toList()
        )
        .build();
  }
}
