package io.spring.workshop.tradingservice;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class QuotesController {

    private final QuotesClient quotesClient;
    private final TradingCompanyClient tradingCompanyClient;

    public QuotesController(QuotesClient quotesClient, TradingCompanyClient tradingCompanyClient) {
        this.quotesClient = quotesClient;
        this.tradingCompanyClient = tradingCompanyClient;
    }

    @GetMapping(value = "/quotes/feed",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Quote> getQuotesFeed(){
        return quotesClient.quotesFeed();
    }

    @GetMapping(value = "/quotes/summary/{ticker}",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<TradingCompanySummary> getTradingCompanySummary(@PathVariable(value = "ticker") String ticker){
         return tradingCompanyClient.getTradingCompany(ticker)
                 .zipWith(
                         quotesClient.getLatestQuote(ticker),
                         (tradingCompany, quote) -> {
                             TradingCompanySummary tradingCompanySummary = new TradingCompanySummary(tradingCompany,quote);
                             return  tradingCompanySummary;
                         });
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TickerNotFoundException.class)
    @ResponseBody
    public String exceptionHandler(ServerWebExchange request, Exception ex){
        return "URL:" +request.getRequest().getURI() + "\n" + "ex:" + ex.getLocalizedMessage();
    }

}
