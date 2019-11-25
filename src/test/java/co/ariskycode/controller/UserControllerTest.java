package co.ariskycode.controller;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import co.ariskycode.model.User;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@WebFluxTest
@AutoConfigureRestDocs
class UserControllerTest {

	@Autowired
	WebTestClient webTestClient;
	
	@DisplayName("Should return user")	
	@WithMockUser
	@Test
	public void testGetUser() {
		 Flux<User> result = webTestClient.get()
			.uri(uriBuilder -> uriBuilder.path("/user").queryParam("username", "john").queryParam("email", "john@email.com").build())
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.returnResult(User.class)		
			.getResponseBody();
		
		 StepVerifier.create(result) 
			 .expectSubscription() 
			 .expectNext(new User("john", "john@email.com"))
			 .verifyComplete();
	}

	@DisplayName("Document /user endpoint")	
	@WithMockUser
	@Test
	public void documentGetUser() {
		 webTestClient.get()
			.uri(uriBuilder -> uriBuilder.path("/user").queryParam("username", "john").queryParam("email", "john@email.com").build())
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.consumeWith(document("user", preprocessResponse(prettyPrint())));
	}
	
}
