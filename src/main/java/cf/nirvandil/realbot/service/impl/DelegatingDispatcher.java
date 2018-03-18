package cf.nirvandil.realbot.service.impl;

import cf.nirvandil.realbot.service.Dispatcher;
import cf.nirvandil.realbot.service.GroupUpdateHandler;
import cf.nirvandil.realbot.service.PrivateUpdateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DelegatingDispatcher implements Dispatcher {
    private final GroupUpdateHandler groupUpdateHandler;
    private final PrivateUpdateHandler privateUpdateHandler;

    @Autowired
    public DelegatingDispatcher(GroupUpdateHandler groupUpdateHandler,
                                PrivateUpdateHandler privateUpdateHandler) {
        this.groupUpdateHandler = groupUpdateHandler;
        this.privateUpdateHandler = privateUpdateHandler;
    }

    @Override
    public Optional<List<BotApiMethod<?>>> dispatch(Update update) {
        log.info("Dispatching update.");
        log.trace("Update for dispatch is: {}", update);
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.isGroupMessage()) {
                log.info("Dispatching group message update.");
                return groupUpdateHandler.handleGroupUpdate(update);
            }
            if (message.isUserMessage()) {
                log.info("Dispatching private message update.");
                return privateUpdateHandler.handlePrivateUpdate(update);
            }
        }
        return Optional.empty();
    }

}
