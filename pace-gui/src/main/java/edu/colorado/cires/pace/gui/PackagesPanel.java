package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureFormLayout;
import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.packaging.PackageProcessor;
import edu.colorado.cires.pace.packaging.PackagingException;
import edu.colorado.cires.pace.packaging.PassivePackerFactory;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.repository.OrganizationRepository;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.pace.repository.ProjectRepository;
import edu.colorado.cires.pace.repository.SensorRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.converter.Converter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.apache.commons.lang3.StringUtils;

public class PackagesPanel extends TranslatePanel<Package, PackageTranslator> {
  
  private static final int PACKAGING_SELECTED_COLUMN = 7;
  private static final int VISIBLE_COLUMN = 8;
  private static final int PACKAGE_COLUMN = 9;
  private static final int DELETE_COLUMN = 10;
  
  private static final JProgressBar progressBar = new JProgressBar();
  private final ObjectMapper objectMapper;
  private static final JButton actionButton = new JButton();
  private static final JButton selectAllButton = new JButton("Select All");
  private static final JButton deselectAllButton = new JButton("Deselect All");
  
  private final PersonRepository personRepository;
  private final OrganizationRepository organizationRepository;
  private final ProjectRepository projectRepository;
  private final SensorRepository sensorRepository;
  private final DetectionTypeRepository detectionTypeRepository;

