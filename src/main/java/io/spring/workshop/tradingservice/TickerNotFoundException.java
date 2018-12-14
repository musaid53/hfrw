package io.spring.workshop.tradingservice;

public class TickerNotFoundException extends Exception {
    public TickerNotFoundException(String s) {
        super(s);
    }
}
