package com.haze;

import com.haze.spatial.config.SpatialProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableJpaRepositories(repositoryBaseClass = com.haze.core.jpa.SimpleBaseRepository.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class DemoApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
		try {
			Class<?> c = Class.forName("org.pentaho.di.core.database.MySQLDatabaseMeta11");
			System.out.println(c.getProtectionDomain().getCodeSource().getLocation());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		SpatialProperties spatialProperties = ctx.getBean(SpatialProperties.class);
		System.out.println(spatialProperties.getName());
	}

	private CorsConfiguration buildConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*"); // 1允许任何域名使用
		corsConfiguration.addAllowedHeader("*"); // 2允许任何头
		corsConfiguration.addAllowedMethod("*"); // 3允许任何方法（post、get等）
		return corsConfiguration;
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", buildConfig()); // 4
		return new CorsFilter(source);
	}
}
