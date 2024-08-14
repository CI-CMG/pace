package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.dataset.audio.translator.AudioDataPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.translator.AudioPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.translator.CPODPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.ChannelTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntryTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateTimeSeparatedTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DefaultTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.detections.translator.DetectionsPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.DutyCycleTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.GainTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.LocationDetailTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.MarineInstrumentLocationTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.MobileMarineLocationTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.MultipointStationaryMarineLocationTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.PackageSensorTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.QualityControlDetailTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.SampleRateTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.SoftwareDependentPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.SoundAnalysisPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.soundClips.translator.SoundClipsPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.translator.SoundLevelMetricsPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.translator.SoundPropagationModelsPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.StationaryMarineLocationTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator.StationaryTerrestrialLocationTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class PackageTranslatorForm extends BaseTranslatorForm<PackageTranslator> {
  
  private final ContactsTranslatorForm contactsTranslatorForm;
  private final FilePathsTranslatorForm filePathsTranslatorForm;
  private final CalibrationTranslatorForm calibrationTranslatorForm;
  private final PackageInfoForm packageInfoForm;
  private final LocationDetailForm locationDetailForm;
  
  private ScrollPane qualityControlForm;
  private ScrollPane channelsForm;
  private ScrollPane packageDetailForm;
  private ScrollPane softwareForm;
  private ScrollPane soundAnalysisForm;
  
  private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
  
  private final JComboBox<String> packageTypeComboBox;
  
  private final PackageTranslator initialTranslator;

  protected PackageTranslatorForm(PackageTranslator initialTranslator, String[] headerOptions) {
    super(headerOptions);
    
    tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    
    if (initialTranslator != null) {
      headerOptions = getInitialHeaderValues(initialTranslator);
    }
    this.initialTranslator = initialTranslator;
    this.packageTypeComboBox = initialTranslator == null ? getPackageTypeComboBox() : null;
    this.contactsTranslatorForm = new ContactsTranslatorForm(headerOptions, initialTranslator);
    this.filePathsTranslatorForm = new FilePathsTranslatorForm(headerOptions, initialTranslator);
    this.calibrationTranslatorForm = new CalibrationTranslatorForm(headerOptions, initialTranslator);
    this.packageInfoForm = new PackageInfoForm(headerOptions, initialTranslator);
    this.locationDetailForm = new LocationDetailForm(headerOptions, initialTranslator == null ? null : initialTranslator.getLocationDetailTranslator());
    addUniqueFields();
    initializeUniqueFields(initialTranslator);
    setHeaderOptions(headerOptions);
  }

  @Override
  protected void addUniqueFields() {
    setLayout(new BorderLayout());
    
    if (initialTranslator == null) {
      tabbedPane.add("Package Type", getComboBoxPanel(packageTypeComboBox));
    }
    tabbedPane.add("Package", new JScrollPane(packageInfoForm));
    tabbedPane.add("File Paths", new JScrollPane(filePathsTranslatorForm));
    tabbedPane.add("Contacts", new JScrollPane(contactsTranslatorForm));
    tabbedPane.add("Calibration", new JScrollPane(calibrationTranslatorForm));
    tabbedPane.add("Location", new JScrollPane(locationDetailForm));

    add(tabbedPane, BorderLayout.CENTER);
  }

  @Override
  protected void initializeUniqueFields(PackageTranslator initialTranslator) {
    if (initialTranslator != null) {
      setHeaderOptions(getInitialHeaderValues(initialTranslator));
      if (initialTranslator instanceof AudioDataPackageTranslator audioDataPackageTranslator) {
        packageDetailForm = new ScrollPane(
            new AudioDataForm<>(getHeaderOptions(), audioDataPackageTranslator)
        );
        
        channelsForm = new ScrollPane(
            new ChannelsForm(getHeaderOptions(), audioDataPackageTranslator.getChannelTranslators())
        );
        
        qualityControlForm = new ScrollPane(
            new QualityControlForm(getHeaderOptions(), audioDataPackageTranslator.getQualityControlDetailTranslator())
        );
        
        tabbedPane.add("Quality", qualityControlForm);
        tabbedPane.add("Channels", channelsForm);
        tabbedPane.add("Package Detail", packageDetailForm);
      } else if (initialTranslator instanceof DetectionsPackageTranslator detectionsPackageTranslator) {
        soundAnalysisForm = new ScrollPane(
            new SoundAnalysisForm<>(getHeaderOptions(), detectionsPackageTranslator)
        );
        qualityControlForm = new ScrollPane(
            new QualityControlForm(getHeaderOptions(), detectionsPackageTranslator.getQualityControlDetailTranslator())
        );
        softwareForm = new ScrollPane(
            new SoftwareForm<>(getHeaderOptions(), detectionsPackageTranslator)
        );
        packageDetailForm = new ScrollPane(
            new DetectionsForm(getHeaderOptions(), detectionsPackageTranslator)
        );

        tabbedPane.add("Quality", qualityControlForm);
        tabbedPane.add("Software", softwareForm);
        tabbedPane.add("Sound Analysis", soundAnalysisForm);
        tabbedPane.add("Package Detail", packageDetailForm);
      } else if (initialTranslator instanceof SoundLevelMetricsPackageTranslator soundLevelMetricsPackageTranslator) {
        soundAnalysisForm = new ScrollPane(
            new SoundAnalysisForm<>(getHeaderOptions(), soundLevelMetricsPackageTranslator)
        );
        qualityControlForm = new ScrollPane(
            new QualityControlForm(getHeaderOptions(), soundLevelMetricsPackageTranslator.getQualityControlDetailTranslator())
        );
        softwareForm = new ScrollPane(
            new SoftwareForm<>(getHeaderOptions(), soundLevelMetricsPackageTranslator)
        );
        packageDetailForm = new ScrollPane(
            new SoundLevelMetricsForm(getHeaderOptions(), soundLevelMetricsPackageTranslator)
        );
        tabbedPane.add("Quality", qualityControlForm);
        tabbedPane.add("Software", softwareForm);
        tabbedPane.add("Sound Analysis", soundAnalysisForm);
        tabbedPane.add("Package Detail", packageDetailForm);
      } else if (initialTranslator instanceof SoundPropagationModelsPackageTranslator soundPropagationModelsPackageTranslator) {
        softwareForm = new ScrollPane(
            new SoftwareForm<>(getHeaderOptions(), soundPropagationModelsPackageTranslator)
        );
        packageDetailForm = new ScrollPane(
            new SoundPropagationModelsForm(getHeaderOptions(), soundPropagationModelsPackageTranslator)
        );
        tabbedPane.add("Software", softwareForm);
        tabbedPane.add("Package Detail", packageDetailForm);
      } else if (initialTranslator instanceof SoundClipsPackageTranslator soundClipsPackageTranslator) {
        softwareForm = new ScrollPane(
            new SoftwareForm<>(getHeaderOptions(), soundClipsPackageTranslator)
        );
        packageDetailForm = new ScrollPane(
            new SoundClipsForm(headerOptions, soundClipsPackageTranslator)
        );
        tabbedPane.add("Software", softwareForm);
        tabbedPane.add("Package Detail", packageDetailForm);
      }
    }
  }

  private String[] getInitialHeaderValues(PackageTranslator initialTranslator) {
    List<String> headerOptions = new ArrayList<>();
    if (initialTranslator == null) {
      return headerOptions.toArray(String[]::new);
    }
    
    headerOptions.add(initialTranslator.getPackageUUID());
    headerOptions.add(initialTranslator.getTemperaturePath());
    headerOptions.add(initialTranslator.getBiologicalPath());
    headerOptions.add(initialTranslator.getOtherPath());
    headerOptions.add(initialTranslator.getDocumentsPath());
    headerOptions.add(initialTranslator.getCalibrationDocumentsPath());
    headerOptions.add(initialTranslator.getSourcePath());
    headerOptions.add(initialTranslator.getDataCollectionName());
    headerOptions.add(initialTranslator.getProcessingLevel());
    headerOptions.add(initialTranslator.getSiteOrCruiseName());
    headerOptions.add(initialTranslator.getDeploymentId());
    headerOptions.add(initialTranslator.getDatasetPackager());
    headerOptions.add(initialTranslator.getProjects());
    headerOptions.add(initialTranslator.getScientists());
    headerOptions.add(initialTranslator.getSponsors());
    headerOptions.add(initialTranslator.getFunders());
    headerOptions.add(initialTranslator.getPlatform());
    headerOptions.add(initialTranslator.getInstrument());
    headerOptions.add(initialTranslator.getCalibrationDescription());
    headerOptions.add(initialTranslator.getDeploymentTitle());
    headerOptions.add(initialTranslator.getDeploymentPurpose());
    headerOptions.add(initialTranslator.getDeploymentDescription());
    headerOptions.add(initialTranslator.getAlternateSiteName());
    headerOptions.add(initialTranslator.getAlternateDeploymentName());

    addDateTranslatorFields(initialTranslator.getPublicReleaseDate(), headerOptions);
    addDateTranslatorFields(initialTranslator.getPreDeploymentCalibrationDate(), headerOptions);
    addDateTranslatorFields(initialTranslator.getPostDeploymentCalibrationDate(), headerOptions);
    
    addTimeTranslatorFields(initialTranslator.getStartTime(), headerOptions);
    addTimeTranslatorFields(initialTranslator.getEndTime(), headerOptions);
    
    addLocationDetailTranslatorFields(initialTranslator.getLocationDetailTranslator(), headerOptions);
    
    addPackageTypeSpecificFields(initialTranslator, headerOptions);
    
    return headerOptions.stream()
        .filter(Objects::nonNull)
        .toArray(String[]::new);
  }

  private void addPackageTypeSpecificFields(PackageTranslator initialTranslator, List<String> headerOptions) {
    if (initialTranslator instanceof AudioDataPackageTranslator audioDataPackageTranslator) {
      addAudioDataPackageFields(audioDataPackageTranslator, headerOptions);
    }
    
    if (initialTranslator instanceof SoundAnalysisPackageTranslator soundAnalysisPackageTranslator) {
      addSoundAnalysisPackageFields(soundAnalysisPackageTranslator, headerOptions);
    }
    
    if (initialTranslator instanceof SoftwareDependentPackageTranslator softwareDependentPackageTranslator) {
      addSoftwareDependentPackageFields(softwareDependentPackageTranslator, headerOptions);
    }
    
    if (initialTranslator instanceof SoundClipsPackageTranslator soundClipsPackageTranslator) {
      addTimeTranslatorFields(soundClipsPackageTranslator.getAudioStartTime(), headerOptions);
      addTimeTranslatorFields(soundClipsPackageTranslator.getAudioEndTime(), headerOptions);
    }
    
    if (initialTranslator instanceof SoundLevelMetricsPackageTranslator soundLevelMetricsPackageTranslator) {
      addQualityDetailFields(soundLevelMetricsPackageTranslator.getQualityControlDetailTranslator(), headerOptions);
      addTimeTranslatorFields(soundLevelMetricsPackageTranslator.getAudioStartTimeTranslator(), headerOptions);
      addTimeTranslatorFields(soundLevelMetricsPackageTranslator.getAudioEndTimeTranslator(), headerOptions);
    }
    
    if (initialTranslator instanceof SoundPropagationModelsPackageTranslator soundPropagationModelsPackageTranslator) {
      headerOptions.add(soundPropagationModelsPackageTranslator.getModeledFrequency());
      addTimeTranslatorFields(soundPropagationModelsPackageTranslator.getAudioStartTimeTranslator(), headerOptions);
      addTimeTranslatorFields(soundPropagationModelsPackageTranslator.getAudioEndTimeTranslator(), headerOptions);
    }
    
    if (initialTranslator instanceof DetectionsPackageTranslator detectionsPackageTranslator) {
      addQualityDetailFields(detectionsPackageTranslator.getQualityControlDetailTranslator(), headerOptions);
      headerOptions.add(detectionsPackageTranslator.getSoundSource());
    }
  }

  private void addSoftwareDependentPackageFields(SoftwareDependentPackageTranslator softwareDependentPackageTranslator, List<String> headerOptions) {
    headerOptions.add(softwareDependentPackageTranslator.getSoftwareNames());
    headerOptions.add(softwareDependentPackageTranslator.getSoftwareVersions());
    headerOptions.add(softwareDependentPackageTranslator.getSoftwareProtocolCitation());
    headerOptions.add(softwareDependentPackageTranslator.getSoftwareDescription());
    headerOptions.add(softwareDependentPackageTranslator.getSoftwareProcessingDescription());
  }

  private void addSoundAnalysisPackageFields(SoundAnalysisPackageTranslator soundAnalysisPackageTranslator, List<String> headerOptions) {
    headerOptions.add(soundAnalysisPackageTranslator.getAnalysisTimeZone());
    headerOptions.add(soundAnalysisPackageTranslator.getAnalysisEffort());
    headerOptions.add(soundAnalysisPackageTranslator.getSampleRate());
    headerOptions.add(soundAnalysisPackageTranslator.getMinFrequency());
    headerOptions.add(soundAnalysisPackageTranslator.getMaxFrequency());
  }

  private void addAudioDataPackageFields(AudioDataPackageTranslator audioDataPackageTranslator, List<String> headerOptions) {
    headerOptions.add(audioDataPackageTranslator.getInstrumentId());
    headerOptions.add(audioDataPackageTranslator.getHydrophoneSensitivity());
    headerOptions.add(audioDataPackageTranslator.getFrequencyRange());
    headerOptions.add(audioDataPackageTranslator.getGain());
    headerOptions.add(audioDataPackageTranslator.getComments());
    for (PackageSensorTranslator sensor : audioDataPackageTranslator.getSensors()) {
      headerOptions.add(sensor.getName());
      headerOptions.add(sensor.getPosition().getX());
      headerOptions.add(sensor.getPosition().getY());
      headerOptions.add(sensor.getPosition().getZ());
    }
    addTimeTranslatorFields(audioDataPackageTranslator.getDeploymentTime(), headerOptions);
    addTimeTranslatorFields(audioDataPackageTranslator.getRecoveryTime(), headerOptions);
    addTimeTranslatorFields(audioDataPackageTranslator.getAudioStartTime(), headerOptions);
    addTimeTranslatorFields(audioDataPackageTranslator.getAudioEndTime(), headerOptions);
    addQualityDetailFields(audioDataPackageTranslator.getQualityControlDetailTranslator(), headerOptions);
    audioDataPackageTranslator.getChannelTranslators().forEach(
        t -> addChannelFields(t, headerOptions)
    );
  }

  private void addChannelFields(ChannelTranslator channelTranslator, List<String> headerOptions) {
    headerOptions.add(channelTranslator.getSensor().getName());
    headerOptions.add(channelTranslator.getSensor().getPosition().getX());
    headerOptions.add(channelTranslator.getSensor().getPosition().getY());
    headerOptions.add(channelTranslator.getSensor().getPosition().getZ());
    addTimeTranslatorFields(channelTranslator.getStartTime(), headerOptions);
    addTimeTranslatorFields(channelTranslator.getEndTime(), headerOptions);
    channelTranslator.getSampleRates().forEach(
        t -> addSampleRateFields(t, headerOptions)
    );
    channelTranslator.getDutyCycles().forEach(
        t -> addDutyCycleFields(t, headerOptions)
    );
    channelTranslator.getGains().forEach(
        t -> addGainFields(t, headerOptions)
    );
  }

  private void addGainFields(GainTranslator gainTranslator, List<String> headerOptions) {
    addTimeTranslatorFields(gainTranslator.getStartTime(), headerOptions);
    addTimeTranslatorFields(gainTranslator.getEndTime(), headerOptions);
    headerOptions.add(gainTranslator.getGain());
  }

  private void addDutyCycleFields(DutyCycleTranslator dutyCycleTranslator, List<String> headerOptions) {
    addTimeTranslatorFields(dutyCycleTranslator.getStartTime(), headerOptions);
    addTimeTranslatorFields(dutyCycleTranslator.getEndTime(), headerOptions);
    headerOptions.add(dutyCycleTranslator.getDuration());
    headerOptions.add(dutyCycleTranslator.getInterval());
  }

  private void addSampleRateFields(SampleRateTranslator sampleRateTranslator, List<String> headerOptions) {
    addTimeTranslatorFields(sampleRateTranslator.getStartTime(), headerOptions);
    addTimeTranslatorFields(sampleRateTranslator.getEndTime(), headerOptions);
    headerOptions.add(sampleRateTranslator.getSampleRate());
    headerOptions.add(sampleRateTranslator.getSampleBits());
  }

  private void addQualityDetailFields(QualityControlDetailTranslator qualityControlDetailTranslator, List<String> headerOptions) {
    if (qualityControlDetailTranslator == null) {
      return;
    }
    
    headerOptions.add(qualityControlDetailTranslator.getQualityAnalyst());
    headerOptions.add(qualityControlDetailTranslator.getQualityAnalysisObjectives());
    headerOptions.add(qualityControlDetailTranslator.getQualityAnalysisMethod());
    headerOptions.add(qualityControlDetailTranslator.getQualityAssessmentDescription());
    
    qualityControlDetailTranslator.getQualityEntryTranslators().forEach(
        t -> addQualityEntryFields(t, headerOptions)
    );
  }

  private void addQualityEntryFields(DataQualityEntryTranslator dataQualityEntryTranslator, List<String> headerOptions) {
    addTimeTranslatorFields(dataQualityEntryTranslator.getStartTime(), headerOptions);
    addTimeTranslatorFields(dataQualityEntryTranslator.getEndTime(), headerOptions);
    headerOptions.add(dataQualityEntryTranslator.getMinFrequency());
    headerOptions.add(dataQualityEntryTranslator.getMaxFrequency());
    headerOptions.add(dataQualityEntryTranslator.getQualityLevel());
    headerOptions.add(dataQualityEntryTranslator.getComments());
    headerOptions.add(dataQualityEntryTranslator.getChannelNumbers());
  }

  private void addLocationDetailTranslatorFields(LocationDetailTranslator locationDetailTranslator, List<String> headerOptions) {
    if (locationDetailTranslator instanceof StationaryMarineLocationTranslator stationaryMarineLocationTranslator) {
      headerOptions.add(
          stationaryMarineLocationTranslator.getSeaArea()
      );
      addMarineInstrumentLocationTranslator(stationaryMarineLocationTranslator.getDeploymentLocationTranslator(), headerOptions);
      addMarineInstrumentLocationTranslator(stationaryMarineLocationTranslator.getRecoveryLocationTranslator(), headerOptions);
    } else if (locationDetailTranslator instanceof MultipointStationaryMarineLocationTranslator multipointStationaryMarineLocationTranslator) {
      headerOptions.add(multipointStationaryMarineLocationTranslator.getSeaArea());
      multipointStationaryMarineLocationTranslator.getLocationTranslators().forEach(
          t -> addMarineInstrumentLocationTranslator(t, headerOptions)
      );
    } else if (locationDetailTranslator instanceof MobileMarineLocationTranslator mobileMarineLocationTranslator) {
      headerOptions.add(mobileMarineLocationTranslator.getSeaArea());
      headerOptions.add(mobileMarineLocationTranslator.getVessel());
      headerOptions.add(mobileMarineLocationTranslator.getLocationDerivationDescription());
    } else if (locationDetailTranslator instanceof StationaryTerrestrialLocationTranslator stationaryTerrestrialLocationTranslator) {
      headerOptions.add(stationaryTerrestrialLocationTranslator.getLatitude());
      headerOptions.add(stationaryTerrestrialLocationTranslator.getLongitude());
      headerOptions.add(stationaryTerrestrialLocationTranslator.getSurfaceElevation());
      headerOptions.add(stationaryTerrestrialLocationTranslator.getInstrumentElevation());
    }
  }
  
  private void addMarineInstrumentLocationTranslator(MarineInstrumentLocationTranslator marineInstrumentLocationTranslator, List<String> headerOptions) {
    headerOptions.add(marineInstrumentLocationTranslator.getLatitude());
    headerOptions.add(marineInstrumentLocationTranslator.getLongitude());
    headerOptions.add(marineInstrumentLocationTranslator.getSeaFloorDepth());
    headerOptions.add(marineInstrumentLocationTranslator.getInstrumentDepth());
  }

  private void addDateTranslatorFields(DateTranslator dateTranslator, List<String> headerOptions) {
    headerOptions.add(dateTranslator.getTimeZone());
    headerOptions.add(dateTranslator.getDate());
  }
  
  private void addTimeTranslatorFields(TimeTranslator timeTranslator, List<String> headerOptions) {
    if (timeTranslator == null) {
      return;
    }
    headerOptions.add(
        timeTranslator.getTimeZone()
    );
    if (timeTranslator instanceof DefaultTimeTranslator defaultTimeTranslator) {
      headerOptions.add(
          defaultTimeTranslator.getTime()
      );
    } else if (timeTranslator instanceof DateTimeSeparatedTimeTranslator dateTimeSeparatedTimeTranslator) {
      headerOptions.add(dateTimeSeparatedTimeTranslator.getTime());
      headerOptions.add(dateTimeSeparatedTimeTranslator.getDate());
    }
  }

  private JPanel getComboBoxPanel(JComboBox<String> comboBox) {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.add(new JLabel("Package Type"), configureLayout(c -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    panel.add(comboBox, configureLayout(c -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
    panel.add(new JPanel(), configureLayout(c -> { c.gridx = 0; c.gridy = 2; c.weighty = 1; }));
    return panel;
  }
  
  private JComboBox<String> getPackageTypeComboBox() {
    JComboBox<String> comboBox = new JComboBox<>(new DefaultComboBoxModel<>(new String[] {
       "Audio","CPOD","Detections","Sound Propagation Models","Sound Clips","Sound Level Metrics"
    }));
    comboBox.setName("packageType");
    comboBox.setSelectedItem(null);

    comboBox.addItemListener(e -> {
      String choice = (String) e.getItem();
      if (choice != null) {
        if (choice.equals("Audio") || choice.equals("CPOD") || choice.equals("Detections") || choice.equals("Sound Level Metrics")) {
          if (qualityControlForm == null) {
            qualityControlForm = new ScrollPane(
                new QualityControlForm(headerOptions, null)
            );
            tabbedPane.add("Quality", qualityControlForm);
          }
        } else {
          if (qualityControlForm != null) {
            tabbedPane.remove(qualityControlForm);
            qualityControlForm = null;
          }
        }
        
        if (choice.equals("Audio") || choice.equals("CPOD")) {
          if (channelsForm == null) {
            channelsForm = new ScrollPane(
                new ChannelsForm(headerOptions, null)
            );
            tabbedPane.add("Channels", channelsForm);
          }
        } else {
          if (channelsForm != null) {
            tabbedPane.remove(channelsForm);
            channelsForm = null;
          }
        }
        if (choice.equals("Audio") || choice.equals("CPOD")) {
          if (packageDetailForm == null || !(packageDetailForm.getComponent() instanceof AudioDataForm<?>)) {
            if (packageDetailForm != null) {
              tabbedPane.remove(packageDetailForm);
            }
            packageDetailForm = new ScrollPane(
                new AudioDataForm<>(headerOptions, null)
            );
            tabbedPane.add("Package Detail", packageDetailForm);
          }
        } else if (choice.equals("Sound Propagation Models")) {
          if (packageDetailForm == null || !(packageDetailForm.getComponent() instanceof SoundPropagationModelsForm)) {
            if (packageDetailForm != null) {
              tabbedPane.remove(packageDetailForm);
            }
            packageDetailForm = new ScrollPane(
                new SoundPropagationModelsForm(headerOptions, null)
            );
            tabbedPane.add("Package Detail", packageDetailForm);
          }
        } else if (choice.equals("Detections")) {
          if (packageDetailForm == null || !(packageDetailForm.getComponent() instanceof DetectionsForm)) {
            if (packageDetailForm != null) {
              tabbedPane.remove(packageDetailForm);
            }
            packageDetailForm = new ScrollPane(
                new DetectionsForm(headerOptions, null)
            );
            tabbedPane.add("Package Detail", packageDetailForm);
          }
        } else if (choice.equals("Sound Level Metrics")) {
          if (packageDetailForm == null || !(packageDetailForm.getComponent() instanceof SoundLevelMetricsForm)) {
            if (packageDetailForm != null) {
              tabbedPane.remove(packageDetailForm);
            }
            packageDetailForm = new ScrollPane(
                new SoundLevelMetricsForm(headerOptions, null)
            );
            tabbedPane.add("Package Detail", packageDetailForm);
          }
        } else if (choice.equals("Sound Clips")) {
          if (packageDetailForm == null || !(packageDetailForm.getComponent() instanceof SoundLevelMetricsForm)) {
            if (packageDetailForm != null) {
              tabbedPane.remove(packageDetailForm);
            }
            packageDetailForm = new ScrollPane(
                new SoundClipsForm(headerOptions, null)
            );
            tabbedPane.add("Package Detail", packageDetailForm);
          }
        } else {
          if (packageDetailForm != null) {
            tabbedPane.remove(packageDetailForm);
            packageDetailForm = null;
          }
        }
        if (
            choice.equals("Detections") || 
            choice.equals("Sound Propagation Models") ||
            choice.equals("Sound Clips") || 
            choice.equals("Sound Level Metrics")) {
          if (softwareForm == null) {
            softwareForm = new ScrollPane(
                new SoftwareForm<>(headerOptions, null)
            );
            tabbedPane.add("Software", softwareForm);
          }
        } else {
          if (softwareForm != null) {
            tabbedPane.remove(softwareForm);
            softwareForm = null;
          }
        }
        if (choice.equals("Sound Level Metrics") || choice.equals("Detections")) {
          if (soundAnalysisForm == null) {
            soundAnalysisForm = new ScrollPane(
                new SoundAnalysisForm<>(headerOptions, null)
            );
            tabbedPane.add("Sound Analysis", soundAnalysisForm);
          }
        } else {
          if (soundAnalysisForm != null) {
            tabbedPane.remove(soundAnalysisForm);
            soundAnalysisForm = null;
          }
        }
        revalidate();
      }
    });
    
    return comboBox;
  }

  @Override
  protected PackageTranslator toTranslator(UUID uuid, String name) {
    PackageTranslator packageTranslator = PackageTranslator.builder()
        .uuid(uuid)
        .name(name)
        .temperaturePath(filePathsTranslatorForm.getTemperaturePathValue())
        .biologicalPath(filePathsTranslatorForm.getBiologicalPathValue())
        .otherPath(filePathsTranslatorForm.getOtherPathValue())
        .documentsPath(filePathsTranslatorForm.getDocumentsPathValue())
        .calibrationDocumentsPath(calibrationTranslatorForm.getCalibrationDocumentsPathValue())
        .sourcePath(filePathsTranslatorForm.getSourcePathValue())
        .scientists(contactsTranslatorForm.getScientistsValue())
        .sponsors(contactsTranslatorForm.getSponsorsValue())
        .funders(contactsTranslatorForm.getFundersValue())
        .datasetPackager(contactsTranslatorForm.getDatasetPackagerValue())
        .calibrationDescription(calibrationTranslatorForm.getCalibrationDescriptionValue())
        .preDeploymentCalibrationDate(calibrationTranslatorForm.getPreDeploymentCalibrationDateTranslator())
        .postDeploymentCalibrationDate(calibrationTranslatorForm.getPostDeploymentCalibrationDateTranslator())
        .processingLevel(packageInfoForm.getProcessingLevelValue())
        .packageUUID(packageInfoForm.getUuidValue())
        .dataCollectionName(packageInfoForm.getDataCollectionNameValue())
        .siteOrCruiseName(packageInfoForm.getSiteOrCruiseNameValue())
        .deploymentId(packageInfoForm.getDeploymentIdValue())
        .projects(packageInfoForm.getProjectsValue())
        .platform(packageInfoForm.getPlatformValue())
        .instrument(packageInfoForm.getInstrumentValue())
        .deploymentTitle(packageInfoForm.getDeploymentTitleValue())
        .deploymentPurpose(packageInfoForm.getDeploymentPurposeValue())
        .deploymentDescription(packageInfoForm.getDeploymentDescriptionValue())
        .alternateSiteName(packageInfoForm.getAlternateSiteNameValue())
        .alternateDeploymentName(packageInfoForm.getAlternateDeploymentNameValue())
        .startTime(packageInfoForm.getStartTimeTranslator())
        .endTime(packageInfoForm.getEndTimeTranslator())
        .publicReleaseDate(packageInfoForm.getPublicReleaseDateTranslator())
        .locationDetailTranslator(locationDetailForm.toTranslator())
        .build();
    
    if (initialTranslator == null) {
      String packageTypeChoice = (String) packageTypeComboBox.getSelectedItem();
      if (packageTypeChoice != null) {
        return switch (packageTypeChoice) {
          case"Audio" -> toAudioTranslator(packageTranslator);
          case"CPOD" -> toCPODPackageTranslator(packageTranslator);
          case"Sound Level Metrics" -> toSoundLevelMetricsTranslator(packageTranslator);
          case"Sound Propagation Models" -> toSoundPropagationModelsTranslator(packageTranslator);
          case"Sound Clips" -> toSoundClipsTranslator(packageTranslator);
          case"Detections" -> toDetectionsTranslator(packageTranslator);
          default -> packageTranslator;
        };
      } else {
        return packageTranslator;
      }
    } else {
      if (initialTranslator instanceof AudioPackageTranslator) {
        return toAudioTranslator(packageTranslator);
      } else if (initialTranslator instanceof CPODPackageTranslator) {
        return toCPODPackageTranslator(packageTranslator);
      } else if (initialTranslator instanceof DetectionsPackageTranslator) {
        return toDetectionsTranslator(packageTranslator);
      } else if (initialTranslator instanceof SoundClipsPackageTranslator) {
        return toSoundClipsTranslator(packageTranslator);
      } else if (initialTranslator instanceof SoundLevelMetricsPackageTranslator) {
        return toSoundLevelMetricsTranslator(packageTranslator);
      } else if (initialTranslator instanceof SoundPropagationModelsPackageTranslator) {
        return toSoundPropagationModelsTranslator(packageTranslator);
      } else {
        return packageTranslator;
      }
    }
  }
  
  private AudioPackageTranslator toAudioTranslator(PackageTranslator packageTranslator) {
    AudioDataForm<?> audioDataForm = (AudioDataForm<?>) packageDetailForm.getComponent();
    return AudioPackageTranslator.toBuilder(packageTranslator)
        .qualityControlDetailTranslator(((QualityControlForm) qualityControlForm.getComponent()).toTranslator())
        .instrumentId(audioDataForm.getInstrumentIdValue())
        .hydrophoneSensitivity(audioDataForm.getHydrophoneSensitivityValue())
        .frequencyRange(audioDataForm.getFrequencyRangeValue())
        .gain(audioDataForm.getGainValue())
        .comments(audioDataForm.getCommentsValue())
        .sensors(audioDataForm.getSensorsValue())
        .deploymentTime(audioDataForm.getDeploymentTimeTranslator())
        .recoveryTime(audioDataForm.getRecoveryTimeTranslator())
        .audioStartTime(audioDataForm.getAudioStartTimeTranslator())
        .audioEndTime(audioDataForm.getAudioEndTimeTranslator())
        .channelTranslators(((ChannelsForm) channelsForm.getComponent()).toTranslator())
        .build();
  }
  
  private CPODPackageTranslator toCPODPackageTranslator(PackageTranslator packageTranslator) {
    AudioDataForm<?> audioDataForm = (AudioDataForm<?>) packageDetailForm.getComponent();
    return CPODPackageTranslator.toBuilder(packageTranslator)
        .qualityControlDetailTranslator(((QualityControlForm) qualityControlForm.getComponent()).toTranslator())
        .instrumentId(audioDataForm.getInstrumentIdValue())
        .hydrophoneSensitivity(audioDataForm.getHydrophoneSensitivityValue())
        .frequencyRange(audioDataForm.getFrequencyRangeValue())
        .gain(audioDataForm.getGainValue())
        .comments(audioDataForm.getCommentsValue())
        .sensors(audioDataForm.getSensorsValue())
        .deploymentTime(audioDataForm.getDeploymentTimeTranslator())
        .recoveryTime(audioDataForm.getRecoveryTimeTranslator())
        .audioStartTime(audioDataForm.getAudioStartTimeTranslator())
        .audioEndTime(audioDataForm.getAudioEndTimeTranslator())
        .channelTranslators(((ChannelsForm) channelsForm.getComponent()).toTranslator())
        .build();
  }
  
  private DetectionsPackageTranslator toDetectionsTranslator(PackageTranslator packageTranslator) {
    DetectionsForm detectionsForm = (DetectionsForm) packageDetailForm.getComponent();
    QualityControlForm qualityForm = (QualityControlForm) qualityControlForm.getComponent();
    SoundAnalysisForm<?> analysisForm = (SoundAnalysisForm<?>) soundAnalysisForm.getComponent();
    SoftwareForm<?> softwareDetailForm = (SoftwareForm<?>) softwareForm.getComponent();
    return DetectionsPackageTranslator.toBuilder(packageTranslator)
        .qualityControlDetailTranslator(((QualityControlForm) qualityControlForm.getComponent()).toTranslator())
        .soundSource(detectionsForm.getSoundSourceValue())
        .qualityControlDetailTranslator(qualityForm.toTranslator())
        .analysisTimeZone(analysisForm.getAnalysisTimeZoneValue())
        .analysisEffort(analysisForm.getAnalysisEffortValue())
        .sampleRate(analysisForm.getSampleRateValue())
        .minFrequency(analysisForm.getMinFrequencyValue())
        .maxFrequency(analysisForm.getMaxFrequencyValue())
        .softwareNames(softwareDetailForm.getSoftwareNamesValue())
        .softwareVersions(softwareDetailForm.getSoftwareVersionsValue())
        .softwareProtocolCitation(softwareDetailForm.getSoftwareProtocolCitationValue())
        .softwareDescription(softwareDetailForm.getSoftwareDescriptionValue())
        .softwareProcessingDescription(softwareDetailForm.getSoftwareProcessingDescriptionValue())
        .build();
  }
  
  private SoundClipsPackageTranslator toSoundClipsTranslator(PackageTranslator packageTranslator) {
    SoundClipsForm soundClipsForm = (SoundClipsForm) packageDetailForm.getComponent(); 
    SoftwareForm<?> softwareDetailForm = (SoftwareForm<?>) softwareForm.getComponent();
    return SoundClipsPackageTranslator.toBuilder(packageTranslator)
        .softwareNames(softwareDetailForm.getSoftwareNamesValue())
        .softwareVersions(softwareDetailForm.getSoftwareVersionsValue())
        .softwareProtocolCitation(softwareDetailForm.getSoftwareProtocolCitationValue())
        .softwareDescription(softwareDetailForm.getSoftwareDescriptionValue())
        .softwareProcessingDescription(softwareDetailForm.getSoftwareProcessingDescriptionValue())
        .audioStartTime(soundClipsForm.getAudioStartTimeTranslator())
        .audioEndTime(soundClipsForm.getAudioEndTimeTranslator())
        .build();
  }
  
  private SoundLevelMetricsPackageTranslator toSoundLevelMetricsTranslator(PackageTranslator packageTranslator) {
    SoundLevelMetricsForm soundLevelMetricsForm = (SoundLevelMetricsForm) packageDetailForm.getComponent();
    QualityControlForm qualityForm = (QualityControlForm) qualityControlForm.getComponent();
    SoundAnalysisForm<?> analysisForm = (SoundAnalysisForm<?>) soundAnalysisForm.getComponent();
    SoftwareForm<?> softwareDetailForm = (SoftwareForm<?>) softwareForm.getComponent();
    return SoundLevelMetricsPackageTranslator.toBuilder(packageTranslator)
        .audioStartTimeTranslator(soundLevelMetricsForm.getAudioStartTimeTranslator())
        .audioEndTimeTranslator(soundLevelMetricsForm.getAudioEndTimeTranslator())
        .qualityControlDetailTranslator(qualityForm.toTranslator())
        .analysisTimeZone(analysisForm.getAnalysisTimeZoneValue())
        .analysisEffort(analysisForm.getAnalysisEffortValue())
        .sampleRate(analysisForm.getSampleRateValue())
        .minFrequency(analysisForm.getMinFrequencyValue())
        .maxFrequency(analysisForm.getMaxFrequencyValue())
        .softwareNames(softwareDetailForm.getSoftwareNamesValue())
        .softwareVersions(softwareDetailForm.getSoftwareVersionsValue())
        .softwareProtocolCitation(softwareDetailForm.getSoftwareProtocolCitationValue())
        .softwareDescription(softwareDetailForm.getSoftwareDescriptionValue())
        .softwareProcessingDescription(softwareDetailForm.getSoftwareProcessingDescriptionValue())
        .build();
  }
  
  private SoundPropagationModelsPackageTranslator toSoundPropagationModelsTranslator(PackageTranslator packageTranslator) {
    SoundPropagationModelsForm soundPropagationModelsForm = (SoundPropagationModelsForm) packageDetailForm.getComponent();
    SoftwareForm<?> softwareDetailForm = (SoftwareForm<?>) softwareForm.getComponent();
    return SoundPropagationModelsPackageTranslator.toBuilder(packageTranslator)
        .modeledFrequency(soundPropagationModelsForm.getModeledFrequencyValue())
        .audioStartTimeTranslator(soundPropagationModelsForm.getAudioStartTimeTranslator())
        .audioEndTimeTranslator(soundPropagationModelsForm.getAudioEndTimeTranslator())
        .softwareNames(softwareDetailForm.getSoftwareNamesValue())
        .softwareVersions(softwareDetailForm.getSoftwareVersionsValue())
        .softwareProtocolCitation(softwareDetailForm.getSoftwareProtocolCitationValue())
        .softwareDescription(softwareDetailForm.getSoftwareDescriptionValue())
        .softwareProcessingDescription(softwareDetailForm.getSoftwareProcessingDescriptionValue())
        .build();
  }

  @Override
  protected void updateHeaderOptions(String[] options) {
    contactsTranslatorForm.updateHeaderOptions(options);
    filePathsTranslatorForm.updateHeaderOptions(options);
    calibrationTranslatorForm.updateHeaderOptions(options);
    packageInfoForm.updateHeaderOptions(options);
    locationDetailForm.updateHeaderOptions(options);
    
    if (qualityControlForm != null) {
      ((QualityControlForm) qualityControlForm.getComponent()).updateHeaderOptions(headerOptions);
    }
    if (soundAnalysisForm != null) {
      ((SoundAnalysisForm<?>) soundAnalysisForm.getComponent()).updateHeaderOptions(headerOptions);
    }
    if (channelsForm != null) {
      ((ChannelsForm) channelsForm.getComponent()).updateHeaderOptions(headerOptions);
    }
    if (packageDetailForm != null) {
      Component component = packageDetailForm.getComponent();
      if (component instanceof AudioDataForm<?>) {
        ((AudioDataForm<?>) component).updateHeaderOptions(headerOptions);
      } else if (component instanceof SoundPropagationModelsForm) {
        ((SoundPropagationModelsForm) component).updateHeaderOptions(headerOptions);
      } else if (component instanceof DetectionsForm) {
        ((DetectionsForm) component).updateHeaderOptions(headerOptions);
      } else if (component instanceof SoundLevelMetricsForm) {
        ((SoundLevelMetricsForm) component).updateHeaderOptions(headerOptions);
      } else if (component instanceof SoundClipsForm) {
        ((SoundClipsForm) component).updateHeaderOptions(headerOptions);
      }
    }
    if (softwareForm != null) {
      ((SoftwareForm<?>) softwareForm.getComponent()).updateHeaderOptions(headerOptions);
    }
  }
}
