package energypa.bems;

import energypa.bems.awsS3.YamlPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"classpath:secretfile.yml"}, factory = YamlPropertySourceFactory.class)
public class BemsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BemsApplication.class, args);
    }

}
