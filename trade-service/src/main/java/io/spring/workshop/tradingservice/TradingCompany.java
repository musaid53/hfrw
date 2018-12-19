package io.spring.workshop.tradingservice;

public class TradingCompany {

    private String description;

    private String ticker;

    public TradingCompany() {
    }

    public TradingCompany(String description, String ticker) {
        this.description = description;
        this.ticker = ticker;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
}
