package cf.nirvandil.realbot.commands;

import cf.nirvandil.realbot.callbacks.LoggingCallback;
import cf.nirvandil.realbot.model.BalanceData;
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
    }

    @Override
    @SneakyThrows
    protected void handleCommand(AbsSender absSender, User user, Chat chat, String[] arguments) {
        Long chatId = chat.getId();
        UserData userData = userDataRepo.findById(user.getId())
                .orElseThrow(RuntimeException::new);
        String phone = userData.getPhone();
        List<BalanceData> balance = jdbcTemplate.query(
                "SELECT SUBSTRING(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(u.mobile_telephone,' ',''),'+',''),')',''),'(',''),'-',''), 1, 11) AS MPhone,\n" +
                        "a.balance,a.credit, a.balance+a.credit AS SUM " +
                        "FROM accounts a, users u " +
                        "WHERE " +
                        "u.basic_account=a.ID AND a.is_deleted=0 AND a.is_blocked=0 AND a.block_id=0 AND SUBSTRING(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(u.mobile_telephone,' ',''),'+',''),'(',''),')',''),'-',''),1,11) REGEXP '^79[0-9]{9}$' AND SUBSTRING(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(u.mobile_telephone,' ',''),'+',''),'(',''),')',''),'-',''),1,11) = ?;", new Object[]{phone},
                (rs, rowNum) -> new BalanceData(rs.getDouble("balance"), rs.getDouble("credit"), rs.getDouble("SUM"))
        );
        if (!balance.isEmpty()) {
            absSender.executeAsync(messageFactory.successBalanceMessage(chatId, balance.get(0)),
                    new LoggingCallback<>());
        } else
            absSender.executeAsync(messageFactory.failAccessDataMessage(chatId), new LoggingCallback<>());
    }
}