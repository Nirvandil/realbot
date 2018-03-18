package cf.nirvandil.realbot.service;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface KeyboardFactory {
    ReplyKeyboardMarkup createAllActionsKeyboard();

    ReplyKeyboardMarkup createRequestContactKeyboard();
}
