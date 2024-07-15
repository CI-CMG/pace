package edu.colorado.cires.pace.repository.search;

import edu.colorado.cires.pace.data.object.Instrument;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Jacksonized
public class InstrumentSearchParameters extends ObjectWithNameSearchParameters<Instrument> {
}
