package com.haze.kettle.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties(KettleProperties.class)
@PropertySource("classpath:/config/kettle/application.properties")
public class KettleConfiguration {

    /*@Bean
    public KettleProperties getKettleProperties() {
        return new KettleProperties();
    }*/
}
