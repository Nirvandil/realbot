/* Commented out for future development.

package cf.nirvandil.realbot.commands;

import cf.nirvandil.realbot.callbacks.LoggingCallback;
import cf.nirvandil.realbot.model.UserData;
import cf.nirvandil.realbot.repo.UserDataRepo;
import cf.nirvandil.realbot.service.RealApiClient;
import cf.nirvandil.realbot.service.SendMessageFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@Slf4j
@Component
public class BalanceCommand extends BaseBotCommand {
    private final SendMessageFactory messageFactory;
    private final RealApiClient realApiClient;
    private final UserDataRepo userDataRepo;

    @Autowired
    public BalanceCommand(@Lazy SendMessageFactory messageFactory,
                          RealApiClient realApiClient,
                          UserDataRepo userDataRepo) {
        super("balance", "Получить баланс лицевого счёта.");
        this.messageFactory = messageFactory;
        this.realApiClient = realApiClient;
        this.userDataRepo = userDataRepo;
        this.isPublic = false;
    }

    @Override
    @SneakyThrows
    protected void handleCommand(AbsSender absSender, User user, Chat chat, String[] arguments) {
        Long chatId = chat.getId();
        UserData userData = userDataRepo.findById(user.getId()).orElseThrow(RuntimeException::new);
        String phone = userData.getPhone();
        realApiClient.findClientIdByPhone(phone)
                .doOnNext(findIdResponse -> {
                    log.debug("Response withId: {}", findIdResponse);
                    if (findIdResponse.isSuccess()) {
                        realApiClient.findCustomerDataById(findIdResponse.getId())
                                .doOnNext(dataResponse -> {
                                    log.debug("Response with customer data: {}", dataResponse);
                                    try {
                                        if (dataResponse.isSuccess()) {
                                            Double balance = dataResponse.getBalance();
                                            absSender.executeAsync(messageFactory.successBalanceMessage(chatId, balance),
                                                    new LoggingCallback<>());
                                        } else {
                                            log.warn("Can't get customer data.");
                                            absSender.executeAsync(messageFactory.failAccessDataMessage(chatId),
                                                    new LoggingCallback<>());
                                        }
                                    } catch (TelegramApiException e) {
                                        log.error("Error sending get balance message, exc: {}", e);
                                    }
                                })
                                .subscribe();
                    } else try {
                        absSender.executeAsync(messageFactory.failAccessDataMessage(chatId), new LoggingCallback<>());
                    } catch (TelegramApiException e) {
                        log.error("Error sending get balance message, exc: {}", e);
                    }
                })
                .subscribe();
    }
}

*/
