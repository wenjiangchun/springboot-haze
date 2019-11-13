package com.haze.spatial.geoserver;

import org.junit.Test;

import static org.junit.Assert.*;

public class ServicePublishUtilsTest {

    //@Test
    public void publish() {
        //ServicePublishUtils.publish("E:\\images.tif", "demo", "tif");
        ServicePublishUtils.publishDem("d:\\008.IMG", "demo", "moon","moon");
    }
}