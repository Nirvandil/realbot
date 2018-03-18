package cf.nirvandil.realbot.commands;

import cf.nirvandil.realbot.callbacks.LoggingCallback;
import cf.nirvandil.realbot.service.SendMessageFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

@Slf4j
@Component
public class BalanceCommand extends BaseBotCommand {
    private final SendMessageFactory messageFactory;

    @Autowired
    public BalanceCommand(@Lazy SendMessageFactory messageFactory) {
        super("balance", "Получить баланс лицевого счёта.");
        this.messageFactory = messageFactory;
    }

    @Override
    @SneakyThrows
    protected void handleCommand(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage message = messageFactory.messageWithText(chat.getId(),
                "Мы сходили в биллинг и получили данные о балансе, например, по номеру телефона.");
        absSender.executeAsync(message, new LoggingCallback<>());
    }
}

