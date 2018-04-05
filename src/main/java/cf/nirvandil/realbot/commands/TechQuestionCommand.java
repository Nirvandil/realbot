package cf.nirvandil.realbot.commands;

import cf.nirvandil.realbot.callbacks.LoggingCallback;
import cf.nirvandil.realbot.service.SendMessageFactory;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

@Component
public class TechQuestionCommand extends BaseBotCommand {
    private final SendMessageFactory sendMessageFactory;

    public TechQuestionCommand(@Lazy SendMessageFactory sendMessageFactory) {
        super("tech", "Получить ответы на технические вопросы: " +
                "настройка роутера, ТВ-приставки и т.д.");
        this.sendMessageFactory = sendMessageFactory;
    }

    @Override
    @SneakyThrows
    protected void handleCommand(AbsSender absSender, User user, Chat chat, String[] arguments) {
        absSender.executeAsync(sendMessageFactory.techQuestionMessage(chat.getId()), new LoggingCallback<>());
    }
}
