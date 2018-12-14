package io.spring.workshop.tradingservice;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class QuotesClient {

    private final WebClient builder;

    public QuotesClient(WebClient.Builder builder) {
        this.builder = builder.baseUrl("http://localhost:8080").build();
    }


    public Flux<Quote> quotesFeed(){
        return builder.get().uri("/quotes").retrieve().bodyToFlux(Quote.class);
    }
}
