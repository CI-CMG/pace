package edu.colorado.cires.pace.gui;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.Assert.assertNotNull;

import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
  public void setUp() {
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
  
  private void selectComboBoxOption(JComponent component, String name, String optionToSelect) {
    JComboBox<?> comboBox = Arrays.stream(component.getComponents())
        .filter(c -> StringUtils.isNotBlank(c.getName()))
        .filter(c -> c.getName().equals(name))
        .filter(c -> c instanceof JComboBox<?>)
        .map(c -> (JComboBox<?>) c)
        .findFirst().orElseThrow();
    comboBox.setSelectedItem(optionToSelect);
  }
  
  protected void selectTimeOptions(String name, String time, String timeZone) {
    TimeTranslatorForm timeTranslatorForm = Arrays.stream(form.getComponents())
        .filter(c -> StringUtils.isNotBlank(c.getName()))
        .filter(c -> c.getName().equals(name))
        .filter(c -> c instanceof TimeTranslatorForm)
        .map(c -> (TimeTranslatorForm) c)
        .findFirst().orElseThrow();
    
    selectComboBoxOption(timeTranslatorForm, "time", time);
    selectComboBoxOption(timeTranslatorForm, "timeZone", timeZone);
    
    
  } 


}