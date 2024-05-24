package com.farid.lms;

import com.farid.lms.entities.Patron;
import com.farid.lms.repositories.PatronRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test-without-cache")
@AutoConfigureMockMvc
public class PatronControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@MockBean
	private PatronRepository patronRepository;
	
	private String user = "user1";
	
	private String password = "password1";
	
	private static final MediaType APPLICATION_JSON_UTF8 = 
			new MediaType(
					MediaType.APPLICATION_JSON.getType(),
					MediaType.APPLICATION_JSON.getSubtype(),
					Charset.forName("utf8"));
	
	@Test
	void getAllPatrons() throws JsonProcessingException, Exception {
		ArrayList<Patron> mockPatrons = new ArrayList<>();
		mockPatrons.add(new Patron("Ahmed", "Street 1", "a@b.com", "01111111111"));
		mockPatrons.add(new Patron("Farid", "Street 2", "c@d.com", "01222222222"));
		
		Mockito.doReturn(mockPatrons).when(patronRepository).findAll();
		
		mockMvc.perform(get("/patrons").with(httpBasic(user, password)))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(mockPatrons)));
		
		verify(patronRepository).findAll();
	}
	
	@Test
	void getExistingPatron() throws JsonProcessingException, Exception {
		Patron mockPatron = new Patron("Ahmed", "Street 1", "a@b.com", "01111111111");
		
		Mockito.doReturn(Optional.of(mockPatron)).when(patronRepository).findById((long) 1);
		
		mockMvc.perform(get("/patrons/1").with(httpBasic("user1", "password1")))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(mockPatron)));
		
		verify(patronRepository).findById((long) 1);
	}
	
	@Test
	void getNonExistingPatron() throws Exception {
		mockMvc.perform(get("/patrons/1").with(httpBasic("user1", "password1")))
				.andExpect(status().isNotFound())
				.andExpect(content().string("Patron with ID 1 not found"));
	}
	
	@Test
	void addPatron() throws Exception {
		Patron mockPatron = new Patron("Ahmed", "Street 1", "a@b.com", "01111111111");
		mockPatron.setId((long) 1);
		
		mockMvc.perform(post("/patrons")
						.with(httpBasic("user1", "password1"))
						.contentType(APPLICATION_JSON_UTF8)
						.content(objectMapper.writeValueAsString(mockPatron)))
				.andExpect(status().isOk())
				.andExpect(content().string(""));
		
		verify(patronRepository).save(Mockito.any());
	}
	
	@Test
	void updateExistingPatron() throws Exception {
		Patron mockPatron = new Patron("Ahmed", "Street 1", "a@b.com", "01111111111");
		mockPatron.setId((long) 1);
		
		Mockito.doReturn(Optional.of(mockPatron)).when(patronRepository).findById((long) 1);
		Mockito.doReturn(mockPatron).when(patronRepository).save(mockPatron);
		
		mockMvc.perform(put("/patrons/1")
				.with(httpBasic("user1", "password1"))
				.contentType(APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(mockPatron)))
		.andExpect(status().isOk())
		.andExpect(content().string(""));
		
		verify(patronRepository).findById((long) 1);
		verify(patronRepository).save(Mockito.any());
	}
	
	@Test
	void updateNonExistingPatron() throws Exception {
		Patron mockPatron = new Patron("Ahmed", "Street 1", "a@b.com", "01111111111");
		mockPatron.setId((long) 1);
		
		Mockito.doReturn(Optional.empty()).when(patronRepository).findById((long) 1);
		Mockito.doReturn(mockPatron).when(patronRepository).save(mockPatron);
		
		mockMvc.perform(put("/patrons/1")
				.with(httpBasic("user1", "password1"))
				.contentType(APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(mockPatron)))
		.andExpect(status().isOk())
		.andExpect(content().string(""));
		
		verify(patronRepository).findById((long) 1);
		verify(patronRepository).save(Mockito.any());
	}
	
	@Test
	void deletePatron() throws Exception {
		mockMvc.perform(delete("/patrons/1").with(httpBasic("user1", "password1")))
		.andExpect(status().isOk());
		
		verify(patronRepository).deleteById((long) 1);
	}
}
