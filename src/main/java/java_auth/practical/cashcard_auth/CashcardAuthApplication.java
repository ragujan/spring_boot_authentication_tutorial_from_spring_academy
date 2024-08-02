package java_auth.practical.cashcard_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class CashcardAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(CashcardAuthApplication.class, args);
	}

	@Bean
	SecurityFilterChain appSecurity(HttpSecurity http, ProblemDetailsAuthenticationEntryPoint entryPoint)
			throws Exception {
		http.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
				.oauth2ResourceServer((oauth2) -> oauth2.authenticationEntryPoint(entryPoint)
						.jwt(Customizer.withDefaults())
				);
		return http.build();
	}

}
