package edu.colorado.cires.pace.gui;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.contact.Contact;
import javax.swing.JPanel;

abstract class ContactFormTest<C extends Contact, F extends ContactForm<C>> extends ObjectWithNameFormTest<C, F> {

  @Override
  protected void populateAdditionalFormFields(C object, JPanel contentPanel) {
    updateTextField(contentPanel, "street", object.getStreet());
    updateTextField(contentPanel, "city", object.getCity());
    updateTextField(contentPanel, "state", object.getState());
    updateTextField(contentPanel, "zip", object.getZip());
    updateTextField(contentPanel, "country", object.getCountry());
    updateTextField(contentPanel, "email", object.getEmail());
    updateTextField(contentPanel, "phone", object.getPhone());
  }

  @Override
  protected void assertAdditionalFieldsEqual(C expected, C actual) {
    assertEquals(expected.getStreet(), actual.getStreet());
    assertEquals(expected.getCity(), actual.getCity());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getZip(), actual.getZip());
    assertEquals(expected.getCountry(), actual.getCountry());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getPhone(), actual.getPhone());
  }
}