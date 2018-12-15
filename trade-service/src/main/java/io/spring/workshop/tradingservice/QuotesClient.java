package io.spring.workshop.tradingservice;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;

@Component
public class QuotesClient {

    private final WebClient builder;

    public QuotesClient(WebClient.Builder builder) {
        this.builder = builder.baseUrl("http://localhost:8081").build();
    }


    public Flux<Quote> quotesFeed(){
        return builder.get().uri("/quotes").retrieve().bodyToFlux(Quote.class);
    }

    public Mono<Quote> getLatestQuote(String ticker){
        return quotesFeed()
                .filter(quote -> quote.getTicker().equals(ticker))
                .next()
                .take(Duration.ofSeconds(15))
                .switchIfEmpty(Mono.just(new Quote(ticker, BigDecimal.ZERO)));
    }
}
