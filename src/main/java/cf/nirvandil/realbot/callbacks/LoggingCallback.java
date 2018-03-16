package cf.nirvandil.realbot.callbacks;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updateshandlers.SentCallback;

import java.io.Serializable;

@Slf4j
public class LoggingCallback<T extends Serializable> implements SentCallback<T> {

    @Override
    public void onResult(BotApiMethod<T> method, T response) {
        log.info("Sent method was: {}, response is: {}", method.getMethod(), response);
    }

    @Override
    public void onError(BotApiMethod<T> method, TelegramApiRequestException apiException) {
        log.error("{}", apiException);
    }

    @Override
    public void onException(BotApiMethod<T> method, Exception exception) {
        log.error("{}.", exception);
    }
}
