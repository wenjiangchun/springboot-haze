package com.haze;

import com.haze.core.jpa.repository.SimpleBaseRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableJpaRepositories(repositoryBaseClass = SimpleBaseRepository.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class TestApplication {
	public static void main(String[] args) {
		//SpringApplication.run(TestApplication.class, args);
		File f = new File("/home/sofar/a.md");
		File b = new File("/home/sofar/下载/12306.md");
	}
}
