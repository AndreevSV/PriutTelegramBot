package omg.group.priuttelegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "telegram.bot")
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
