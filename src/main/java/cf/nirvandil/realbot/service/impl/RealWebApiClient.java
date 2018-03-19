package cf.nirvandil.realbot.service.impl;

import cf.nirvandil.realbot.model.web.CustomerData;
import cf.nirvandil.realbot.model.web.FindIdResponse;
import cf.nirvandil.realbot.service.RealApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


@Slf4j
@Service
public class RealWebApiClient implements RealApiClient {

    private final WebClient webClient;
    private final MultiValueMap<String, String> findClientParams;

    @Autowired
    public RealWebApiClient(WebClient webClient) {
        this.webClient = webClient;
        this.findClientParams = new LinkedMultiValueMap<>();
        this.findClientParams.add("cat", "customer");
        this.findClientParams.add("subcat", "get_abon_id");
        this.findClientParams.add("data_typer", "phone");
    }

    @Override
    public Mono<FindIdResponse> findClientIdByPhone(String phone) {
        this.findClientParams.add("data_value", phone);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParams(this.findClientParams)
                        .build())
                .retrieve()
                .bodyToMono(FindIdResponse.class);
    }

    @Override
    public Mono<CustomerData> findCustomerDataById(Integer id) {
        return null;
    }

    Function<String, List<String>> splicer() {
        return string -> {
            List<String> list = new ArrayList<>();
            list.add(string);
            list.add("+" + string); // with + at start
            list.add(" " + string); // with space at start
            StringBuilder builder = new StringBuilder(string);
            builder.insert(4, ' ');
            list.add(builder.toString());
            builder = new StringBuilder(string);
            builder.insert(1, ' ').insert(5, ' ').insert(9, ' ').insert(12, ' ');
            list.add(builder.toString());
            builder = new StringBuilder("+" + string);
            builder.insert(2, ' ').insert(6, ' ').insert(10, ' ').insert(13, ' ');
            list.add(builder.toString());
            builder.delete(2, 3);
            list.add(builder.toString());
            builder = new StringBuilder(" " + string);
            builder.insert(2, ' ').insert(6, ' ').insert(10, ' ').insert(13, ' ');
            list.add(builder.toString());
            builder.delete(2, 3);
            list.add(builder.toString());
            log.debug("List of spliced number is: {}", list);
            return list;
        };
    }
}
