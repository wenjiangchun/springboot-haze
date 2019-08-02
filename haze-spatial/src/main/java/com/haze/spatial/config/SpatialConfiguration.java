package com.haze.spatial.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties(SpatialProperties.class)
@PropertySource("classpath:/config/spatial/application.properties")
public class SpatialConfiguration {

}
