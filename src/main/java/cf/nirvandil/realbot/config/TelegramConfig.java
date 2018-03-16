package cf.nirvandil.realbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Configuration
public class TelegramConfig {
    @Bean
    public DefaultBotOptions botOptions() {
        DefaultBotOptions botOptions = new DefaultBotOptions();
        botOptions.setMaxThreads(4);
        return botOptions;
    }
}
