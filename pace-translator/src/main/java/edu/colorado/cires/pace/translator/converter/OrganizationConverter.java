package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;

import edu.colorado.cires.pace.data.object.contact.organization.Organization;
import edu.colorado.cires.pace.data.object.contact.organization.translator.OrganizationTranslator;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;

/**
 * OrganizationConverter extends Converter and provides convert function for
 * Organization objects
 */
public class OrganizationConverter extends Converter<OrganizationTranslator, Organization> {

  /**
   * Creates an organization object from the provided properties
   * @param translator translates to organization object
   * @param properties maps property names to values
   * @param row relevant row
   * @param runtimeException thrown in case of error mapping
   * @return Organization object
   */
  @Override
  public Organization convert(OrganizationTranslator translator, Map<String, ValueWithColumnNumber> properties, int row,
      RuntimeException runtimeException) {
    return Organization.builder()
        .uuid(uuidFromMap(properties, "UUID", translator.getOrganizationUUID(), row, runtimeException))
        .name(stringFromMap(properties, translator.getOrganizationName()))
        .street(stringFromMap(properties, translator.getStreet()))
        .city(stringFromMap(properties, translator.getCity()))
        .state(stringFromMap(properties, translator.getState()))
        .zip(stringFromMap(properties, translator.getZip()))
        .country(stringFromMap(properties, translator.getCountry()))
        .email(stringFromMap(properties, translator.getEmail()))
        .phone(stringFromMap(properties, translator.getPhone()))
        .build();
  }
}
