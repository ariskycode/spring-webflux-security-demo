package co.ariskycode.controller;

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

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class GreetingsControllerTest {

	@Autowired
	WebTestClient webTestClient;
	
	
	@DisplayName("Should greet the user")
	@ParameterizedTest(name = "having name as {0} with Hello, {0}")
	@ValueSource(strings = {"test", "john", "clint"})
	@WithMockUser
	public void greetTestWithValidResponse(String name) {
		Flux<String> result = webTestClient.get()
		.uri(uriBuilder -> uriBuilder.path("/greet").queryParam("name", name).build())
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()		
		.returnResult(String.class)		
		.getResponseBody();
		
		StepVerifier.create(result)
		.expectSubscription()
		.expectNext("Hello, ".concat(name))
		.verifyComplete();
	}
	
	@DisplayName("Should greet the user with Hello, user")	
	@WithMockUser
	@Test
	public void greetTestWithNoParamsReturnsValidResponse() {
		 Flux<String> result = webTestClient.get()
		.uri("/greet")
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.returnResult(String.class)		
		.getResponseBody();
		
		
		 StepVerifier.create(result) 
		 .expectSubscription() 
		 .expectNext("Hello, user")
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
