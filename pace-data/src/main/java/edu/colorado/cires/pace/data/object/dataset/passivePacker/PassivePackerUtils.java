package edu.colorado.cires.pace.data.object.dataset.passivePacker;

public class PassivePackerUtils {
  
  public static <L extends PassivePackerLocation, B extends PassivePackerLocation.PassivePackerLocationBuilder<L, ?>> B builderFromBaseLocation(PassivePackerLocation baseLocation, B childLocationBuilder) {
    return (B) childLocationBuilder
        .deployType(baseLocation.getDeployType());
  }
  
  public static <D extends PassivePackerDeployment, B extends PassivePackerDeployment.PassivePackerDeploymentBuilder<D, ?>> B builderFromBaseDeployment(PassivePackerDeployment baseDeployment, B childDeploymentBuilder) {
    return (B) childDeploymentBuilder
        .location(baseDeployment.getLocation());
  }
  
  public static <C extends PassivePackerCalibrationInfo, B extends PassivePackerCalibrationInfo.PassivePackerCalibrationInfoBuilder<C, ?>> B builderFromBaseCalibrationInfo(PassivePackerCalibrationInfo baseCalibrationInfo, B childCalibrationBuilder) {
    return (B) childCalibrationBuilder
        .calState(baseCalibrationInfo.getCalState())
        .calDocsPath(baseCalibrationInfo.getCalDocsPath())
        .comment(baseCalibrationInfo.getComment())
        .calDate(baseCalibrationInfo.getCalDate());
  }
  
  public static <D extends PassivePackerDatasetDetails, B extends PassivePackerDatasetDetails.PassivePackerDatasetDetailsBuilder<D, ?>> B builderFromBaseDatasetDetails(PassivePackerDatasetDetails baseDatasetDetails, B childDetailBuilder) {
    return (B) childDetailBuilder
        .type(baseDatasetDetails.getType())
        .subType(baseDatasetDetails.getSubType());
  }
  
  public static <P extends PassivePackerPackage, B extends PassivePackerPackage.PassivePackerPackageBuilder<P, ?>> B builderFromBasePackage(PassivePackerPackage basePackage, B childPackageBuilder) {
    return (B) childPackageBuilder
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
        .deployment(basePackage.getDeployment());
  }

}
