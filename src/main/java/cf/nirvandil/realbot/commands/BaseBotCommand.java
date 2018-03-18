package cf.nirvandil.realbot.commands;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;

@Slf4j
public abstract class BaseBotCommand extends BotCommand {
    @Getter
    protected boolean isPublic = true;
    public BaseBotCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public final void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        log.warn("Command executed: {}", getCommandIdentifier());
        handleCommand(absSender, user, chat, arguments);
    }

    protected abstract void handleCommand(AbsSender absSender, User user, Chat chat, String[] arguments);
}
