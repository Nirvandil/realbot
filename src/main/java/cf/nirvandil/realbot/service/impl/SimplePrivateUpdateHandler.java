package cf.nirvandil.realbot.service.impl;

import cf.nirvandil.realbot.model.UserData;
import cf.nirvandil.realbot.repo.UserDataRepo;
import cf.nirvandil.realbot.service.PrivateUpdateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendChatAction;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Contact;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SimplePrivateUpdateHandler implements PrivateUpdateHandler {

    private final UserDataRepo userDataRepo;

    @Autowired
    public SimplePrivateUpdateHandler(UserDataRepo userDataRepo) {
        this.userDataRepo = userDataRepo;
    }

    @Override
    public Optional<List<BotApiMethod<?>>> handlePrivateUpdate(Update update) {
        Message message = update.getMessage();
        User user = message.getFrom();
        Contact contact = message.getContact();
        Long chatId = message.getChatId();
        Integer id = user.getId();
        String phoneNumber = null;
        if (contact != null) {
            phoneNumber = contact.getPhoneNumber();
        }
        if (phoneNumber != null && !userDataRepo.existsById(id)) {
            log.info("Given new contacts. Saving them to user database.");
            userDataRepo.save(new UserData(id, phoneNumber));
            return Optional.of(Collections.singletonList(new SendMessage(chatId, "Данные сохранены, наберите /start " +
                    "для продолжения работы")));
        }

        return Optional.empty();
    }
}
