package cf.nirvandil.realbot.commands;

import cf.nirvandil.realbot.callbacks.LoggingCallback;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;

import static java.util.Collections.singletonList;

@Slf4j
public class CheckBalanceCommand extends BotCommand {

    public CheckBalanceCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    @SneakyThrows
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        log.info("Executing check balance command.");
        if (arguments.length > 0) {
            log.info("Getting contacts!");
            ReplyKeyboardMarkup kbForContacts = new ReplyKeyboardMarkup()
                    .setOneTimeKeyboard(true)
                    .setSelective(true)
                    .setResizeKeyboard(true);
            KeyboardRow keyboardButtons = new KeyboardRow();
            keyboardButtons.add(new KeyboardButton("Отправьте свои контактные данные").setRequestContact(true));
            kbForContacts.setKeyboard(singletonList(keyboardButtons));
            absSender.executeAsync(new SendMessage(chat.getId(), "Вы хотите проверить баланс.")
                    .setReplyMarkup(kbForContacts), new LoggingCallback<>());
        }
    }
}
