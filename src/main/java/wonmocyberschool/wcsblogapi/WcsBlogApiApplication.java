package wonmocyberschool.wcsblogapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import wonmocyberschool.wcsblogapi.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class WcsBlogApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WcsBlogApiApplication.class, args);
	}

}
