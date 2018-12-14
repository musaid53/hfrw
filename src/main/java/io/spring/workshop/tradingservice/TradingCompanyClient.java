package io.spring.workshop.tradingservice;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TradingCompanyClient {

    private final WebClient builder;

    public TradingCompanyClient(WebClient.Builder builder) {
        this.builder = builder.baseUrl("http://localhost:8082").build();
    }


    public Flux<TradingCompany> findAllCompanies(){
        return builder.get().uri("/details").retrieve().bodyToFlux(TradingCompany.class);
    }

    public Mono<TradingCompany> getTradingCompany(String ticker){
        return builder.get().uri("/details/{ticker}", ticker).retrieve()
                .bodyToMono(TradingCompany.class)
                .log()
                .switchIfEmpty(Mono.error(new TickerNotFoundException("Ticker Not Found !!!!")));
    }

}
