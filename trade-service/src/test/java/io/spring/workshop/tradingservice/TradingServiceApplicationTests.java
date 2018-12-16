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
	private  QuotesClient mockClient;
	@Test
	public void contextLoads() {
		System.out.println("Context loads");

	}

	@Test
	public void getQuotesFeed(){

		//QuotesClient mockClient = mock(QuotesClient.class);

		//Create 25 quotes
		when(mockClient.quotesFeed()).thenReturn(getRandomQutesFlux(25));
		//when(mockClient.getLatestQuote("asdasd")).thenReturn(getMono());


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

	private Flux<Quote> getRandomQutesFlux(final int count) {

		List<Quote> quoteList = new ArrayList<>(20);
		for (int i = 0; i <= count; i++) {
			Quote quote =new Quote(RandomString.generate(4), new Random().nextDouble());
			quoteList.add(quote);
			System.out.println(quote.toString());
		}
		return Flux.fromIterable(quoteList);
	}

}

