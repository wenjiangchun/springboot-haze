package com.haze.spatial.geoserver;

import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.GSResourceEncoder;
import it.geosolutions.geoserver.rest.encoder.coverage.GSCoverageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class ServicePublishUtils {

    private static final Logger logger = LoggerFactory.getLogger(ServicePublishUtils.class);
    private static final String URL = "http://192.168.31.178:8080/geoserver/";
    private static final String USER = "admin";
    private static final String PASSWORD = "geoserver";
    public static void publish(String fileName, String workspace, String storeName) {
        try {
            logger.debug("开始连接geoserver【url={}, user={}, password={}】", URL, USER, PASSWORD);
            GeoServerRESTManager manager = new GeoServerRESTManager(new URL(URL), USER, PASSWORD);
            logger.debug("geoserver已连接");
            GeoServerRESTPublisher publisher = manager.getPublisher();
            logger.debug("准备发布tiff, file=【{}】", fileName);
            publisher.publishGeoTIFF(workspace, storeName, Paths.get(fileName).toFile());
            logger.debug("发布成功, workspace=【{}】, storeName=【{}】", workspace, storeName);
        } catch (MalformedURLException | FileNotFoundException e) {
            logger.error("发布失败", e);
            //e.printStackTrace();
        }
    }

    public static void publishDem(String fileName, String workspace, String storeName, String coverageName) {
        try {
            logger.debug("开始连接geoserver【url={}, user={}, password={}】", URL, USER, PASSWORD);
            GeoServerRESTManager manager = new GeoServerRESTManager(new URL(URL), USER, PASSWORD);
            logger.debug("geoserver已连接");
            GeoServerRESTPublisher publisher = manager.getPublisher();
            logger.debug("准备发布tiff, file=【{}】", fileName);
            publisher.publishExternalGeoTIFF(workspace, storeName, Paths.get(fileName).toFile(),"moon", "EPSG:14903", GSResourceEncoder.ProjectionPolicy.NONE, "default");
            logger.debug("发布成功, workspace=【{}】, storeName=【{}】", workspace, storeName);
        } catch (MalformedURLException | FileNotFoundException e) {
            logger.error("发布失败", e);
            //e.printStackTrace();
        }
    }
}
