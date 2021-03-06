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
public class CallCommand extends BaseBotCommand {
    private final StateRepo stateRepo;
    private final SendMessageFactory messageFactory;

    @Autowired
    public CallCommand(StateRepo stateRepo, @Lazy SendMessageFactory messageFactory) {
        super("call", "Заказать обратный звонок.");
        this.stateRepo = stateRepo;
        this.messageFactory = messageFactory;
    }

    @Override
    @SneakyThrows
    protected void handleCommand(AbsSender absSender, User user, Chat chat, String[] arguments) {
        Long chatId = chat.getId();
        if (!stateRepo.existsByChatId(chatId)) {
            stateRepo.save(new State(chatId, user.getId(), true, false));
        }
        State state = stateRepo.findByChatId(chatId);
        state.setIsDataForCallRequested(true);
        state.setIsDataForConnectRequested(false);
        stateRepo.save(state);
        absSender.executeAsync(messageFactory.requestAdditionalDataMessage(chatId),
                new LoggingCallback<>());
    }
}
