package verkkokauppa.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        // Ladataan tietokantainfot .env tiedostosta erikseen, koska springboot v4 + pom dotenv-java dependencyn kautta
        Dotenv dotenv = Dotenv.configure()
                .directory("./Verkkokauppa_API/") // location of .env directory
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(entry
                -> System.setProperty(entry.getKey(), entry.getValue())
        );

        SpringApplication.run(Main.class, args);

    }
}
