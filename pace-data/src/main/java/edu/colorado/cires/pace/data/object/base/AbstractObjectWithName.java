package edu.colorado.cires.pace.data.object.base;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
@Jacksonized
public class AbstractObjectWithName {
    private final String name;
    private final UUID uuid;
}
