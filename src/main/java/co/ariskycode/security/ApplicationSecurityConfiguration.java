package co.ariskycode.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.WebFilter;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class ApplicationSecurityConfiguration {

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http.addFilterBefore(contextPathWebFilter(), SecurityWebFiltersOrder.FIRST)
		.csrf().disable()
		.authorizeExchange()
		.pathMatchers("/secure/controller/greet").hasRole("USER")
		.pathMatchers("/secure/controller/admin").hasRole("ADMIN")
		.and()
        .formLogin()
		.and()
		.httpBasic();
		return http.build();
	}
	
	@Bean
	public WebFilter contextPathWebFilter() {
	    String contextPath = "/secure/controller";
	    return (exchange, chain) -> {
	        ServerHttpRequest request = exchange.getRequest();
	        if (request.getURI().getPath().startsWith(contextPath)) {
	            return chain.filter(
	                exchange.mutate()
	                .request(request.mutate().contextPath(contextPath).build())
	                .build());
	        }
	        return chain.filter(exchange);
	    };
	}
	
	@Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User
            .withUsername("user")
            .password(passwordEncoder().encode("user"))
            .roles("USER")
            .build();

        UserDetails admin = User
            .withUsername("sa")
            .password(passwordEncoder().encode("sa"))
            .roles("ADMIN")
            .build();

        return new MapReactiveUserDetailsService(user, admin);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
}
