package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.data.translator.SensorTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
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
  protected List<String> getHiddenColumns() {
    return List.of("UUID", "Object");
  }
}
