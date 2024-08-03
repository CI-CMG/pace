package edu.colorado.cires.pace.data.object.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
@Jacksonized
public class AbstractObjectWithName {
    @JsonProperty("name")
    private final String name;
    @JsonProperty("uuid")
    private final UUID uuid;
}
