package co.ariskycode.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class GreetingsController {

	@GetMapping("/greet")
	public Mono<String> greet() {
		return Mono.just("Hello");
	}

	@GetMapping("/admin")
	public Mono<String> greet(Mono<Principal> principal) {
		return principal
				.map(Principal::getName)
				.map(name -> String.format("Hello, %s", name));
	}

}
