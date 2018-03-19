package cf.nirvandil.realbot.service;

import cf.nirvandil.realbot.model.web.CustomerData;
import cf.nirvandil.realbot.model.web.FindIdResponse;
import reactor.core.publisher.Mono;

public interface RealApiClient {
    Mono<FindIdResponse> findClientIdByPhone(String phone);
    Mono<CustomerData> findCustomerDataById(Integer id);
}
