package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.contact.organization.Organization;
import edu.colorado.cires.pace.data.object.contact.organization.translator.OrganizationTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.converter.OrganizationConverter;
import java.util.UUID;

public class OrganizationsPanel extends MetadataPanel<Organization, OrganizationTranslator> {

  public OrganizationsPanel(CRUDRepository<Organization> repository,
      TranslatorRepository translatorRepository) {
    super(
        "organizationsPanel",
        repository,
        new String[]{"UUID", "Name", "Street", "City", "State", "Zip", "Country", "Email", "Phone", "Visible"},
        (organization) -> new Object[]{organization.getUuid(), organization.getName(), organization.getStreet(), organization.getCity(),
            organization.getState(), organization.getZip(), organization.getCountry(), organization.getEmail(), organization.getPhone(), 
            organization.isVisible()},
        Organization.class,
        (objects) -> Organization.builder()
            .uuid((UUID) objects[0])
            .name((String) objects[1])
            .street((String) objects[2])
            .city((String) objects[3])
            .state((String) objects[4])
            .zip((String) objects[5])
            .country((String) objects[6])
            .email((String) objects[7])
            .phone((String) objects[8])
            .visible((Boolean) objects[9])
            .build(),
        OrganizationForm::new,
        translatorRepository,
        new OrganizationConverter(),
        OrganizationTranslator.class
    );
  }

  @Override
  protected String getUniqueField(Organization object) {
    return object.getName();
  }

  @Override
  protected String getHumanReadableObjectName() {
    return "Organization";
  }
}
