package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.data.translator.SensorTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import edu.colorado.cires.pace.repository.search.SensorSearchParameters;
import edu.colorado.cires.pace.translator.converter.SensorConverter;
import java.util.List;

public class SensorsPanel extends MetadataPanel<Sensor, SensorTranslator> {

  public SensorsPanel(CRUDRepository<Sensor> repository, TranslatorRepository translatorRepository) {
    super(
        "sensorsPanel",
        repository,
        new String[]{"UUID", "Name", "Position", "Description", "Object"},
        (s) -> new Object[]{s.getUuid(), s.getName(), String.format("(%s, %s, %s)", s.getPosition().getX(), s.getPosition().getY(), s.getPosition().getZ()), s.getDescription(), s},
        Sensor.class,
        (o) -> (Sensor) o[4],
        SensorForm::new,
        translatorRepository,
        new SensorConverter(),
        SensorTranslator.class
    );
  }

  @Override
  protected SearchParameters<Sensor> getSearchParameters(List<String> uniqueFieldSearchTerms) {
    return SensorSearchParameters.builder()
        .names(uniqueFieldSearchTerms)
        .build();
  }

  @Override
  protected List<String> getHiddenColumns() {
    return List.of("UUID", "Object");
  }

  @Override
  protected String getUniqueField(Sensor object) {
    return object.getName();
  }

  @Override
  protected String getHumanReadableObjectName() {
    return "Sensor";
  }
}