  public PackagesPanel(CRUDRepository<Package> repository, String[] headers,
      Function<Package, Object[]> objectConversion,
      Class<Package> clazz,
      ObjectMapper objectMapper,
      TranslatorRepository translatorRepository,
      Converter<PackageTranslator, Package> converter, PersonRepository personRepository, OrganizationRepository organizationRepository,
      ProjectRepository projectRepository,
      SensorRepository sensorRepository, DetectionTypeRepository detectionTypeRepository) {
    super("packagesPanel", repository, headers, objectConversion, clazz, translatorRepository, converter, PackageTranslator.class);
    
    this.objectMapper = objectMapper;
    this.personRepository = personRepository;
    this.organizationRepository = organizationRepository;
    this.projectRepository = projectRepository;
    this.sensorRepository = sensorRepository;
    this.detectionTypeRepository = detectionTypeRepository;
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
    buttonPanel.add(selectAllButton, configureLayout((c) -> { c.gridx = 2; c.gridy = 0; c.weightx = 0; }));
    buttonPanel.add(deselectAllButton, configureLayout((c) -> { c.gridx = 3; c.gridy = 0; c.weightx = 0; }));
    buttonPanel.add(actionButton, configureLayout((c) -> { c.gridx = 4; c.gridy = 0; c.weightx = 0; }));
    panel.add(buttonPanel, configureLayout((c) -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    
    translateButton.addActionListener((e) -> {
      try {
        createTranslateForm();
      } catch (DatastoreException ex) {
        throw new RuntimeException(ex);
      }
    });
    
    return panel;
  }

  @Override
  protected String getHumanReadableUniqueFieldName() {
    return "package id";
  }

  private void packageSelectedRows() {
    List<Package> packages = new ArrayList<>();
    
    for (int i = 0; i < tableModel.getRowCount(); i++) {
      Boolean selected = (Boolean) tableModel.getValueAt(i, PACKAGING_SELECTED_COLUMN);
      if (selected) {
        packages.add((Package) tableModel.getValueAt(i, PACKAGE_COLUMN));
      }
    }
    
    processPackages(packages);
  }

  private void selectSelectedPackages() {
    trueBooleanTableValue(PACKAGING_SELECTED_COLUMN);
  }
  private void deselectSelectedPackages() {
    falseBooleanTableValue(PACKAGING_SELECTED_COLUMN);
  }

  private void selectPackageVisibilities() {
    trueBooleanTableValue(VISIBLE_COLUMN);
  }
  private void deselectPackageVisibilities() {
    falseBooleanTableValue(VISIBLE_COLUMN);
  }
  
  private void selectPackageDeleteStatuses(){
    trueBooleanTableValue(DELETE_COLUMN);
  }
  private void deselectPackageDeleteStatuses(){
    falseBooleanTableValue(DELETE_COLUMN);
  }
  
  private void trueBooleanTableValue(int columnIndex) {
    Boolean newValue = null;
    for (int i = 0; i < tableModel.getRowCount(); i++) {
      tableModel.setValueAt(true, i, columnIndex);
    }
  }
  private void falseBooleanTableValue(int columnIndex) {
    Boolean newValue = null;
    for (int i = 0; i < tableModel.getRowCount(); i++) {
      tableModel.setValueAt(false, i, columnIndex);
    }
  }

  private void saveRowVisibility() {
    for (int i = 0; i < tableModel.getRowCount(); i++) {
      Boolean selected = (Boolean) tableModel.getValueAt(i, VISIBLE_COLUMN);
      Package p = (Package) tableModel.getValueAt(i, PACKAGE_COLUMN);
      Package packageToUpdate = p.setVisible(selected);

      if (p.isVisible() != packageToUpdate.isVisible()) {
        try {
          repository.update(packageToUpdate.getUuid(), packageToUpdate);
        } catch (DatastoreException | ConflictException | NotFoundException | BadArgumentException e) {
          JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    }
    
    searchData();
  }

  private void deleteSelectedRows() {
    List<UUID> uuidsToDelete = new ArrayList<>(0);
    for (int i = 0; i < tableModel.getRowCount(); i++) {
      Boolean selected = (Boolean) tableModel.getValueAt(i, DELETE_COLUMN);
      Package p = (Package) tableModel.getValueAt(i, PACKAGE_COLUMN);

      if (selected) {
        uuidsToDelete.add(p.getUuid());
      }
    }
    
    if (!uuidsToDelete.isEmpty()) {
      int result = JOptionPane.showConfirmDialog(this, String.format(
          "Are you sure you want to proceed? This action cannot be undone. %s packages will be deleted", uuidsToDelete.size()
      ), "Confirm Deletion", JOptionPane.YES_NO_OPTION);
      if (result == JOptionPane.YES_OPTION) {
        try {
          for (UUID uuid : uuidsToDelete) {
            repository.delete(uuid);
          }
        } catch (DatastoreException | NotFoundException | BadArgumentException e) {
          JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        searchData();
      }
    }
  }
  
  private void resetTable() {
    for (int i = 0; i < tableModel.getRowCount(); i++) {
      tableModel.setValueAt(false, i, PACKAGING_SELECTED_COLUMN);
      Package p = (Package) tableModel.getValueAt(i, PACKAGE_COLUMN);
      tableModel.setValueAt(p.isVisible(), i, VISIBLE_COLUMN);
      tableModel.setValueAt(false, i, DELETE_COLUMN);
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
    Dimension size = UIUtils.getPercentageOfWindowDimension(0.5, 0.4);
    chooseDestinationDialog.setSize(size);
    chooseDestinationDialog.setPreferredSize(size);
    chooseDestinationDialog.setTitle("Choose Destination");
    chooseDestinationDialog.setModal(true);
    chooseDestinationDialog.setLocationRelativeTo(this);
    chooseDestinationDialog.add(chooseDestinationPanel);

    PassivePackerFactory passivePackerFactory = new PassivePackerFactory(
        personRepository, organizationRepository, sensorRepository, detectionTypeRepository
    );

    submitButton.addActionListener((e) -> {
      String destinationText = destinationField.getText();
      if (StringUtils.isBlank(destinationText)) {
        JOptionPane.showMessageDialog(this, "Choose a destination directory", "Error", JOptionPane.ERROR_MESSAGE);
      } else {
        actionButton.setEnabled(false);
        selectAllButton.setEnabled(false);
        deselectAllButton.setEnabled(false);
        
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
                passivePackerFactory,
                progressIndicator
            );

            List<Package> processedPackages = packageProcessor.process().stream()
                .filter(p -> Objects.nonNull(p.getUuid()))
                .toList();
            for (Package processedPackage : processedPackages) {
              repository.update(processedPackage.getUuid(), processedPackage);
            }
          } catch (DatastoreException | IOException | PackagingException | ConflictException | NotFoundException | BadArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          } finally {
            progressIndicator.indicateStatus(0);
            resetTable();
            actionButton.setEnabled(true);
            selectAllButton.setEnabled(true);
            deselectAllButton.setEnabled(true);
            searchData();
          }

        }).start();

        chooseDestinationDialog.dispose();
      }
    });
    
    chooseDestinationDialog.pack();
    chooseDestinationDialog.setVisible(true);
  }

  @Override
  protected JComponent createToolBar() {
    JToolBar searchToolBar = (JToolBar) super.createToolBar();
    JToolBar packageToolbar = createPackageToolbar();
    JPanel panel = new JPanel(new GridBagLayout());
    panel.add(searchToolBar, configureFormLayout(0, 0));
    panel.add(packageToolbar, configureFormLayout(0, 1));
    JToggleButton seeHidden = (JToggleButton) searchToolBar.getComponentAtIndex(1);
    seeHidden.setText("See Packaged");
    return panel;
  }

  private JToolBar createPackageToolbar() {
    JToolBar toolBar = new JToolBar();
    ButtonGroup group = new ButtonGroup();
//    JRadioButton viewModeButton = createModeSelectCheckbox("View", this::setViewMode);
//    viewModeButton.setSelected(true);
//    setViewMode(true);
    JRadioButton packageModeButton = createModeSelectCheckbox("Package", this::setPackageMode);
    packageModeButton.setSelected(true);
    setPackageMode(true);
    JRadioButton editVisibilityModeButton = createModeSelectCheckbox("Edit Visibility", this::setEditVisibilityModel);
    setEditVisibilityModel(false);
    JRadioButton deleteModeButton = createModeSelectCheckbox("Delete", this::setDeleteModel);
//    group.add(viewModeButton);
    group.add(packageModeButton);
    group.add(editVisibilityModeButton);
    group.add(deleteModeButton);
//    toolBar.add(viewModeButton);
    toolBar.add(packageModeButton);
    toolBar.add(editVisibilityModeButton);
    toolBar.add(deleteModeButton);
    return toolBar;
  }
  
  private void setViewMode(Boolean enabled) {
    actionButton.setVisible(!enabled);
    selectAllButton.setVisible(!enabled);
    deselectAllButton.setVisible(!enabled);
    
    resetTable();
  }
  
  private void setPackageMode(Boolean enabled) {
    if (enabled) {
      Arrays.stream(actionButton.getActionListeners()).forEach(
          actionButton::removeActionListener
      );
      
      actionButton.addActionListener(e -> packageSelectedRows());
      actionButton.setText("Package");
      
      getTableColumnModel().addColumn(
          getHiddenColumnByHeaderValue("Select for Packaging")
      );

      Arrays.stream(selectAllButton.getActionListeners()).forEach(
          selectAllButton::removeActionListener
      );
      Arrays.stream(deselectAllButton.getActionListeners()).forEach(
          deselectAllButton::removeActionListener
      );
      
      selectAllButton.addActionListener(e -> selectSelectedPackages());
      deselectAllButton.addActionListener(e -> deselectSelectedPackages());
    } else {
      TableColumn tableColumn = getHiddenColumnByHeaderValue("Select for Packaging");
      if (tableColumn != null) {
        getTableColumnModel().removeColumn(tableColumn);
      }
    }
    
    resetTable();
  }

  private void setEditVisibilityModel(Boolean enabled) {
    if (enabled) {
      Arrays.stream(actionButton.getActionListeners()).forEach(
          actionButton::removeActionListener
      );
      
      actionButton.addActionListener(e -> saveRowVisibility());
      actionButton.setText("Save");
      
      getTableColumnModel().addColumn(
          getHiddenColumnByHeaderValue("Visible")
      );

      Arrays.stream(selectAllButton.getActionListeners()).forEach(
          selectAllButton::removeActionListener
      );
      Arrays.stream(deselectAllButton.getActionListeners()).forEach(
          deselectAllButton::removeActionListener
      );
      
      selectAllButton.addActionListener(e -> selectPackageVisibilities());
      deselectAllButton.addActionListener(e -> deselectPackageVisibilities());
    } else {
      TableColumn tableColumn = getHiddenColumnByHeaderValue("Visible");
      if (tableColumn != null) {
        getTableColumnModel().removeColumn(tableColumn);
      }
    }
    
    resetTable();
  }

  private void setDeleteModel(Boolean enabled) {
    if (enabled) {
      Arrays.stream(actionButton.getActionListeners()).forEach(
          actionButton::removeActionListener
      );

      actionButton.addActionListener(e -> deleteSelectedRows());
      actionButton.setText("Delete");

      getTableColumnModel().addColumn(
          getHiddenColumnByHeaderValue("Select for Deletion")
      );

      Arrays.stream(selectAllButton.getActionListeners()).forEach(
          selectAllButton::removeActionListener
      );
      Arrays.stream(deselectAllButton.getActionListeners()).forEach(
          deselectAllButton::removeActionListener
      );

      selectAllButton.addActionListener(e -> selectPackageDeleteStatuses());
      deselectAllButton.addActionListener(e -> deselectPackageDeleteStatuses());
    } else {
      TableColumn tableColumn = getHiddenColumnByHeaderValue("Select for Deletion");
      if (tableColumn != null) {
        getTableColumnModel().removeColumn(tableColumn);
      }
    }

    resetTable();
  }

  private JRadioButton createModeSelectCheckbox(String text, Consumer<Boolean> itemListener) {
    JRadioButton checkBox = new JRadioButton(text);
    checkBox.addItemListener(e -> itemListener.accept(checkBox.isSelected()));
    return checkBox;
  }

  private static class PackageTableModel extends DefaultTableModel {

    public PackageTableModel(Object[][] data, Object[] columnNames) {
      super(data, columnNames);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
      return switch (columnIndex) {
        case 0 -> UUID.class;
        case PACKAGING_SELECTED_COLUMN, VISIBLE_COLUMN, DELETE_COLUMN -> Boolean.class;
        case PACKAGE_COLUMN -> Package.class;
        default -> String.class;
      };
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return column == PACKAGING_SELECTED_COLUMN || column == VISIBLE_COLUMN || column == DELETE_COLUMN;
    }
  }
}
