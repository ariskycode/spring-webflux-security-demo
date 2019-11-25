package co.ariskycode.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.ariskycode.model.Greet;
import reactor.core.publisher.Mono;

@RestController
public class GreetingsController {

	@GetMapping("/greet")
	public Mono<Greet> greet(@RequestParam(name = "name", defaultValue = "user") String name) {
		return Mono.just(new Greet(name));
	}

	@GetMapping("/admin")
	public Mono<Greet> greet(Mono<Principal> principal) {
		return principal.map(Principal::getName).map(Greet::new);
	}

}
