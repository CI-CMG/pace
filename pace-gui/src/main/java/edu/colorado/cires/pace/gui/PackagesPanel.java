package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.packaging.PackageProcessor;
import edu.colorado.cires.pace.packaging.PackagingException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.CSVTranslatorRepository;
import edu.colorado.cires.pace.repository.ExcelTranslatorRepository;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;

public class PackagesPanel extends TranslatePanel<Package> {
  
  private static final JProgressBar progressBar = new JProgressBar();
  private final CRUDRepository<?>[] dependencyRepositories;
  private final ObjectMapper objectMapper;
  private final Path outputDir;
  private static final JButton packageButton = new JButton("Package");

  public PackagesPanel(CRUDRepository<Package> repository, String[] headers,
      Function<Package, Object[]> objectConversion,
      ExcelTranslatorRepository excelTranslatorRepository,
      CSVTranslatorRepository csvTranslatorRepository, Class<Package> clazz,
      ObjectMapper objectMapper, Path outputDir,
      CRUDRepository<?>... dependencyRepositories) {
    super(repository, headers, objectConversion, excelTranslatorRepository, csvTranslatorRepository, clazz, dependencyRepositories);
    this.outputDir = outputDir;
    this.dependencyRepositories = dependencyRepositories;
    this.objectMapper = objectMapper;
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
    packageButton.setEnabled(false);
    List<Package> packages = new ArrayList<>();
    
    for (int i = 0; i < tableModel.getRowCount(); i++) {
      Boolean selected = (Boolean) tableModel.getValueAt(i, 4);
      if (selected) {
        packages.add((Package) tableModel.getValueAt(i, 5));
      }
    }
    
    processPackages(packages);
  }
  
  private void resetTable() {
    for (int i = 0; i < tableModel.getRowCount(); i++) {
      tableModel.setValueAt(false, i, 4);
    }
  }
  
  private void processPackages(List<Package> packages) {
    new Thread(() -> {
      GUIProgressIndicator progressIndicator = new GUIProgressIndicator(progressBar);
      
      try {
        PackageProcessor packageProcessor = new PackageProcessor(
            objectMapper,
            getRepository(Person.class).findAll().toList(),
            getRepository(Organization.class).findAll().toList(),
            getRepository(Project.class).findAll().toList(),
            packages,
            outputDir,
            progressIndicator
        );

        packageProcessor.process();
      } catch (DatastoreException | IOException | PackagingException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      } finally {
        progressIndicator.indicateStatus(0);
        resetTable();
        packageButton.setEnabled(true);
      }
      
    }).start();
  }
  
  private <O extends ObjectWithUniqueField> CRUDRepository<O> getRepository(Class<O> clazz) {
    return Arrays.stream(dependencyRepositories)
        .map(r -> (CRUDRepository<?>) r)
        .filter(r -> r.getClassName().equals(clazz.getSimpleName()))
        .map(r ->(CRUDRepository<O>) r)
        .findFirst().orElseThrow(
            () -> new IllegalStateException(String.format(
                "Repository not found for %s", clazz.getSimpleName()
            ))
        );
  }
  
  private static class PackageTableModel extends DefaultTableModel {

    public PackageTableModel(Object[][] data, Object[] columnNames) {
      super(data, columnNames);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
      return switch (columnIndex) {
        case 0 -> UUID.class;
        case 4 -> Boolean.class;
        case 5 -> Package.class;
        default -> String.class;
      };
    }
  }
}
