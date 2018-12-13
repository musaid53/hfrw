package io.spring.workshop.tradingservice;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class QuotesController {

    private final QuotesClient quotesClient;

    public QuotesController(QuotesClient quotesClient) {
        this.quotesClient = quotesClient;
    }

    @GetMapping(value = "/quotes/feed",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Quote> getQuotesFeed(){
        return quotesClient.quotesFeed();
    }
}
