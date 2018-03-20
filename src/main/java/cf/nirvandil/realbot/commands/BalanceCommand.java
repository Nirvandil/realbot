package cf.nirvandil.realbot.commands;

import cf.nirvandil.realbot.callbacks.LoggingCallback;
import cf.nirvandil.realbot.model.UserData;
import cf.nirvandil.realbot.repo.UserDataRepo;
import cf.nirvandil.realbot.service.SendMessageFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

import java.util.List;

@Slf4j
@Component
public class BalanceCommand extends BaseBotCommand {
    private final SendMessageFactory messageFactory;
    private final UserDataRepo userDataRepo;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BalanceCommand(@Lazy SendMessageFactory messageFactory,
                          UserDataRepo userDataRepo,
                          JdbcTemplate jdbcTemplate) {
        super("balance", "Получить баланс лицевого счёта.");
        this.messageFactory = messageFactory;
        this.userDataRepo = userDataRepo;
        this.jdbcTemplate = jdbcTemplate;
        this.isPublic = false;
    }

    @Override
    @SneakyThrows
    protected void handleCommand(AbsSender absSender, User user, Chat chat, String[] arguments) {
        Long chatId = chat.getId();
        UserData userData = userDataRepo.findById(user.getId())
                .orElseThrow(RuntimeException::new);
        String phone = userData.getPhone();
        List<Double> balance = jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[]{phone},
                (rs, rowNum) -> rs.getDouble("balance")
        );
        if (!balance.isEmpty()) {
            absSender.executeAsync(messageFactory.successBalanceMessage(chatId, balance.get(0)),
                    new LoggingCallback<>());
        }
    }
}