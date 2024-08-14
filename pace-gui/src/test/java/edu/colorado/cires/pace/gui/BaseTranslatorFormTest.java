package edu.colorado.cires.pace.gui;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.Assert.assertNotNull;

import edu.colorado.cires.pace.data.object.base.Translator;
import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import java.util.Arrays;
import java.util.UUID;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

public abstract class BaseTranslatorFormTest<T extends Translator, F extends BaseTranslatorForm<T>> {
  
  protected abstract String[] getHeaderOptions();
  protected abstract F createForm(T t, String[] headerOptions);
  protected abstract void assertTranslatorEqualsHeaderOptions(T translator);
  protected abstract void populateInitialForm(BaseTranslatorForm<T> translatorForm);
  
  private BaseTranslatorForm<T> translatorForm;

  @Before
  public void setUp() {
    Boolean headless = ApplicationPropertyResolver.getPropertyValue("java.awt.headless", Boolean::parseBoolean);
    assertNotNull(headless);
    assertTrue(headless, "Test must run in headless mode");
  }

  @Test
  public void testForm() {
    translatorForm = createForm(null, getHeaderOptions());

    UUID uuid = UUID.randomUUID();
    String name = "test-translator";
    populateInitialForm(translatorForm);
    T translator = translatorForm.toTranslator(uuid, name);
    assertTranslatorEqualsHeaderOptions(translator);
    
    translator = createForm(translator, new String[0]).toTranslator(uuid, name);
    assertTranslatorEqualsHeaderOptions(translator);
  }
  
  protected void selectComboBoxOption(String name, String optionToSelect) {
    JComponent component = (JComponent) translatorForm.getComponents()[0];
    component = (JComponent) component.getComponents()[0];
    component = (JComponent) component.getComponents()[0];
    component = (JComponent) component.getComponents()[0];

    JComboBox<?> comboBox = Arrays.stream(component.getComponents())
        .filter(c -> StringUtils.isNotBlank(c.getName()))
        .filter(c -> c.getName().equals(name))
        .filter(c -> c instanceof JComboBox<?>)
        .map(c -> (JComboBox<?>) c)
        .findFirst().orElseThrow();
    
    comboBox.setSelectedItem(optionToSelect);
  }

}
