package cf.nirvandil.realbot.config;

import cf.nirvandil.realbot.commands.BotLinkCommand;
import cf.nirvandil.realbot.commands.CheckBalanceCommand;
import cf.nirvandil.realbot.commands.CallCommand;
import cf.nirvandil.realbot.commands.StartCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.bots.commandbot.commands.helpCommand.HelpCommand;

@Configuration
public class CommandsConfig {
    @Bean
    public BotCommand example() {
        return new CallCommand("call",
                "Заказать обратный звонок.");
    }

    @Bean
    public BotCommand help() {
        return new HelpCommand("help", "Справка",
                "Показывает информацию об остальных командах.");
    }

    @Bean
    public BotCommand checkBalance() {
        return new CheckBalanceCommand("balance",
                "Получить баланс лицевого счёта.");
    }

    @Bean
    public BotCommand start() {
        return new StartCommand();
    }

    @Bean
    public BotCommand botLink() {
        return new BotLinkCommand("bot", "Получить ссылку на личку с ботом.");
    }
}
