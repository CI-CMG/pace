package edu.colorado.cires.pace.gui;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.Assert.assertNotNull;

import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

public abstract class AuxiliaryTranslatorFormTest<T, F extends AuxiliaryTranslatorForm<T>> {
  
  protected abstract F createForm(T translator, String[] headerOptions);
  protected abstract String[] getHeaderOptions();
  protected abstract void populateInitialForm(F form);
  protected abstract void assertTranslatorEqualsHeaderOptions(T translator);
  protected abstract void assertTranslatorEqualsNewHeaderOptions(T translator);
  
  private F form;

  @Before
  public void setUp() throws IOException {
    Path testPath = Paths.get("target").resolve("test-dir").resolve("test-metadata").toAbsolutePath();
    System.setProperty("pace.metadata-directory", testPath.toString());
    FileUtils.forceMkdir(testPath.toFile());
    Boolean headless = ApplicationPropertyResolver.getPropertyValue("java.awt.headless", Boolean::parseBoolean);
    assertNotNull(headless);
    assertTrue(headless, "Test must run in headless mode");
  }

  @Test
  public void testForm() {
    form = createForm(null, getHeaderOptions());
    populateInitialForm(form);
    T translator = form.toTranslator();
    assertTranslatorEqualsHeaderOptions(translator);
    
    translator = createForm(translator, getHeaderOptions()).toTranslator();
    assertTranslatorEqualsHeaderOptions(translator);

    form.updateHeaderOptions(getHeaderOptions());
    translator = form.toTranslator();
    assertTranslatorEqualsHeaderOptions(translator);
    
    form.updateHeaderOptions(new String[] { getHeaderOptions()[0] });
    translator = form.toTranslator();
    assertTranslatorEqualsNewHeaderOptions(translator);
  }

  protected void selectComboBoxOption(String name, String optionToSelect) {
    selectComboBoxOption((JComponent) form, name, optionToSelect);
  }
  
  protected void selectComboBoxOption(JComponent component, String name, String optionToSelect) {
    JComboBox<?> comboBox = Arrays.stream(component.getComponents())
        .filter(c -> StringUtils.isNotBlank(c.getName()))
        .filter(c -> c.getName().equals(name))
        .filter(c -> c instanceof JComboBox<?>)
        .map(c -> (JComboBox<?>) c)
        .findFirst().orElseThrow();
    comboBox.setSelectedItem(optionToSelect);
  }
  
  protected void selectTimeOptions(JComponent component, String name, String time, String timeZone) {
    TimeTranslatorForm timeTranslatorForm = Arrays.stream(component.getComponents())
        .filter(c -> StringUtils.isNotBlank(c.getName()))
        .filter(c -> c.getName().equals(name))
        .filter(c -> c instanceof TimeTranslatorForm)
        .map(c -> (TimeTranslatorForm) c)
        .findFirst().orElseThrow();
        ;
    selectComboBoxOption(timeTranslatorForm, "time", time);
    selectComboBoxOption(timeTranslatorForm, "timeZone", timeZone);
  }

  protected void selectDateOptions(String name, String date, String timeZone) {
    selectDateOptions((JComponent) form, name, date, timeZone);
  }

  protected void selectDateOptions(JComponent component, String name, String date, String timeZone) {
    DateTranslatorForm dateTranslatorForm = Arrays.stream(component.getComponents())
        .filter(c -> StringUtils.isNotBlank(c.getName()))
        .filter(c -> c.getName().equals(name))
        .filter(c -> c instanceof DateTranslatorForm)
        .map(c -> (DateTranslatorForm) c)
        .findFirst().orElseThrow();
    ;
    selectComboBoxOption(dateTranslatorForm, "date", date);
    selectComboBoxOption(dateTranslatorForm, "timeZone", timeZone);
  }
  
  protected void selectTimeOptions(String name, String time, String timeZone) {
    selectTimeOptions((JComponent) form, name, time, timeZone);
  }
  
  protected void selectSensorOptions(String name, String sensorName, String sensorX, String sensorY, String sensorZ) {
    PackageSensorTranslatorForm sensorTranslatorForm = Arrays.stream(form.getComponents())
        .filter(c -> StringUtils.isNotBlank(c.getName()))
        .filter(c -> c.getName().equals(name))
        .filter(c -> c instanceof PackageSensorTranslatorForm)
        .map(c -> (PackageSensorTranslatorForm) c)
        .findFirst().orElseThrow();
    selectSensorOptions(sensorTranslatorForm, sensorName, sensorX, sensorY, sensorZ);
  }
  
  protected void selectSensorOptions(JPanel sensorTranslatorForm, String sensorName, String sensorX, String sensorY, String sensorZ) {
    Arrays.stream(sensorTranslatorForm.getComponents())
        .filter(c -> c instanceof JComboBox<?>)
        .map(c -> (JComboBox<?>) c)
        .findFirst().orElseThrow()
        .setSelectedItem(sensorName);

    PositionTranslatorForm positionTranslatorForm = Arrays.stream(sensorTranslatorForm.getComponents())
        .filter(c -> c instanceof PositionTranslatorForm)
        .map(c -> (PositionTranslatorForm) c)
        .findFirst().orElseThrow();
    selectComboBoxOption(positionTranslatorForm, "x", sensorX);
    selectComboBoxOption(positionTranslatorForm, "y", sensorY);
    selectComboBoxOption(positionTranslatorForm, "z", sensorZ);
  }
  
  protected JPanel getPanel(String panelName) {
    return Arrays.stream(form.getComponents())
        .filter(c -> StringUtils.isNotBlank(c.getName()))
        .filter(c -> c.getName().equals(panelName))
        .filter(c -> c instanceof JPanel)
        .map(c -> (JPanel) c)
        .findFirst().orElseThrow();
  }
  
  protected void clickButton(JComponent component, String buttonText) {
    Arrays.stream(component.getComponents())
        .filter(c -> c instanceof JButton)
        .map(c -> (JButton) c)
        .filter(b -> b.getText().equals(buttonText))
        .findFirst().orElseThrow()
        .doClick();
  }
  
  protected T getTranslator() {
    return form.toTranslator();
  }


}