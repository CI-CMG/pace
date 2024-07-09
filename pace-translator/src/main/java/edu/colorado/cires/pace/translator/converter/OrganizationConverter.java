package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;

import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.translator.OrganizationTranslator;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;

public class OrganizationConverter extends Converter<OrganizationTranslator, Organization> {

  @Override
  public Organization convert(OrganizationTranslator translator, Map<String, ValueWithColumnNumber> properties, int row,
      RuntimeException runtimeException) {
    return Organization.builder()
        .uuid(uuidFromMap(properties, translator.getOrganizationUUID(), row, runtimeException))
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
