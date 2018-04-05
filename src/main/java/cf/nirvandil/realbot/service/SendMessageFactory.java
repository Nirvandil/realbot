package cf.nirvandil.realbot.service;

import cf.nirvandil.realbot.model.BalanceData;
import org.telegram.telegrambots.api.methods.send.SendMessage;

public interface SendMessageFactory {
    SendMessage requestAdditionalDataMessage(Long chatId);

    SendMessage waitForTheAnswerMessage(Long chatId);

    SendMessage callBackNotifyMessage(String text);

    SendMessage connectNotifyMessage(String text);

    SendMessage helloMessage(Long chatId);

    SendMessage requestIdentityMessage(Long chatId);

    SendMessage botLinkMessage(Long chatId);

    SendMessage messageWithText(Long chatId, String text);

    SendMessage successBalanceMessage(Long chatId, BalanceData data);

    SendMessage failAccessDataMessage(Long chatId);

    SendMessage greetingMessage(Long chatId);

    SendMessage techQuestionMessage(Long chatId);

    SendMessage financialQuestionMessage(Long chatId);
}
