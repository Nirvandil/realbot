package cf.nirvandil.realbot.commands;

import cf.nirvandil.realbot.callbacks.LoggingCallback;
import cf.nirvandil.realbot.model.State;
import cf.nirvandil.realbot.repo.StateRepo;
import cf.nirvandil.realbot.service.SendMessageFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

@Slf4j
@Component
public class ConnectCommand extends BaseBotCommand {
    private final StateRepo stateRepo;
    private final SendMessageFactory messageFactory;

    @Autowired
    public ConnectCommand(StateRepo stateRepo, @Lazy SendMessageFactory messageFactory) {
        super("connect", "Оставить заявку на подключение к сети RealWeb.");
        this.stateRepo = stateRepo;
        this.messageFactory = messageFactory;
    }

    @Override
    @SneakyThrows
    protected void handleCommand(AbsSender absSender, User user, Chat chat, String[] arguments) {
        Long chatId = chat.getId();
        if (!stateRepo.existsByChatId(chatId)) {
            stateRepo.save(new State(chatId, user.getId(), false, true));
        }
        State state = stateRepo.findByChatId(chatId);
        state.setIsDataForCallRequested(false);
        state.setIsDataForConnectRequested(true);
        stateRepo.save(state);
        absSender.executeAsync(messageFactory.requestAdditionalDataMessage(chatId),
                new LoggingCallback<>());
    }
}
