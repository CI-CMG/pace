package edu.colorado.cires.pace.data.object.dataset.base;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "type")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public abstract class DetailedPackage extends BasePackage<Object> {
  private final Object datasetPackager;
  @Builder.Default
  private final List<Object> scientists = Collections.emptyList();
  @Builder.Default
  private final List<Object> projects = Collections.emptyList();
  @Builder.Default
  private final List<Object> sponsors = Collections.emptyList();
  @Builder.Default
  private final List<Object> funders = Collections.emptyList();
  private final String platform;
  private final Object instrument;

  @Override
  protected List<String> getProjectNames() {
    return getProjects().stream()
            .map(o -> (String) o)
            .toList();
  }
}
