package com.farid.lms.configurations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	private Map<String, String> passwords = new HashMap<>();
	
	public SecurityConfiguration() {
		passwords.put(
				"user1", "{bcrypt}$2a$12$7KLqe9LbrgOAAJ5JGgaj9.bLD9j9A1yOnbkBreF2DbbbqmKlUwkVy");
		passwords.put(
				"user2", "{bcrypt}$2a$12$2f0o7uXZP/h5gLVR1xJ4m.WtXP/l0/8Yeva.J5IltRX3s71bT1jCm");
		passwords.put(
				"user3", "{bcrypt}$2a$12$VOA6.0CLP57dtNYvRI1a1uoPc7El96I9ihLp5GMfhjI5/8r4kVMSa");
	}

	private String getPasswordOfUser(String user) {
		return passwords.get(user);
	}
	
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.authorizeHttpRequests((requests) -> 
			requests.anyRequest().authenticated()
		)
		.csrf((csrf) -> csrf.disable())
		.httpBasic(Customizer.withDefaults());

		return http.build();
	}

    @Bean
    UserDetailsService userDetailsService() {
    	List<UserDetails> userDetails = new ArrayList<>();
    	
    	for (String user : passwords.keySet())
    		userDetails.add(User
							.withUsername(user)
							.password(getPasswordOfUser(user))
							.build());

		return new InMemoryUserDetailsManager(userDetails);
	}
}
