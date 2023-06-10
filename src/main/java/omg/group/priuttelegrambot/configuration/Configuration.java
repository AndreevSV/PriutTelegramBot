package omg.group.priuttelegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Configuration {
    @Bean
    public TelegramBot telegramBot(@Value("${Telegram.bot.token}") String token) {
        return new TelegramBot(token);
    }
}
