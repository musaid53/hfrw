package io.spring.workshop.tradingservice;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TradingCompanyController {
    private final TradingCompanyClient tradingCompanyClient;


    public TradingCompanyController(TradingCompanyClient tradingCompanyClient) {
        this.tradingCompanyClient = tradingCompanyClient;
    }

    @GetMapping(value = "/details", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<TradingCompany> getAllTradingCompany(){
        return tradingCompanyClient.findAllCompanies();
    }

    @GetMapping(value = "/details/{ticker}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<TradingCompany> getAllTradingCompany(@PathVariable(value = "ticker") String ticker){
        return tradingCompanyClient.getTradingCompany(ticker);
    }
}
