package cf.nirvandil.realbot.commands;

import cf.nirvandil.realbot.callbacks.LoggingCallback;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Slf4j
@Component
@Scope(SCOPE_PROTOTYPE)
public class CallCommand extends BotCommand {
    public CallCommand() {
        super("call", "Заказать обратный звонок.");
    }

    public CallCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    @SneakyThrows
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        log.warn("Command executed! {}", getCommandIdentifier());
        absSender.executeAsync(new SendMessage(chat.getId(), "Оставьте нам свой номер телефона и адрес."),
                new LoggingCallback<>());
    }
}
