package com.farid.lms;

import com.farid.lms.entities.Book;
import com.farid.lms.repositories.BookRepository;
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
public class BookControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@MockBean
	private BookRepository bookRepository;
	
	private String user = "user1";
	
	private String password = "password1";
	
	private static final MediaType APPLICATION_JSON_UTF8 = 
			new MediaType(
					MediaType.APPLICATION_JSON.getType(),
					MediaType.APPLICATION_JSON.getSubtype(),
					Charset.forName("utf8"));
	
	@Test
	void getAllBooks() throws JsonProcessingException, Exception {
		ArrayList<Book> mockBooks = new ArrayList<>();
		mockBooks.add(new Book("Book 1", "Ahmed", (short) 2023, "12345"));
		mockBooks.add(new Book("Book 2", "Farid", (short) 2024, "67890"));
		
		Mockito.doReturn(mockBooks).when(bookRepository).findAll();
		
		mockMvc.perform(get("/books").with(httpBasic(user, password)))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(mockBooks)));
		
		verify(bookRepository).findAll();
	}
	
	@Test
	void getExistingBook() throws JsonProcessingException, Exception {
		Book mockBook = new Book("Book 1", "Ahmed", (short) 2023, "12345");
		
		Mockito.doReturn(Optional.of(mockBook)).when(bookRepository).findById((long) 1);
		
		mockMvc.perform(get("/books/1").with(httpBasic("user1", "password1")))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(mockBook)));
		
		verify(bookRepository).findById((long) 1);
	}
	
	@Test
	void getNonExistingBook() throws Exception {
		mockMvc.perform(get("/books/1").with(httpBasic("user1", "password1")))
				.andExpect(status().isNotFound())
				.andExpect(content().string("Book with ID 1 not found"));
	}
	
	@Test
	void addBook() throws Exception {
		Book mockBook = new Book("Book 1", "Ahmed", (short) 2023, "12345");
		mockBook.setId((long) 1);
		
		mockMvc.perform(post("/books")
						.with(httpBasic("user1", "password1"))
						.contentType(APPLICATION_JSON_UTF8)
						.content(objectMapper.writeValueAsString(mockBook)))
				.andExpect(status().isOk())
				.andExpect(content().string(""));
		
		verify(bookRepository).save(Mockito.any());
	}
	
	@Test
	void updateExistingBook() throws Exception {
		Book mockBook = new Book("Book 1", "Ahmed", (short) 2023, "12345");
		mockBook.setId((long) 1);
		
		Mockito.doReturn(Optional.of(mockBook)).when(bookRepository).findById((long) 1);
		Mockito.doReturn(mockBook).when(bookRepository).save(mockBook);
		
		mockMvc.perform(put("/books/1")
				.with(httpBasic("user1", "password1"))
				.contentType(APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(mockBook)))
		.andExpect(status().isOk())
		.andExpect(content().string(""));
		
		verify(bookRepository).findById((long) 1);
		verify(bookRepository).save(Mockito.any());
	}
	
	@Test
	void updateNonExistingBook() throws Exception {
		Book mockBook = new Book("Book 1", "Ahmed", (short) 2023, "12345");
		mockBook.setId((long) 1);
		
		Mockito.doReturn(Optional.empty()).when(bookRepository).findById((long) 1);
		Mockito.doReturn(mockBook).when(bookRepository).save(mockBook);
		
		mockMvc.perform(put("/books/1")
				.with(httpBasic("user1", "password1"))
				.contentType(APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsString(mockBook)))
		.andExpect(status().isOk())
		.andExpect(content().string(""));
		
		verify(bookRepository).findById((long) 1);
		verify(bookRepository).save(Mockito.any());
	}
	
	@Test
	void deleteBook() throws Exception {
		mockMvc.perform(delete("/books/1").with(httpBasic("user1", "password1")))
		.andExpect(status().isOk());
		
		verify(bookRepository).deleteById((long) 1);
	}
}
