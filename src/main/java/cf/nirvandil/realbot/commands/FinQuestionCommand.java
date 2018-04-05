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
public class FinQuestionCommand extends BaseBotCommand {
    private final SendMessageFactory sendMessageFactory;

    public FinQuestionCommand(@Lazy SendMessageFactory sendMessageFactory) {
        super("fin", "Получить ответ на финансовые вопросы.");
        this.sendMessageFactory = sendMessageFactory;
    }

    @Override
    @SneakyThrows
    protected void handleCommand(AbsSender absSender, User user, Chat chat, String[] arguments) {
        absSender.executeAsync(sendMessageFactory.financialQuestionMessage(chat.getId()), new LoggingCallback<>());
    }
}
