package wonmocyberschool.wcsblogapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import wonmocyberschool.wcsblogapi.config.JwtProperties;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class WcsBlogApiApplication {

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		System.out.println(new Date()+" : 한국시간");
	}

	public static void main(String[] args) {
		SpringApplication.run(WcsBlogApiApplication.class, args);
	}

}
