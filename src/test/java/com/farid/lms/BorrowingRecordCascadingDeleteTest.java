package com.farid.lms;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.farid.lms.entities.Book;
import com.farid.lms.entities.Patron;
import com.farid.lms.repositories.BookRepository;
import com.farid.lms.repositories.PatronRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class BorrowingRecordCascadingDeleteTest {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private PatronRepository patronRepository;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	private String user = "user1";
	
	private String password = "password1";
	
	private static final MediaType APPLICATION_JSON_UTF8 = 
			new MediaType(
					MediaType.APPLICATION_JSON.getType(),
					MediaType.APPLICATION_JSON.getSubtype(),
					Charset.forName("utf8"));
	
	@Test
	void deleteBorrowingRecordWhenBookIsDeleted() throws Exception {
		Book mockBook = new Book("Book 1", "Ahmed", (short) 2023, "12345");
		mockBook.setId((long) 1);
		
		mockMvc.perform(post("/books")
						.with(httpBasic(user, password))
						.contentType(APPLICATION_JSON_UTF8)
						.content(objectMapper.writeValueAsString(mockBook)))
				.andExpect(status().isOk())
				.andExpect(content().string(""));
		
		Patron mockPatron = new Patron("Ahmed", "Street 1", "a@b.com", "01111111111");
		mockPatron.setId((long) 1);
		
		mockMvc.perform(post("/patrons")
						.with(httpBasic(user, password))
						.contentType(APPLICATION_JSON_UTF8)
						.content(objectMapper.writeValueAsString(mockPatron)))
				.andExpect(status().isOk())
				.andExpect(content().string(""));
		
		Long borrowedBookId = ((ArrayList<Book>) bookRepository.findAll()).get(0).getId();
		Long borrowingPatronId = ((ArrayList<Patron>) patronRepository.findAll()).get(0).getId();
		
		mockMvc.perform(
					post("/borrow/" + borrowedBookId + "/patron/" + borrowingPatronId)
					.with(httpBasic(user, password)))
				.andExpect(status().isOk())
				.andExpect(content().string(""));
		
		mockMvc.perform(delete("/books/1").with(httpBasic("user1", "password1")))
		.andExpect(status().isOk());
		
		mockMvc.perform(put("/return/1/patron/1").with(httpBasic(user, password)))
		.andExpect(status().isNotFound())
		.andExpect(content().string("Borrowing record with book ID 1 and patron ID 1 not found"));
		
		mockMvc.perform(delete("/patrons/1").with(httpBasic("user1", "password1")))
		.andExpect(status().isOk());
	}
	

	@Test
	void deleteBorrowingRecordWhenPatronIsDeleted() throws Exception {
		Book mockBook = new Book("Book 1", "Ahmed", (short) 2023, "12345");
		mockBook.setId((long) 1);
		
		mockMvc.perform(post("/books")
						.with(httpBasic(user, password))
						.contentType(APPLICATION_JSON_UTF8)
						.content(objectMapper.writeValueAsString(mockBook)))
				.andExpect(status().isOk())
				.andExpect(content().string(""));
		
		Patron mockPatron = new Patron("Ahmed", "Street 1", "a@b.com", "01111111111");
		mockPatron.setId((long) 1);
		
		mockMvc.perform(post("/patrons")
						.with(httpBasic(user, password))
						.contentType(APPLICATION_JSON_UTF8)
						.content(objectMapper.writeValueAsString(mockPatron)))
				.andExpect(status().isOk())
				.andExpect(content().string(""));
		
		Long borrowedBookId = ((ArrayList<Book>) bookRepository.findAll()).get(0).getId();
		Long borrowingPatronId = ((ArrayList<Patron>) patronRepository.findAll()).get(0).getId();
		
		mockMvc.perform(
					post("/borrow/" + borrowedBookId + "/patron/" + borrowingPatronId)
					.with(httpBasic(user, password)))
				.andExpect(status().isOk())
				.andExpect(content().string(""));
		
		mockMvc.perform(delete("/patrons/1").with(httpBasic("user1", "password1")))
		.andExpect(status().isOk());
		
		mockMvc.perform(put("/return/1/patron/1").with(httpBasic(user, password)))
		.andExpect(status().isNotFound())
		.andExpect(content().string("Borrowing record with book ID 1 and patron ID 1 not found"));
		
		mockMvc.perform(delete("/books/1").with(httpBasic("user1", "password1")))
		.andExpect(status().isOk());
	}
}
