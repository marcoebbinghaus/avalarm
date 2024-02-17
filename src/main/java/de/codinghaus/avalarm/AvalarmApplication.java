package de.codinghaus.avalarm;

import de.codinghaus.avalarm.config.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ConfigProperties.class)
public class AvalarmApplication {

	public static void main(String[] args) {
		SpringApplication.run(AvalarmApplication.class, args);
	}

}
