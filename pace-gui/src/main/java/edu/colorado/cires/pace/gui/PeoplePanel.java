package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.contact.person.Person;
import edu.colorado.cires.pace.data.object.contact.person.translator.PersonTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.converter.PersonConverter;
import java.util.UUID;

/**
 * PeoplePanel extends MetadataPanel
 */
public class PeoplePanel extends MetadataPanel<Person, PersonTranslator> {

  /**
   * Creates a people panel
   * @param repository holds existing people objects
   * @param translatorRepository holds existing people translators
   */
  public PeoplePanel(CRUDRepository<Person> repository, TranslatorRepository translatorRepository) {
    super(
        "peoplePanel",
        repository,
        new String[]{"UUID", "Name", "Organization", "Position", "Street", "City", "State", "Zip", "Country", "Email",
            "Phone", "Orcid", "Visible"},
        (person) -> new Object[]{person.getUuid(), person.getName(), person.getOrganization(), person.getPosition(),
            person.getStreet(), person.getCity(), person.getState(), person.getZip(), person.getCountry(), person.getEmail(),
            person.getPhone(), person.getOrcid(), person.isVisible()},
        Person.class,
        (objects) -> Person.builder()
            .uuid((UUID) objects[0])
            .name((String) objects[1])
            .organization((String) objects[2])
            .position((String) objects[3])
            .street((String) objects[4])
            .city((String) objects[5])
            .state((String) objects[6])
            .zip((String) objects[7])
            .country((String) objects[8])
            .email((String) objects[9])
            .phone((String) objects[10])
            .orcid((String) objects[11])
            .visible((Boolean) objects[12])
            .build(),
        PersonForm::new,
        translatorRepository,
        new PersonConverter(),
        PersonTranslator.class
    );
  }

  @Override
  protected String getUniqueField(Person object) {
    return object.getName();
  }

  @Override
  protected String getHumanReadableObjectName() {
    return "Person";
  }
}
