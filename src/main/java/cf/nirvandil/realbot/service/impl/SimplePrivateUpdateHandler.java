package cf.nirvandil.realbot.service.impl;

import cf.nirvandil.realbot.model.State;
import cf.nirvandil.realbot.model.UserData;
import cf.nirvandil.realbot.repo.StateRepo;
import cf.nirvandil.realbot.repo.UserDataRepo;
import cf.nirvandil.realbot.service.PrivateUpdateHandler;
import cf.nirvandil.realbot.service.SendMessageFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Contact;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.bots.commandbot.commands.helpCommand.HelpCommand;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class SimplePrivateUpdateHandler implements PrivateUpdateHandler {

    private final UserDataRepo userDataRepo;
    private final StateRepo stateRepo;
    private final SendMessageFactory messageFactory;
    private final List<BotCommand> botCommands;
    @Value("${bot.adminChannelId}")
    private String adminChannelId;

    @Autowired
    public SimplePrivateUpdateHandler(UserDataRepo userDataRepo,
                                      StateRepo stateRepo,
                                      SendMessageFactory messageFactory,
                                      List<BotCommand> botCommands) {
        this.userDataRepo = userDataRepo;
        this.stateRepo = stateRepo;
        this.messageFactory = messageFactory;
        this.botCommands = botCommands.stream().sorted(comparing(BotCommand::getCommandIdentifier)).collect(toList());
    }

    @Override
    public Optional<List<BotApiMethod<?>>> handlePrivateUpdate(Update update) {
        Message message = update.getMessage();
        User user = message.getFrom();
        Contact contact = message.getContact();
        Long chatId = message.getChatId();
        Integer userId = user.getId();
        String phoneNumber = null;
        if (contact != null) {
            phoneNumber = contact.getPhoneNumber();
        }
        if (phoneNumber != null && !userDataRepo.existsById(userId)) {
            log.info("Given new contact. Saving them to user database.");
            userDataRepo.save(new UserData(userId, phoneNumber));
            String helpText = HelpCommand.getHelpText(botCommands);
            SendMessage helpMessage = messageFactory.messageWithText(chatId, helpText);
            SendMessage helloMessage = messageFactory.helloMessage(chatId);
            return Optional.of(asList(helpMessage, helloMessage));
        }
        if (stateRepo.existsByChatId(chatId)) {
            State state = stateRepo.findByChatId(chatId);
            StringBuilder builder = new StringBuilder(message.getText());
            userDataRepo.findById(state.getUserId())
                    .map(UserData::getPhone)
                    .ifPresent(origPhone ->
                            builder.append("\n_Номер телефона пользователя Телеграм, отправившего это сообщение, ")
                                    .append(origPhone).append("_"));
            if (state.getIsDataForCallRequested()) {
                log.info("Found answer for call request.");
                return handleAnswer(state, builder.toString(), messageFactory::callBackNotifyMessage);
            }
            if (state.getIsDataForConnectRequested()) {
                log.info("Found answer for connect request.");
                return handleAnswer(state, builder.toString(), messageFactory::connectNotifyMessage);
            }
        }
        return Optional.empty();
    }

    private Optional<List<BotApiMethod<?>>> handleAnswer(State state, String text, Function<String, SendMessage> message) {
        state.setIsDataForCallRequested(false);
        state.setIsDataForConnectRequested(false);
        stateRepo.save(state);
        return Optional.of(asList(
                messageFactory.waitForTheAnswerMessage(state.getChatId()),
                message.apply(text)));
    }
}
