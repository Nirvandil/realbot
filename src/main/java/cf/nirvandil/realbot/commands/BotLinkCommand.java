package cf.nirvandil.realbot.commands;

import cf.nirvandil.realbot.callbacks.LoggingCallback;
import lombok.SneakyThrows;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;

public class BotLinkCommand extends BotCommand {
    public BotLinkCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    @SneakyThrows
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        absSender.executeAsync(new SendMessage(chat.getId(), "Кликните на ссылку: @Test_Nirvandil_bot"),
                new LoggingCallback<>());
    }
}
