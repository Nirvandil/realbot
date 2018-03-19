package cf.nirvandil.realbot.service.impl;

import cf.nirvandil.realbot.service.Censurer;
import cf.nirvandil.realbot.service.GroupUpdateHandler;
import cf.nirvandil.realbot.service.SendMessageFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@Slf4j
@Service
public class CensorGroupUpdateHandler implements GroupUpdateHandler {
    private final Censurer censurer;
    private final SendMessageFactory messageFactory;

    @Autowired
    public CensorGroupUpdateHandler(Censurer censurer, SendMessageFactory messageFactory) {
        this.censurer = censurer;
        this.messageFactory = messageFactory;
    }

    @Override
    public Optional<List<BotApiMethod<?>>> handleGroupUpdate(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        if (message.hasText() && censurer.hasBadWords(message)) {
            log.warn("Censuring bad words!");
            String name = buildNameFor(message.getFrom());
            DeleteMessage deleteMessage = new DeleteMessage(chatId, message.getMessageId());
            String clearedText = "Цензурированное сообщение от " + name + ":\n " + censurer.clearMessage(message);
            return Optional.of(asList(deleteMessage, messageFactory.messageWithText(chatId, clearedText)));
        }
        List<User> newChatMembers = message.getNewChatMembers();
        if (newChatMembers != null) {
            return Optional.of(singletonList(messageFactory.greetingMessage(chatId)));
        }
        return Optional.empty();
    }

    private String buildNameFor(User user) {
        String nickName = user.getUserName();
        String lastName = user.getLastName();
        String firstName = user.getFirstName();
        StringBuilder builder = new StringBuilder();
        if (nickName != null) builder.append("@").append(nickName);
        if (lastName != null || firstName != null) {
            builder.append("(");
            if (firstName != null) builder.append(firstName);
            if (lastName != null) builder.append(" ").append(lastName);
            builder.append(")");
        }
        return builder.toString();
    }
}
