package cf.nirvandil.realbot.bot;

import cf.nirvandil.realbot.callbacks.LoggingCallback;
import cf.nirvandil.realbot.service.Censurer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.commandbot.TelegramLongPollingCommandBot;

@Slf4j
@Service
public class RealBot extends TelegramLongPollingCommandBot {
    private final Censurer censurer;
    @Value("${bot.token}")
    private String token;

    public RealBot(DefaultBotOptions options, @Value("${bot.name}") String botUsername,
                   Censurer censurer) {
        super(options, true, botUsername);
        this.censurer = censurer;
    }

    @Override
    @SneakyThrows
    public void processNonCommandUpdate(Update update) {
        log.debug("Received non command update.");
        log.trace("Update body is: {}", update);
        if (update.hasMessage() && update.getMessage().isGroupMessage()) {
            Message message = update.getMessage();
            if (censurer.hasBadWords(message)) {
                log.warn("Censuring bad words!");
                Long chatId = message.getChatId();
                User user = message.getFrom();
                String nicName = user.getUserName();
                String firstName = user.getFirstName();
                String lastName = user.getLastName();
                DeleteMessage deleteMessage = new DeleteMessage(chatId, message.getMessageId());
                sendApiMethodAsync(deleteMessage, new LoggingCallback<>());
                String clearedText = "Цензурированное сообщение от @" + nicName +
                        "(" + firstName + " " + lastName + "):\n " + censurer.clearMessage(message);
                sendApiMethodAsync(new SendMessage(chatId, clearedText), new LoggingCallback<>());
            }
        }
    }

    @Override
    public String getBotToken() {
        return token;
    }


}
