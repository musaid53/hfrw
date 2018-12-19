package io.spring.workshop.tradingservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TradingServiceApplicationTests {


	@Autowired
	private WebTestClient webTestClient;
	@MockBean
	private  QuotesClient quotesClient;

	@MockBean
	TradingCompanyClient tradingCompanyClient;

	@Test
	public void contextLoads() {
		System.out.println("Context loads");

	}

	@Test
	public void getQuotesFeed(){


		//Create 25 random quotes
		when(quotesClient.quotesFeed()).thenReturn(getRandomQuotesFlux(25));
		//when(quotesClient.getLatestQuote("asdasd")).thenReturn(getMono());


		//----/quotes/feed
		List<Quote> results = webTestClient.get()
				.uri("/quotes/feed")
				.accept(MediaType.APPLICATION_STREAM_JSON)
				.exchange()
				.expectStatus().isOk()
//				.expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON_VALUE + ";charset=UTF-8")
				.expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_STREAM_JSON)
				.returnResult(Quote.class)
				.getResponseBody()
				.take(20)
				.log(".Quote")
				.collectList()
				.block();

		assertThat(results)
				.allSatisfy(quote -> assertThat(quote.getPrice()).isNotNull());
	}

	@Test
	public void getTradingCompanySummary(){
		String ticker = "demo ticker";
		when(tradingCompanyClient.getTradingCompany(ticker)).thenReturn(Mono.just(new TradingCompany("Demo Desc", "Demo Ticker")));
		when(quotesClient.getLatestQuote(ticker)).thenReturn(Mono.just(new Quote(ticker, new Random().nextDouble())));

		TradingCompanySummary summaryMono = webTestClient.get()
				.uri("/quotes/summary/{ticker}", ticker)
				.accept(MediaType.APPLICATION_STREAM_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_STREAM_JSON)
				.returnResult(TradingCompanySummary.class)
				.getResponseBody()
				.log()
				.blockFirst();
		assertThat(summaryMono).isNotNull();
		assertThat(summaryMono.getTradingCompany()).isNotNull();
		assertThat(summaryMono.getLatestQuote()).isNotNull();

	}

	private Flux<Quote> getRandomQuotesFlux(final int count) {

		List<Quote> quoteList = new ArrayList<>(20);
		for (int i = 0; i <= count; i++) {
			Quote quote =new Quote(RandomString.generate(4), new Random().nextDouble());
			quoteList.add(quote);
			System.out.println(quote.toString());
		}
		//return Flux.fromIterable(quoteList);
		return Flux.fromIterable(quoteList).delayElements(Duration.ofSeconds(1));
	}
	private Mono<Quote> getRandomQuoteMono(){
		return Mono.just(new Quote(RandomString.generate(4), new Random().nextDouble()));
	}





}

