package edu.colorado.cires.pace.repository.search;

import edu.colorado.cires.pace.data.object.Sensor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class SensorSearchParameters extends ObjectWithNameSearchParameters<Sensor> {

}
