package co.ariskycode.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class GreetingsController {

	@GetMapping("/greet")
	public Mono<String> greet(@RequestParam(name = "name", defaultValue = "user") String name) {
		return Mono.just("Hello, ".concat(name));
	}

	@GetMapping("/admin")
	public Mono<String> greet(Mono<Principal> principal) {
		return principal.map(Principal::getName).map(name -> String.format("Hello, %s", name));
	}

}
