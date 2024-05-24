package com.farid.lms;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void authenticatedUserTest() throws Exception {
		for (int i = 1; i <= 3; i++)
			mockMvc
			.perform(get("/api/abc").with(httpBasic("user" + i, "password" + i)))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void unauthenticatedUserTest() throws Exception {
		for (int i = 0; i < 3; i++)
			mockMvc
			.perform(get("/api/abc").with(httpBasic("user1", "invalid_password")))
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	void nonExistingUserTest() throws Exception {
		for (int i = 0; i < 3; i++)
			mockMvc
			.perform(get("/api/abc").with(httpBasic("user4", "password1")))
			.andExpect(status().isUnauthorized());
	}
}
