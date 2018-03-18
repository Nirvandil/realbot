package cf.nirvandil.realbot.commands;

import cf.nirvandil.realbot.callbacks.LoggingCallback;
import cf.nirvandil.realbot.repo.UserDataRepo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;

import java.util.Arrays;

import static java.util.Collections.singletonList;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Component
public class StartCommand extends BotCommand {
    @Autowired
    private UserDataRepo userDataRepo;

    public StartCommand() {
        super("start", "Начать общение с ботом.");
    }

    public StartCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    @SneakyThrows
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        Integer id = user.getId();
        if (!userDataRepo.existsById(id)) {
            log.info("Getting contacts!");
            ReplyKeyboardMarkup kbForContacts = new ReplyKeyboardMarkup()
                    .setOneTimeKeyboard(true)
                    .setSelective(true)
                    .setResizeKeyboard(true);
            KeyboardRow keyboardButtons = new KeyboardRow();
            keyboardButtons.add(new KeyboardButton("Отправьте свои контактные данные").setRequestContact(true));
            kbForContacts.setKeyboard(singletonList(keyboardButtons));
            absSender.executeAsync(new SendMessage(chat.getId(), "Нам необходимо идентифицировать вас.")
                    .setReplyMarkup(kbForContacts), new LoggingCallback<>());
        }
        else {
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup().setResizeKeyboard(true);
            KeyboardRow buttons = new KeyboardRow();
            KeyboardButton balance = new KeyboardButton().setText("/balance");
            KeyboardButton example = new KeyboardButton().setText("/call");
            KeyboardButton help = new KeyboardButton().setText("/help");
            buttons.addAll(Arrays.asList(balance, example, help));
            keyboard.setKeyboard(singletonList(buttons));
            SendMessage message = new SendMessage(chat.getId(), "Добро пожаловать. Что бы вы хотели сделать?");
            message.setReplyMarkup(keyboard);
            absSender.executeAsync(message, new LoggingCallback<>());
        }
    }
}
