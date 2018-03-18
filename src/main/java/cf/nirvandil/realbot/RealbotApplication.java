package cf.nirvandil.realbot;

import cf.nirvandil.realbot.bot.RealBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;

import java.util.List;

@SpringBootApplication
public class RealbotApplication implements CommandLineRunner {
    private final RealBot realBot;
    private final List<BotCommand> commands;

    @Autowired
    public RealbotApplication(RealBot realBot, List<BotCommand> commands) {
        this.realBot = realBot;
        this.commands = commands;
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
		SpringApplication.run(RealbotApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        TelegramBotsApi api = new TelegramBotsApi();
        commands.forEach(realBot::register);
        api.registerBot(realBot);
    }
}
