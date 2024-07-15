package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.translator.OrganizationTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.repository.search.OrganizationSearchParameters;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import edu.colorado.cires.pace.translator.converter.OrganizationConverter;
import java.util.List;
import java.util.UUID;

public class OrganizationsPanel extends MetadataPanel<Organization, OrganizationTranslator> {

  public OrganizationsPanel(CRUDRepository<Organization> repository,
      TranslatorRepository translatorRepository) {
    super(
        "organizationsPanel",
        repository,
        new String[]{"UUID", "Name", "Street", "City", "State", "Zip", "Country", "Email", "Phone"},
        (person) -> new Object[]{person.getUuid(), person.getName(), person.getStreet(), person.getCity(),
            person.getState(), person.getZip(), person.getCountry(), person.getEmail(), person.getPhone()},
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
            .build(),
        OrganizationForm::new,
        translatorRepository,
        new OrganizationConverter(),
        OrganizationTranslator.class
    );
  }

  @Override
  protected SearchParameters<Organization> getSearchParameters(List<String> uniqueFieldSearchTerms) {
    return OrganizationSearchParameters.builder()
        .names(uniqueFieldSearchTerms)
        .build();
  }
}