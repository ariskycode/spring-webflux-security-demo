package co.ariskycode.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.ariskycode.model.User;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

	@GetMapping("/user")
	public Mono<User> getUser(@RequestParam String username, @RequestParam String email) {
		return Mono.just(new User(username, email));
	}
	
}
