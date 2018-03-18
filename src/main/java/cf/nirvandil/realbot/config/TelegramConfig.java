package cf.nirvandil.realbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.bots.commandbot.commands.helpCommand.HelpCommand;

@Configuration
public class TelegramConfig {
    @Bean
    public DefaultBotOptions botOptions() {
        DefaultBotOptions botOptions = new DefaultBotOptions();
        botOptions.setMaxThreads(4);
        return botOptions;
    }

    @Bean
    public BotCommand help() {
        return new HelpCommand("help", "Справка обо всех командах (Вы её читаете).",
                "Показывает информацию об остальных командах.");
    }
}
