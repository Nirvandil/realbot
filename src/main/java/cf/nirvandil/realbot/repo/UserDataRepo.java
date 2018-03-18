package cf.nirvandil.realbot.repo;

import cf.nirvandil.realbot.model.UserData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDataRepo extends MongoRepository<UserData, Integer> {
}
