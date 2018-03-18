package cf.nirvandil.realbot.service.impl;

import cf.nirvandil.realbot.service.Censurer;
import cf.nirvandil.realbot.service.GroupUpdateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CensorGroupUpdateHandler implements GroupUpdateHandler {
    private final Censurer censurer;

    @Autowired
    public CensorGroupUpdateHandler(Censurer censurer) {
        this.censurer = censurer;
    }

    @Override
    public Optional<List<BotApiMethod<?>>> handleGroupUpdate(Update update) {
        Message message = update.getMessage();
        if (message.hasText() && censurer.hasBadWords(message)) {
            log.warn("Censuring bad words!");
            Long chatId = message.getChatId();
            User user = message.getFrom();
            String name = buildName(user.getUserName(), user.getFirstName(), user.getLastName());
            DeleteMessage deleteMessage = new DeleteMessage(chatId, message.getMessageId());
            String clearedText = "Цензурированное сообщение от " + name + ":\n " + censurer.clearMessage(message);
            SendMessage sendMessage = new SendMessage(chatId, clearedText);
            return Optional.of(Arrays.asList(deleteMessage, sendMessage));
        }
        return Optional.empty();
    }

    private String buildName(String nickName, String firstName, String lastName) {
        StringBuilder builder = new StringBuilder();
        if (nickName != null) builder.append("@").append(nickName);
        if (lastName != null || firstName != null) {
            builder.append("(");
            if (firstName != null) builder.append(firstName).append(" ");
            if (lastName != null) builder.append(lastName);
            builder.append(")");
        }
        return builder.toString();
    }
}
