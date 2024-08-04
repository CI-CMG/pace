package edu.colorado.cires.pace.data.object.dataset.passivePacker;

public class PassivePackerUtils {
  
  public static <L extends PassivePackerLocation, B extends PassivePackerLocation.PassivePackerLocationBuilder<L, ?>> L fromBaseLocation(PassivePackerLocation baseLocation, B childLocationBuilder) {
    return childLocationBuilder
        .deployType(baseLocation.getDeployType())
        .build();
  }
  
  public static <D extends PassivePackerDeployment, B extends PassivePackerDeployment.PassivePackerDeploymentBuilder<D, ?>> D fromBaseDeployment(PassivePackerDeployment baseDeployment, B childDeploymentBuilder) {
    return childDeploymentBuilder
        .location(baseDeployment.getLocation())
        .build();
  }
  
  public static <C extends PassivePackerCalibrationInfo, B extends PassivePackerCalibrationInfo.PassivePackerCalibrationInfoBuilder<C, ?>> C fromBaseCalibrationInfo(PassivePackerCalibrationInfo baseCalibrationInfo, B childCalibrationBuilder) {
    return childCalibrationBuilder
        .calState(baseCalibrationInfo.getCalState())
        .calDocsPath(baseCalibrationInfo.getCalDocsPath())
        .comment(baseCalibrationInfo.getComment())
        .calDate(baseCalibrationInfo.getCalDate())
        .build();
  }
  
  public static <D extends PassivePackerDatasetDetails, B extends PassivePackerDatasetDetails.PassivePackerDatasetDetailsBuilder<D, ?>> D fromBaseDatasetDetails(PassivePackerDatasetDetails baseDatasetDetails, B childDetailBuilder) {
    return childDetailBuilder
        .type(baseDatasetDetails.getType())
        .subType(baseDatasetDetails.getSubType())
        .build();
  }
  
  public static <P extends PassivePackerPackage, B extends PassivePackerPackage.PassivePackerPackageBuilder<P, ?>> P fromBasePackage(PassivePackerPackage basePackage, B childPackageBuilder) {
    return childPackageBuilder
        .dataCollectionName(basePackage.getDataCollectionName())
        .publishDate(basePackage.getPublishDate())
        .projectName(basePackage.getProjectName())
        .deploymentName(basePackage.getDeploymentName())
        .deploymentAlias(basePackage.getDeploymentAlias())
        .site(basePackage.getSite())
        .siteAliases(basePackage.getSiteAliases())
        .title(basePackage.getTitle())
        .purpose(basePackage.getPurpose())
        .description(basePackage.getDescription())
        .platformName(basePackage.getPlatformName())
        .instrumentType(basePackage.getInstrumentType())
        .metadataAuthor(basePackage.getMetadataAuthor())
        .scientists(basePackage.getScientists())
        .sponsors(basePackage.getSponsors())
        .funders(basePackage.getFunders())
        .calibrationInfo(basePackage.getCalibrationInfo())
        .datasetDetails(basePackage.getDatasetDetails())
        .deployment(basePackage.getDeployment())
        .build();
  }

}
