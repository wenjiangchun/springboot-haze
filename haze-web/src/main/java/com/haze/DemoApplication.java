package com.haze;

import com.haze.spatial.config.SpatialProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableJpaRepositories(repositoryBaseClass = com.haze.core.jpa.SimpleBaseRepository.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class DemoApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
		try {
			Class<?> c = Class.forName("org.pentaho.di.core.database.MySQLDatabaseMeta");
			System.out.println(c.getProtectionDomain().getCodeSource().getLocation());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		SpatialProperties spatialProperties = ctx.getBean(SpatialProperties.class);
		System.out.println(spatialProperties.getName());
	}
}
