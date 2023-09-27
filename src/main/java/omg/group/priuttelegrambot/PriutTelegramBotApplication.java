package omg.group.priuttelegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PriutTelegramBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(PriutTelegramBotApplication.class, args);
    }
}
