package cf.nirvandil.realbot.service;

import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Update;

import java.util.List;
import java.util.Optional;

public interface GroupUpdateHandler {
    Optional<List<BotApiMethod<?>>> handleGroupUpdate(Update update);
}
