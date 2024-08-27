package edu.colorado.cires.pace.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.PackageSensorTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.PositionTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.translator.AudioDataPackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;

public class AudioDataFormTest extends AuxiliaryTranslatorFormTest<AudioDataPackageTranslator, AudioDataForm<AudioDataPackageTranslator>> {

  @Override
  protected AudioDataForm<AudioDataPackageTranslator> createForm(AudioDataPackageTranslator translator, String[] headerOptions) {
    return new AudioDataForm<>(headerOptions, translator);
  }

  @Override
  protected String[] getHeaderOptions() {
    return new String[] {
        "Instrument ID", "Comments", "Deployment Time", "Recovery Time",
        "Audio Start Time", "Audio End Time", "Time Zone",
        "Sensor 1 Name", "Sensor 1 X", "Sensor 1 Y", "Sensor 1 Z",
        "Sensor 2 Name", "Sensor 2 X", "Sensor 2 Y", "Sensor 2 Z"
    };
  }

  @Override
  protected void populateInitialForm(AudioDataForm<AudioDataPackageTranslator> form) {
    selectComboBoxOption("instrumentId", "Instrument ID");
    selectComboBoxOption("comments", "Comments");
    selectTimeOptions("deploymentTime", "Deployment Time", "Time Zone");
    selectTimeOptions("recoveryTime", "Recovery Time", "Time Zone");
    selectTimeOptions("audioStartTime", "Audio Start Time", "Time Zone");
    selectTimeOptions("audioEndTime", "Audio End Time", "Time Zone");
    
    JPanel sensors = getPanel("sensors");
    clickButton(sensors, "Add Sensor");
    selectSensorOptions(
        getSensorForm(
            sensors, 0
        ),
        "Sensor 1 Name",
        "Sensor 1 X",
        "Sensor 1 Y",
        "Sensor 1 Z"
    );
    clickButton(sensors, "Add Sensor");
    selectSensorOptions(
        getSensorForm(
            sensors, 1
        ),
        "Sensor 2 Name",
        "Sensor 2 X",
        "Sensor 2 Y",
        "Sensor 2 Z"
    );
  }

  private PackageSensorTranslatorForm getSensorForm(JPanel sensors, int sensorNumber) {
    List<PackageSensorTranslatorForm> packageSensorTranslatorForms = Arrays.stream(sensors.getComponents())
        .filter(c -> c instanceof JPanel)
        .map(c -> Arrays.asList(((JPanel) c).getComponents()))
        .flatMap(List::stream)
        .filter(c -> c instanceof CollapsiblePanel<?>)
        .map(c -> (CollapsiblePanel<?>) c)
        .map(CollapsiblePanel::getContentPanel)
        .filter(c -> c instanceof PackageSensorTranslatorForm)
        .map(c -> ((PackageSensorTranslatorForm) c))
        .toList();

    return packageSensorTranslatorForms.get(sensorNumber);
  }

  @Override
  protected void assertTranslatorEqualsHeaderOptions(AudioDataPackageTranslator translator) {
    assertEquals("Instrument ID", translator.getInstrumentId());
    assertEquals("Comments", translator.getComments());
    TimeTranslator timeTranslator = translator.getDeploymentTime();
    assertEquals("Deployment Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
    timeTranslator = translator.getRecoveryTime();
    assertEquals("Recovery Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
    timeTranslator = translator.getAudioStartTime();
    assertEquals("Audio Start Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
    timeTranslator = translator.getAudioEndTime();
    assertEquals("Audio End Time", timeTranslator.getTime());
    assertEquals("Time Zone", timeTranslator.getTimeZone());
    List<PackageSensorTranslator> sensorTranslators = translator.getSensors();
    assertEquals(2, sensorTranslators.size());
    PackageSensorTranslator packageSensorTranslator = sensorTranslators.get(0);
    assertEquals("Sensor 1 Name", packageSensorTranslator.getName());
    PositionTranslator positionTranslator = packageSensorTranslator.getPosition();
    assertEquals("Sensor 1 X", positionTranslator.getX());
    assertEquals("Sensor 1 Y", positionTranslator.getY());
    assertEquals("Sensor 1 Z", positionTranslator.getZ());
    packageSensorTranslator = sensorTranslators.get(1);
    assertEquals("Sensor 2 Name", packageSensorTranslator.getName());
    positionTranslator = packageSensorTranslator.getPosition();
    assertEquals("Sensor 2 X", positionTranslator.getX());
    assertEquals("Sensor 2 Y", positionTranslator.getY());
    assertEquals("Sensor 2 Z", positionTranslator.getZ());
  }

  @Override
  protected void assertTranslatorEqualsNewHeaderOptions(AudioDataPackageTranslator translator) {
    assertEquals("Instrument ID", translator.getInstrumentId());
    assertNull(translator.getComments());
    TimeTranslator timeTranslator = translator.getDeploymentTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    timeTranslator = translator.getRecoveryTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    timeTranslator = translator.getAudioStartTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    timeTranslator = translator.getAudioEndTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    List<PackageSensorTranslator> sensorTranslators = translator.getSensors();
    assertEquals(2, sensorTranslators.size());
    PackageSensorTranslator packageSensorTranslator = sensorTranslators.get(0);
    assertNull(packageSensorTranslator.getName());
    PositionTranslator positionTranslator = packageSensorTranslator.getPosition();
    assertNull(positionTranslator.getX());
    assertNull(positionTranslator.getY());
    assertNull(positionTranslator.getZ());
    packageSensorTranslator = sensorTranslators.get(1);
    assertNull(packageSensorTranslator.getName());
    positionTranslator = packageSensorTranslator.getPosition();
    assertNull(positionTranslator.getX());
    assertNull(positionTranslator.getY());
    assertNull(positionTranslator.getZ());
  }

  @Override
  public void testForm() {
    super.testForm();
    
    
    JPanel sensors = getPanel("sensors");
    clickButton(
        getSensorForm(sensors, 0),
        "Remove Sensor"
    );
    
    AudioDataPackageTranslator translator = getTranslator();

    assertEquals("Instrument ID", translator.getInstrumentId());
    assertNull(translator.getComments());
    TimeTranslator timeTranslator = translator.getDeploymentTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    timeTranslator = translator.getRecoveryTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    timeTranslator = translator.getAudioStartTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    timeTranslator = translator.getAudioEndTime();
    assertNull(timeTranslator.getTime());
    assertNull(timeTranslator.getTimeZone());
    List<PackageSensorTranslator> sensorTranslators = translator.getSensors();
    assertEquals(1, sensorTranslators.size());
    PackageSensorTranslator packageSensorTranslator = sensorTranslators.get(0);
    assertNull(packageSensorTranslator.getName());
    PositionTranslator positionTranslator = packageSensorTranslator.getPosition();
    assertNull(positionTranslator.getX());
    assertNull(positionTranslator.getY());
    assertNull(positionTranslator.getZ());
  }
}