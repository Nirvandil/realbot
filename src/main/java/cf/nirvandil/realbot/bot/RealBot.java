package cf.nirvandil.realbot.bot;

import cf.nirvandil.realbot.callbacks.LoggingCallback;
import cf.nirvandil.realbot.service.Dispatcher;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.commandbot.TelegramLongPollingCommandBot;

@Slf4j
@Service
public class RealBot extends TelegramLongPollingCommandBot {
    private final Dispatcher dispatcher;
    @Value("${bot.token}")
    private String token;

    @Autowired
    public RealBot(DefaultBotOptions options, @Value("${bot.name}") String botUsername, Dispatcher dispatcher) {
        super(options, true, botUsername);
        this.dispatcher = dispatcher;
    }

    @Override
    @SneakyThrows
    public void processNonCommandUpdate(Update update) {
        log.debug("Received non command update.");
        log.trace("Incoming Update body is: {}", update);
        dispatcher.dispatch(update)
                .ifPresent(botApiMethods -> botApiMethods.forEach(method ->
                        sendApiMethodAsync(method, new LoggingCallback<>())));
    }

    @Override
    public String getBotToken() {
        return token;
    }

}
