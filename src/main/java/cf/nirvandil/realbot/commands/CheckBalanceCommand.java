package cf.nirvandil.realbot.commands;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Component
public class CheckBalanceCommand extends BotCommand {
    public CheckBalanceCommand() {
        super("balance", "Получить баланс лицевого счёта.");
    }

    public CheckBalanceCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    @SneakyThrows
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        log.info("Executing check balance command.");
    }
}

