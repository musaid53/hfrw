package io.spring.workshop.stockdetails;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TradingCompanyCommandLineRunner implements CommandLineRunner {

    private final  TradingCompanyRepository tradingCompanyRepository;

    public TradingCompanyCommandLineRunner(TradingCompanyRepository tradingCompanyRepository) {
        this.tradingCompanyRepository = tradingCompanyRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        TradingCompany tradingCompany = new TradingCompany("1", "Demo", "DELL");

        tradingCompanyRepository.insert(tradingCompany).log(".TradingCompany").block();
    }
}
