package com.haze;

import com.haze.core.jpa.repository.SimpleBaseRepository;
import com.jagregory.shiro.freemarker.ShiroTags;
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
@EnableJpaRepositories(repositoryBaseClass = SimpleBaseRepository.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class HazeApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(HazeApplication.class, args);
		/*try {
			Class<?> c = Class.forName("org.pentaho.di.core.database.MySQLDatabaseMeta11");
			System.out.println(c.getProtectionDomain().getCodeSource().getLocation());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}*/
		for (String beanDefinitionName : ctx.getBeanDefinitionNames()) {
			System.out.println(beanDefinitionName);
		}
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

	/*@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer() throws IOException, TemplateException {
		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
		freeMarkerConfigurer.setTemplateLoaderPath("classpath:templates/");
		freemarker.template.Configuration configuration = freeMarkerConfigurer.createConfiguration();
		configuration.setDefaultEncoding("UTF-8");
		configuration.setSharedVariable("shiro", new ShiroTags());
		freeMarkerConfigurer.setConfiguration(configuration);
		return freeMarkerConfigurer;
	}*/

	@Bean
	public Object Object(freemarker.template.Configuration configuration) {
		configuration.setSharedVariable("shiro", new ShiroTags());
		return new Object();
	}
}
