package cf.nirvandil.realbot.commands;

import cf.nirvandil.realbot.callbacks.LoggingCallback;
import cf.nirvandil.realbot.service.SendMessageFactory;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

@Component
public class BotLinkCommand extends BaseBotCommand {
    private final SendMessageFactory messageFactory;
    @Autowired
    public BotLinkCommand (@Lazy SendMessageFactory messageFactory) {
        super("bot", "Получить ссылку на личку с ботом.");
        this.isPublic = false;
        this.messageFactory = messageFactory;
    }

    @Override
    @SneakyThrows
    protected void handleCommand(AbsSender absSender, User user, Chat chat, String[] arguments) {
        absSender.executeAsync(messageFactory.botLinkMessage(chat.getId()),
                new LoggingCallback<>());
    }
}
