package co.ariskycode.controller;

import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import co.ariskycode.model.Greet;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


@WebFluxTest
@AutoConfigureRestDocs
public class GreetingsControllerTest {

	@Autowired
	WebTestClient webTestClient;
	
	
	@DisplayName("Should greet the user")
	@ParameterizedTest(name = "having name as {0} with message {0}")
	@ValueSource(strings = {"test", "john", "clint"})
	@WithMockUser
	public void greetTestWithValidResponse(String name) {
		Flux<Greet> result = webTestClient.get()
		.uri(uriBuilder -> uriBuilder.path("/greet").queryParam("name", name).build())
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()		
		.returnResult(Greet.class)		
		.getResponseBody();						
		
		StepVerifier.create(result)
		.expectSubscription()		
		.expectNext(new Greet(name))
		.verifyComplete();
	}
	
	@DisplayName("Should greet the user with message user")	
	@WithMockUser
	@Test
	public void greetTestWithNoParamsReturnsValidResponse() {
		 Flux<Greet> result = webTestClient.get()
		.uri("/greet")
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.returnResult(Greet.class)		
		.getResponseBody();
		
		 StepVerifier.create(result) 
		 .expectSubscription() 
		 .expectNext(new Greet("user"))
		 .verifyComplete();
		 
	}
	

	@WithMockUser
	@Test
	public void greetTestWithNoParamsShouldReturnBody() {
		webTestClient.get()
		.uri("/greet")
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectBody()
		.consumeWith(document("greet"));
		
	}
	
	@WithMockUser
	@Test
	public void greetTestWithValidParameterReturnsResponse() {
		webTestClient.get()
		.uri(uriBuilder -> uriBuilder.path("/greet").queryParam("name", "John").build())
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()		
		.expectBody()
		.consumeWith(document("greet-with-params"));
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
