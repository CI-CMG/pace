package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureFormLayout;
import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MultiPointStationaryMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.StationaryMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.StationaryTerrestrialLocation;
import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.packaging.FileUtils;
import edu.colorado.cires.pace.packaging.PackageProcessor;
import edu.colorado.cires.pace.packaging.PackagingException;
import edu.colorado.cires.pace.packaging.PassivePackerFactory;
import edu.colorado.cires.pace.packaging.ProcessSet;
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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import org.apache.commons.lang3.StringUtils;

/**
 * PackagesPanel extends TranslatePanel and provides structuring related
 * to packaging
 */
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

  /**
   * Creates a packages panel
   * @param repository holds packages that have been translated
   * @param headers labels in packages panel
   * @param objectConversion Creates an object list out of a package fields
   * @param clazz indicates package type
   * @param objectMapper serializes object
   * @param translatorRepository holds existing translators
   * @param converter functions to create packages from map
   * @param personRepository holds existing person objects
   * @param organizationRepository holds existing organization objects
   * @param projectRepository holds existing project objects
   * @param sensorRepository holds existing sensor objects
   * @param detectionTypeRepository holds existing detection type objects
   */
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
    
    JPanel submitDestinationPanel = new JPanel(new BorderLayout());
    JButton submitDestinationButton = new JButton("Verify Directory");
    submitDestinationPanel.add(submitDestinationButton, BorderLayout.EAST);

    JButton metadataButton = new JButton("Metadata Only");
    submitDestinationPanel.add(metadataButton, BorderLayout.WEST);

    chooseDestinationPanel.add(submitDestinationPanel, configureLayout((c) -> { c.gridx = 0; c.gridy = 3; c.gridwidth = GridBagConstraints.REMAINDER; }));
    
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
    chooseDestinationDialog.setTitle("Choose Destination for Packaged Data");
    chooseDestinationDialog.setModal(true);
    chooseDestinationDialog.setLocationRelativeTo(this);
    chooseDestinationDialog.add(chooseDestinationPanel);

    PassivePackerFactory passivePackerFactory = new PassivePackerFactory(
        personRepository, organizationRepository, sensorRepository, detectionTypeRepository
    );

    JDialog verifyMap = new JDialog();
    verifyMap.setLayout(new BorderLayout(5, 5));
    Dimension sizeMap = UIUtils.getPercentageOfWindowDimension(0.5, 0.4);
    verifyMap.setSize(1350,700);
    verifyMap.setPreferredSize(sizeMap);
    verifyMap.setTitle("Verify Package Information");
    verifyMap.setModal(true);
    verifyMap.setLocationRelativeTo(this);
    JPanel infoVerifyPanel = new JPanel(new BorderLayout());
    JButton verifyButton = new JButton("Verify");
    JButton cancelButton = new JButton("Cancel");

    cancelButton.addActionListener((e) -> {
      verifyMap.dispose();
      chooseDestinationDialog.dispose();
    });

    submitDestinationButton.addActionListener((e) -> {
      String destinationText = destinationField.getText();
      if (StringUtils.isBlank(destinationText)) {
        JOptionPane.showMessageDialog(this, "Choose a destination directory", "Error", JOptionPane.ERROR_MESSAGE);
      } else {
        List<Color> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.ORANGE);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        colors.add(Color.BLACK);
        colors.add(Color.GRAY);

        this.mapLocationVerification(packages, verifyMap, colors);
          try {
              this.dateVerification(packages, verifyMap, colors);
          } catch (IOException ex) {
              throw new RuntimeException(ex);
          }
          infoVerifyPanel.add(cancelButton, BorderLayout.WEST);
        infoVerifyPanel.add(verifyButton, BorderLayout.EAST);
        verifyMap.add(infoVerifyPanel, BorderLayout.SOUTH);
        verifyMap.setLocationRelativeTo(chooseDestinationDialog);
        chooseDestinationDialog.setVisible(false);
        verifyMap.setVisible(true);
      }
    });

    verifyButton.addActionListener((e) -> {
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

            ProcessSet pSet = packageProcessor.process();
            List<Package> processedPackages = pSet.processedPackages.stream()
                .filter(p -> Objects.nonNull(p.getUuid()))
                .toList();
            for (Package processedPackage : processedPackages) {
              repository.update(processedPackage.getUuid(), processedPackage);
            }
            for (int i = 0; i < pSet.zeroBytePackages.size(); i++) {
              Package unprocessedPackage = pSet.zeroBytePackages.get(i);
              String message = "Error processing "+ unprocessedPackage.getDataCollectionName() + " due to zero byte "
                  + pSet.zeroByteLists.get(i);
              JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
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

        verifyMap.dispose();
      }
    });

    metadataButton.addActionListener((e) -> {
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
            for(Package p : packages) {
              FileUtils.writeMetadata(passivePackerFactory.createPackage(p), Paths.get(destinationField.getText()));
            }
          } catch (DatastoreException | IOException | NotFoundException ex) {
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

  /**
   * Pops up a map with the locations indicated in packages for verification before packaging
   * up packages.
   */
  protected void mapLocationVerification(List<Package> packages, JDialog verifyMap, List<Color> colors) {
    BufferedImage myPicture;

    int mapWidth = 2700;
    int mapHeight = 1350;
    int xMin = mapWidth;
    int yMin = mapHeight;
    int xMax = 0;
    int yMax = 0;

    List<List<Integer>> spots = new ArrayList<>();

    for (Package p : packages) {
      LocationDetail loc = p.getLocationDetail();
      double lon = 0;
      double lat = 0;
      if (loc instanceof StationaryMarineLocation stationaryMarineLocation) {
        lon = stationaryMarineLocation.getDeploymentLocation().getLongitude();
        lat = stationaryMarineLocation.getDeploymentLocation().getLatitude();
      }
      if (loc instanceof StationaryTerrestrialLocation stationaryT) {
        lon = stationaryT.getLongitude();
        lat = stationaryT.getLatitude();
      }
      if (loc instanceof MultiPointStationaryMarineLocation multiPoint) {
        if (!multiPoint.getLocations().isEmpty()) {
          @NotNull @NotEmpty List<@Valid MarineInstrumentLocation> location = multiPoint.getLocations();
          lon = location.get(0).getLongitude();
          lat = location.get(0).getLatitude();
        }
      }
      if (loc instanceof MobileMarineLocation) {
        spots.add(new ArrayList<>(Arrays.asList(null, null)));
      }

      int x = (int) ((lon + 180) * ((double) mapWidth / 360));
      int y = (int) (((lat * -1) + 90) * ((double) mapHeight / 180));

      spots.add(new ArrayList<>(Arrays.asList(x, y)));

      if (x < xMin) {
        xMin = x;
      }
      if (y < yMin) {
        yMin = y;
      }
      if (x > xMax) {
        xMax = x;
      }
      if (y > yMax) {
        yMax = y;
      }
    }

    int margin = 75;
    try {
      String path = "map.png";
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      int width = (int) screenSize.getWidth();
      int height = (int) screenSize.getHeight();
      if (width < (xMax-xMin) + 100 || height < (yMax-yMin) + 250) {
        path = "small_map.png";
        mapWidth = 900;
        mapHeight = 450;
        margin = 100;
        xMax /= 2;
        yMax /= 2;
        xMin /= 2;
        yMin /= 2;
        List<List<Integer>> adjusted = new ArrayList<>();
        for(List<Integer> spot : spots) {
          if (spot.get(0) == null){
            adjusted.add(new ArrayList<>(Arrays.asList(null, null)));
          }
          else {
            adjusted.add(new ArrayList<>(Arrays.asList(spot.get(0) / 3, spot.get(1) / 3)));
          }
        }
        spots = adjusted;
      }

      myPicture = ImageIO.read(
          Objects.requireNonNull(
              this.getClass().getResourceAsStream(String.format("/%s", path))
          )
      );
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

    Graphics2D g2d = myPicture.createGraphics();
    int i = 1;
    int colorIndex = 0;
    for (List<Integer> spot : spots) {
      int radius = 5;
      Color color = colors.get(colorIndex % colors.size());
      g2d.setColor(color);
      if (spot.get(0) == null) {
        colorIndex++;
        i++;
        continue;
      }
      g2d.fillOval(spot.get(0) - radius, spot.get(1) - radius, 2 * radius, 2 * radius);
      g2d.setFont(new Font("Times New Roman", Font.BOLD, 20));
      g2d.drawString(String.valueOf(i), spot.get(0) - radius, spot.get(1) - radius);
      colorIndex++;
      i++;
    }

    try {
      ImageIO.write(myPicture, "png", new File("map_dot.png"));
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

    g2d.dispose();

    if (xMax - xMin < 2*margin) { xMax += 2*margin; xMin -= 2*margin; }
    if (yMax - yMin < 2*margin) { yMax += 2*margin; yMin -= 2*margin; }

    xMin -= margin;
    yMin -= margin;
    xMax += margin;
    yMax += margin;
    if (xMin < 0) { xMin = 0; }
    if (yMin < 0) { yMin = 0; }
    if (xMax > mapWidth) { xMax = mapWidth; }
    if (yMax > mapHeight) { yMax = mapHeight; }

    myPicture = myPicture.getSubimage(xMin, yMin, xMax-xMin, yMax-yMin);

    JLabel picLabel = new JLabel(new ImageIcon(myPicture));
    verifyMap.setSize(xMax-xMin+30, yMax-yMin+480);
    if (xMax-xMin+30 < 800) {
      verifyMap.setSize(800, yMax-yMin+480);
    }
    verifyMap.add(picLabel, BorderLayout.NORTH);
  }

  protected void dateVerification(List<Package> packages, JDialog verifyMap, List<Color> colors) throws IOException {
    JLabel verificationLabel = new JLabel();
    List<Object[]> dataList = new ArrayList<>();
    String[] columnNames = {"#", "Package", "Public Release Date", "Audio Start Time", "Audio End Time", "Start Longitude", "Start Latitude", "File Count"};
    // Possibly an issue if we're looking to get all file types since we're doing 'instanceof AudioPackage'
    if (packages.get(0) instanceof AudioPackage) {
      verificationLabel = new JLabel("Please confirm that the information below is correct before clicking Verify");
      verificationLabel.setFont(verificationLabel.getFont().deriveFont(Font.BOLD, 18));
      verificationLabel.setHorizontalAlignment(SwingConstants.CENTER);
      verificationLabel.setVerticalAlignment(SwingConstants.CENTER);
    }

    int i = 1;
    for (Package p : packages) {
      Path path = p.getSourcePath();
      long fileCount = 0;
      try {
        fileCount = Files.walk(path)
                         .filter(Files::isRegularFile)
                         .count();
      } catch (IOException ex) {
        ex.printStackTrace();
      }

        if (p instanceof AudioPackage a) {

        LocationDetail loc = p.getLocationDetail();
        double lon = 0;
        double lat = 0;
        if (loc instanceof StationaryMarineLocation stationaryMarineLocation) {
          lon = stationaryMarineLocation.getDeploymentLocation().getLongitude();
          lat = stationaryMarineLocation.getDeploymentLocation().getLatitude();
        }
        if (loc instanceof StationaryTerrestrialLocation stationaryT) {
          lon = stationaryT.getLongitude();
          lat = stationaryT.getLatitude();
        }
        if (loc instanceof MultiPointStationaryMarineLocation multiPoint) {
          if (!multiPoint.getLocations().isEmpty()) {
            @NotNull @NotEmpty List<@Valid MarineInstrumentLocation> location = multiPoint.getLocations();
            lon = location.get(0).getLongitude();
            lat = location.get(0).getLatitude();
          }
        }


      Object[] newRow = {i, a.getPackageId(), a.getPublicReleaseDate(), a.getAudioStartTime(), a.getAudioEndTime(), lon, lat, fileCount -1};
      dataList.add(newRow);
      i++;
    }
  }

      Object[][] data = dataList.toArray(new Object[0][]);
      JTable table = new JTable(data, columnNames) {
        @Override
        public Class<?> getColumnClass(int column) {
            if(convertColumnIndexToModel(column)==0) return Double.class;
            return super.getColumnClass(column);
        }
      };

    JTableHeader header = table.getTableHeader();
    header.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
    DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
    renderer.setHorizontalAlignment(SwingConstants.LEFT);
    table.setPreferredScrollableViewportSize(new Dimension(300, table.getPreferredSize().height));

    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer(){
      @Override
      public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Color color = colors.get(row % colors.size());
        c.setForeground(color);
        return c;
      }
    };

    cellRenderer.setHorizontalAlignment(JLabel.LEFT);
    table.getColumnModel().getColumn(0).setPreferredWidth(1);
    table.getColumnModel().getColumn(1).setPreferredWidth(30);
    table.getColumnModel().getColumn(2).setPreferredWidth(99);
    table.getColumnModel().getColumn(3).setPreferredWidth(99);
    table.getColumnModel().getColumn(4).setPreferredWidth(99);
    table.getColumnModel().getColumn(5).setPreferredWidth(1);
    table.getColumnModel().getColumn(6).setPreferredWidth(1);
//    table.getColumnModel().getColumn(7).setPreferredWidth(99);
    table.setDefaultRenderer(Double.class, cellRenderer);

    JScrollPane scrollPane = new JScrollPane(table);
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
    verificationLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    panel.add(verificationLabel, BorderLayout.NORTH);
    panel.add(scrollPane, BorderLayout.CENTER);

    verifyMap.add(panel, BorderLayout.CENTER);
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
    //JRadioButton viewModeButton = createModeSelectCheckbox("View", this::setViewMode);
    //viewModeButton.setSelected(true);
    //setViewMode(true);
    JRadioButton packageModeButton = createModeSelectCheckbox("Create Package", this::setPackageMode);
    packageModeButton.setSelected(true);
    JRadioButton editVisibilityModeButton = createModeSelectCheckbox("Edit Visibility", this::setEditVisibilityModel);
    setEditVisibilityModel(false);
    JRadioButton deleteModeButton = createModeSelectCheckbox("Delete Translation", this::setDeleteModel);
    //group.add(viewModeButton);
    group.add(packageModeButton);
    group.add(editVisibilityModeButton);
    group.add(deleteModeButton);
    //toolBar.add(viewModeButton);
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
    actionButton.setVisible(true);
    selectAllButton.setVisible(true);
    deselectAllButton.setVisible(true);
    if (enabled) {
      Arrays.stream(actionButton.getActionListeners()).forEach(
          actionButton::removeActionListener
      );
      
      actionButton.addActionListener(e -> packageSelectedRows());
      actionButton.setText("Package Data");

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
