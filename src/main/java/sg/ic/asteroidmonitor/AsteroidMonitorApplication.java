package sg.ic.asteroidmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AsteroidMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsteroidMonitorApplication.class, args);
	}

}
