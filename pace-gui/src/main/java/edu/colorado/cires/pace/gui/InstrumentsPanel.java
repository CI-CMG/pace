package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.instrument.Instrument;
import edu.colorado.cires.pace.data.object.instrument.translator.InstrumentTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.FileTypeRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.converter.InstrumentConverter;
import java.util.List;

public class InstrumentsPanel extends MetadataPanel<Instrument, InstrumentTranslator> {

  public InstrumentsPanel(
      CRUDRepository<Instrument> repository,
      FileTypeRepository fileTypeRepository,
      TranslatorRepository translatorRepository
  ) {
    super(
        "instrumentsPanel",
        repository,
        new String[]{"UUID", "Name", "File Types", "Visible", "Object"},
        (i) -> new Object[]{i.getUuid(), i.getName(), String.join(", ", i.getFileTypes()), i},
        Instrument.class,
        (o) -> (Instrument) o[3],
        (i) -> InstrumentForm.create(i, fileTypeRepository),
        translatorRepository,
        new InstrumentConverter(),
        InstrumentTranslator.class
    );
  }

  @Override
  protected List<String> getHiddenColumns() {
    return List.of("UUID", "Object", "Visible");
  }

  @Override
  protected String getUniqueField(Instrument object) {
    return object.getName();
  }

  @Override
  protected String getHumanReadableObjectName() {
    return "Instrument";
  }
}
