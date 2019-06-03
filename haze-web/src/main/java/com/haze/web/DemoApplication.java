package com.haze.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@ComponentScan(value = "com.haze")
@EntityScan("com.haze")
@EnableJpaRepositories(repositoryBaseClass = com.haze.core.jpa.SimpleBaseRepository.class, basePackages= "com.haze")
@EnableTransactionManagement(proxyTargetClass = true)
public class DemoApplication {
	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);
		try {
			Class<?> c = Class.forName("org.pentaho.di.core.database.MySQLDatabaseMeta");
			System.out.println(c.getProtectionDomain().getCodeSource().getLocation());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
