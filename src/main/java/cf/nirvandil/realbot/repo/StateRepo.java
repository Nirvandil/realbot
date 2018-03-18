package cf.nirvandil.realbot.repo;

import cf.nirvandil.realbot.model.State;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StateRepo extends MongoRepository<State, Long> {
    boolean existsByChatId(Long chatId);

    State findByChatId(Long chatId);
}
