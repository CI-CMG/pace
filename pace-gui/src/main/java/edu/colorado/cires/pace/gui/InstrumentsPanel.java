package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.data.translator.InstrumentTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.FileTypeRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.repository.search.InstrumentSearchParameters;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import edu.colorado.cires.pace.translator.converter.InstrumentConverter;
import java.util.List;
import java.util.stream.Collectors;

public class InstrumentsPanel extends MetadataPanel<Instrument, InstrumentTranslator> {

  public InstrumentsPanel(
      CRUDRepository<Instrument> repository,
      FileTypeRepository fileTypeRepository,
      TranslatorRepository translatorRepository
  ) {
    super(
        "instrumentsPanel",
        repository,
        new String[]{"UUID", "Name", "File Types", "Object"},
        (i) -> new Object[]{i.getUuid(), i.getName(), i.getFileTypes().stream()
            .map(FileType::getType)
            .collect(Collectors.joining(", ")), i},
        Instrument.class,
        (o) -> (Instrument) o[3],
        (i) -> new InstrumentForm(i, fileTypeRepository),
        translatorRepository,
        new InstrumentConverter(fileTypeRepository),
        InstrumentTranslator.class
    );
  }

  @Override
  protected SearchParameters<Instrument> getSearchParameters(List<String> uniqueFieldSearchTerms) {
    return InstrumentSearchParameters.builder()
        .names(uniqueFieldSearchTerms)
        .build();
  }

  @Override
  protected List<String> getHiddenColumns() {
    return List.of("UUID", "Object");
  }
}
