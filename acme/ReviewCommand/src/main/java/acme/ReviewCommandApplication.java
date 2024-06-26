package acme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class, scanBasePackages = {"com.isep.acme", "acme"})
@EnableEurekaClient
public class ReviewCommandApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReviewCommandApplication.class, args);
	}
}
