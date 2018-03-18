package cf.nirvandil.realbot.commands;

import cf.nirvandil.realbot.callbacks.LoggingCallback;
import cf.nirvandil.realbot.repo.UserDataRepo;
import cf.nirvandil.realbot.service.SendMessageFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;

@Slf4j
@Component
public class StartCommand extends BaseBotCommand {
    private final UserDataRepo userDataRepo;
    private final SendMessageFactory messageFactory;
    private final BotCommand help;

    @Autowired
    public StartCommand(UserDataRepo userDataRepo, @Lazy SendMessageFactory messageFactory, BotCommand help) {
        super("start", "Начать общение с ботом.");
        this.userDataRepo = userDataRepo;
        this.messageFactory = messageFactory;
        this.help = help;
        this.isPublic = false;
    }

    @Override
    @SneakyThrows
    protected void handleCommand(AbsSender absSender, User user, Chat chat, String[] arguments) {
        Integer id = user.getId();
        if (!userDataRepo.existsById(id)) {
            log.info("Requesting contacts from user {}.", id);
            absSender.executeAsync(messageFactory.requestIdentityMessage(chat.getId()), new LoggingCallback<>());
        } else {
            help.execute(absSender, user, chat, arguments);
            absSender.executeAsync(messageFactory.helloMessage(chat.getId()), new LoggingCallback<>());
        }
    }
}
