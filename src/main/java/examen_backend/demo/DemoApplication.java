package examen_backend.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"backen_examen", "examen_backend"})
@ComponentScan(basePackages = {"backen_examen", "examen_backend"})
@EnableJpaRepositories(basePackages = {"backen_examen", "examen_backend"})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
