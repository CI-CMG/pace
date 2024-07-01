package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.translator.PackageTranslator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.packaging.PackageProcessor;
import edu.colorado.cires.pace.packaging.PackagingException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.OrganizationRepository;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.pace.repository.ProjectRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.converter.Converter;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.StringUtils;

public class PackagesPanel extends TranslatePanel<Package, PackageTranslator> {
  
  private static final JProgressBar progressBar = new JProgressBar();
  private final ObjectMapper objectMapper;
  private static final JButton packageButton = new JButton("Package");
  
  private final PersonRepository personRepository;
  private final OrganizationRepository organizationRepository;
  private final ProjectRepository projectRepository;

  public PackagesPanel(CRUDRepository<Package> repository, String[] headers,
      Function<Package, Object[]> objectConversion,
      Class<Package> clazz,
      ObjectMapper objectMapper,
      TranslatorRepository translatorRepository,
      Converter<PackageTranslator, Package> converter, PersonRepository personRepository, OrganizationRepository organizationRepository,
      ProjectRepository projectRepository) {
    super(repository, headers, objectConversion, clazz, translatorRepository, converter, PackageTranslator.class);
    this.objectMapper = objectMapper;
    this.personRepository = personRepository;
    this.organizationRepository = organizationRepository;
    this.projectRepository = projectRepository;
  }

  @Override
  protected DefaultTableModel createTableModel(String[] headers) {
    return new PackageTableModel(null, headers);
  }

  @Override
  protected JPanel createControlPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    
    panel.add(progressBar, configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    
    JPanel buttonPanel = new JPanel(new GridBagLayout());
    JButton translateButton = new JButton("Translate");
    buttonPanel.add(translateButton, configureLayout((c) -> { c.gridx = 0; c.gridy = 0; c.weightx = 0; }));
    buttonPanel.add(new JPanel(), configureLayout((c) -> { c.gridx = 1; c.gridy = 0; c.weightx = 1; }));
    buttonPanel.add(packageButton, configureLayout((c) -> { c.gridx = 2; c.gridy = 0; c.weightx = 0; }));
    panel.add(buttonPanel, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    
    packageButton.addActionListener((e) -> packageSelectedRows());
    
    translateButton.addActionListener((e) -> {
      try {
        createTranslateForm();
      } catch (DatastoreException ex) {
        throw new RuntimeException(ex);
      }
    });
    
    return panel;
  }

  private void packageSelectedRows() {
    List<Package> packages = new ArrayList<>();
    
    for (int i = 0; i < tableModel.getRowCount(); i++) {
      Boolean selected = (Boolean) tableModel.getValueAt(i, 6);
      if (selected) {
        packages.add((Package) tableModel.getValueAt(i, 7));
      }
    }
    
    processPackages(packages);
  }
  
  private void resetTable() {
    for (int i = 0; i < tableModel.getRowCount(); i++) {
      tableModel.setValueAt(false, i, 6);
    }
  }
  
  private void processPackages(List<Package> packages) {
    JPanel chooseDestinationPanel = new JPanel(new GridBagLayout());
    chooseDestinationPanel.add(new JLabel("Destination"), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    JTextField destinationField = new JTextField();
    destinationField.setEditable(false);
    chooseDestinationPanel.add(destinationField, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    JButton chooseDestinationButton = new JButton("Choose Directory");
    chooseDestinationPanel.add(chooseDestinationButton, configureLayout((c) -> { c.gridx = 2; c.gridy = 1; c.weightx = 0; }));
    
    chooseDestinationPanel.add(new JPanel(), configureLayout((c) -> { c.gridy = 2; c.gridx = 0; c.weightx = c.weighty = 1; }));
    
    JPanel submitPanel = new JPanel(new BorderLayout());
    JButton submitButton = new JButton("Submit");
    submitPanel.add(submitButton, BorderLayout.EAST);
    chooseDestinationPanel.add(submitPanel, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.gridwidth = GridBagConstraints.REMAINDER; }));
    
    chooseDestinationButton.addActionListener((e) -> {
      JFileChooser chooser = new JFileChooser();
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      chooser.setDialogTitle("Select Output Directory");
      if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        File selectedFile = chooser.getSelectedFile();
        destinationField.setText(selectedFile.toString());
      }
    });
    
    JDialog chooseDestinationDialog = new JDialog();
    chooseDestinationDialog.setTitle("Choose Destination");
    chooseDestinationDialog.setModal(true);
    chooseDestinationDialog.setLocationRelativeTo(this);
    chooseDestinationDialog.add(chooseDestinationPanel);

    submitButton.addActionListener((e) -> {
      String destinationText = destinationField.getText();
      if (StringUtils.isBlank(destinationText)) {
        JOptionPane.showMessageDialog(this, "Choose a destination directory", "Error", JOptionPane.ERROR_MESSAGE);
      } else {
        packageButton.setEnabled(false);
        
        new Thread(() -> {
          GUIProgressIndicator progressIndicator = new GUIProgressIndicator(progressBar);

          try {
            PackageProcessor packageProcessor = new PackageProcessor(
                objectMapper,
                personRepository.findAll().toList(),
                organizationRepository.findAll().toList(),
                projectRepository.findAll().toList(),
                packages,
                Paths.get(destinationField.getText()),
                progressIndicator
            );

            packageProcessor.process();
          } catch (DatastoreException | IOException | PackagingException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          } finally {
            progressIndicator.indicateStatus(0);
            resetTable();
            packageButton.setEnabled(true);
          }

        }).start();

        chooseDestinationDialog.dispose();
      }
    });
    
    chooseDestinationDialog.pack();
    chooseDestinationDialog.setVisible(true);
  }
  
  private static class PackageTableModel extends DefaultTableModel {

    public PackageTableModel(Object[][] data, Object[] columnNames) {
      super(data, columnNames);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
      return switch (columnIndex) {
        case 0 -> UUID.class;
        case 6 -> Boolean.class;
        case 7 -> Package.class;
        default -> String.class;
      };
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return column == 6;
    }
  }
}
