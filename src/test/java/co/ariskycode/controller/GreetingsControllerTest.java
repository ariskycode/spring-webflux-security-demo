package co.ariskycode.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@WebFluxTest
public class GreetingsControllerTest {

	@Autowired
	WebTestClient webTestClient;
	
	@Test
	@WithMockUser
	public void greetTestWithValidResponse() {
		Flux<String> result = webTestClient.get().uri("/greet")
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.returnResult(String.class)
		.getResponseBody();
		
		StepVerifier.create(result)
		.expectSubscription()
		.expectNext("Hello")
		.verifyComplete();
	}
	
	@Test
	public void greetTestWithUnauthorizedResponse() {
		webTestClient.get().uri("/greet")
		.accept(MediaType.APPLICATION_JSON)
		.exchange()		
		.expectStatus().isUnauthorized();
		
	}
	
	
	@Test
	@WithAnonymousUser
	public void greetTestWithForbiddenResponse() {
		webTestClient.get().uri("/greet")
		.accept(MediaType.APPLICATION_JSON)
		.exchange()		
		.expectStatus().isForbidden();
		
	}
}
