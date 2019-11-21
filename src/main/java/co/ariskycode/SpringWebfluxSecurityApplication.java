package co.ariskycode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class SpringWebfluxSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxSecurityApplication.class, args);
	}

}
