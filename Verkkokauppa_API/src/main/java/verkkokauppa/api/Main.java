package verkkokauppa.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        // Ladataan tietokantainfot .env tiedostosta erikseen
        Dotenv dotenv = Dotenv.configure()
                .directory("./Verkkokauppa_API/")
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(entry
                -> System.setProperty(entry.getKey(), entry.getValue())
        );

        SpringApplication.run(Main.class, args);

    }
}
