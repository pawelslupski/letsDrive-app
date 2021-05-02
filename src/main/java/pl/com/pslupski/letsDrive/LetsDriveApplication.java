package pl.com.pslupski.letsDrive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class LetsDriveApplication {

	public static void main(String[] args) {
		SpringApplication.run(LetsDriveApplication.class, args);
	}

}
