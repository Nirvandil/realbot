package cf.nirvandil.realbot.service.impl;

import cf.nirvandil.realbot.commands.BaseBotCommand;
import cf.nirvandil.realbot.service.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;

@Service
public class KeyboardFactoryImpl implements KeyboardFactory {
    private KeyboardRow buttons = new KeyboardRow();

    @Autowired
    public KeyboardFactoryImpl(List<BaseBotCommand> botCommands) {
        botCommands.stream()
                .filter(BaseBotCommand::isPublic)
                .sorted(comparing(BotCommand::getCommandIdentifier))
                .map(command -> new KeyboardButton().setText("/" + command.getCommandIdentifier()))
                .forEachOrdered(buttons::add);
        this.buttons.add(new KeyboardButton().setText("/help"));
    }

    @Override
    public ReplyKeyboardMarkup createAllActionsKeyboard() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup().setResizeKeyboard(true);
        keyboard.setKeyboard(singletonList(this.buttons));
        return keyboard;
    }

    @Override
    public ReplyKeyboardMarkup createRequestContactKeyboard() {
        ReplyKeyboardMarkup kbForContacts = new ReplyKeyboardMarkup()
                .setOneTimeKeyboard(true)
                .setSelective(true)
                .setResizeKeyboard(true);
        KeyboardRow keyboardButtons = new KeyboardRow();
        keyboardButtons.add(new KeyboardButton("Отправьте свои контактные данные").setRequestContact(true));
        kbForContacts.setKeyboard(singletonList(keyboardButtons));
        return kbForContacts;
    }
}
