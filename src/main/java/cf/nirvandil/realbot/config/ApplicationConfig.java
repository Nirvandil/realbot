package cf.nirvandil.realbot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.bots.commandbot.commands.helpCommand.HelpCommand;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@Configuration
public class ApplicationConfig {
    @Value("${bot.realApiHost}")
    private String realApiHost;
    @Value("${bot.realApiPath}")
    private String realApiPath;
    @Value("${bot.realApiKey}")
    private String realApiKey;

    @Bean
    public DefaultBotOptions botOptions() {
        DefaultBotOptions botOptions = new DefaultBotOptions();
        botOptions.setMaxThreads(4);
        return botOptions;
    }

    @Bean
    public BotCommand help() {
        return new HelpCommand("help", "Справка обо всех командах (Вы её читаете).",
                "Показывает информацию об остальных командах.");
    }

    @Bean
    public WebClient webClient() {
        WebClient.Builder builder = WebClient.builder()
                .uriBuilderFactory(authorizedRealFactory())
                .defaultHeader(ACCEPT, APPLICATION_JSON_UTF8_VALUE);
        if (log.isDebugEnabled()) builder.filter(this::loggingFilter);
        return builder.build();
    }

    private DefaultUriBuilderFactory authorizedRealFactory() {
        return new DefaultUriBuilderFactory() {
            @Override
            public UriBuilder builder() {
                return super.builder().scheme("https").host(realApiHost).path(realApiPath)
                        .queryParam("key", realApiKey);
            }
        };
    }

    private Mono<ClientResponse> loggingFilter(ClientRequest request, ExchangeFunction next) {
        log.debug("===============Dumping client request===============");
        log.trace("Request: {}", request);
        log.debug("Request URI: {}", request.url());
        log.debug("Request method: {}", request.method());
        log.debug("Request attributes: {}", request.attributes());
        log.debug("Request headers: {}", request.headers());
        log.debug("Request cookies: {}", request.cookies());
        log.debug("Request body: {}", request.body());
        log.debug("==============End of client request dump=============");
        return next.exchange(request);
    }
}
