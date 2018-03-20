package cf.nirvandil.realbot.service.impl;

import cf.nirvandil.realbot.model.BalanceData;
import cf.nirvandil.realbot.service.KeyboardFactory;
import cf.nirvandil.realbot.service.SendMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;

@Service
public class SendMessageFactoryImpl implements SendMessageFactory {
    @Value("${bot.adminChannelId}")
    private Long adminChannelId;
    private final KeyboardFactory keyboardFactory;
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.supportLink}")
    private String supportLink;

    @Autowired
    public SendMessageFactoryImpl(KeyboardFactory keyboardFactory) {
        this.keyboardFactory = keyboardFactory;
    }

    @Override
    public SendMessage requestAdditionalDataMessage(Long chatId) {
        return new SendMessage(chatId, "Оставьте нам свой номер телефона и адрес, " +
                "мы незамедлительно свяжемся с Вами.");
    }

    @Override
    public SendMessage waitForTheAnswerMessage(Long chatId) {
        return new SendMessage(chatId,
                "Ваша заявка зарегистрирована. \n" +
                        "Пожалуйста, ожидайте звонка от нашей службы поддержки.");
    }

    @Override
    public SendMessage callBackNotifyMessage(String text) {
        return new SendMessage(adminChannelId,
                "*Поступила заявка на обратный звонок:*\n" + text)
                .enableMarkdown(true);
    }

    @Override
    public SendMessage connectNotifyMessage(String text) {
        return new SendMessage(adminChannelId,
                "*Поступила заявка на подключение:*\n" + text)
                .enableMarkdown(true);
    }

    @Override
    public SendMessage helloMessage(Long chatId) {
        return new SendMessage(chatId,
                "Добро пожаловать. Что бы вы хотели сделать?")
                .setReplyMarkup(keyboardFactory.createAllActionsKeyboard());
    }

    @Override
    public SendMessage requestIdentityMessage(Long chatId) {
        return new SendMessage(chatId, "Нам необходимо идентифицировать вас.")
                .setReplyMarkup(keyboardFactory.createRequestContactKeyboard());
    }

    @Override
    public SendMessage botLinkMessage(Long chatId) {
        return new SendMessage(chatId, "Кликните на ссылку: @" + botName);
    }

    @Override
    public SendMessage messageWithText(Long chatId, String text) {
        return new SendMessage(chatId, text).enableMarkdown(true).enableHtml(true);
    }

    @Override
    public SendMessage successBalanceMessage(Long chatId, BalanceData data) {
        return new SendMessage(chatId, "Ваш баланс (данные получены на основании номера телефона): " + data)
                .enableMarkdown(true);
    }

    @Override
    public SendMessage failAccessDataMessage(Long chatId) {
        return new SendMessage(chatId, "К сожалению, мы не смогли получить данные из нашего биллинга. " +
                "Попробуйте позже или обратитесь в нашу службу поддержки с помощью опции /call");
    }

    @Override
    public SendMessage greetingMessage(Long chatId) {
        return new SendMessage(chatId, "Приветствуем новых участников группы.\n" +
                "Вы можете заказать обратный звонок или оставить заявку на подключение к сети RealWeb с помощью " +
                "нашего бота, проследовав по ссылке @"+ botName + ".\n" +
                "Если Вы хотите пообщаться с техподдержкой напрямую, можно сделать это в выделенном чате: " + supportLink);
    }
}
