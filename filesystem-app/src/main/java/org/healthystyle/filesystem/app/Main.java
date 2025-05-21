package org.healthystyle.filesystem.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "org.healthystyle.filesystem")
@EnableJpaRepositories(basePackages = "org.healthystyle.filesystem.repository")
@EntityScan(basePackages = "org.healthystyle.filesystem.model")
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
