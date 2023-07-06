package omg.group.priuttelegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ConfigurationProperties(prefix = "telegram.bot")
@EnableJpaRepositories(basePackages = "omg.group.priuttelegrambot.repository")
public class TelegramBotConfiguration {

    private String token;

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(this.token);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
