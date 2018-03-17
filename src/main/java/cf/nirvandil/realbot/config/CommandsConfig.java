package cf.nirvandil.realbot.config;

import cf.nirvandil.realbot.commands.CheckBalanceCommand;
import cf.nirvandil.realbot.commands.ExampleCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.bots.commandbot.commands.helpCommand.HelpCommand;

import java.util.List;

@Configuration
public class CommandsConfig {
    @Bean
    public BotCommand example() {
        return new ExampleCommand("example", "Пример описания");
    }

    @Bean
    public BotCommand help() {
        return new HelpCommand();
    }

    @Bean
    public BotCommand checkBalance() {
        return new CheckBalanceCommand("balance", "Получить баланс лицевого счёта");
    }
}
