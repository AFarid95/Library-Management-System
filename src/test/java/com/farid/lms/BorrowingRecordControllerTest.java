package com.farid.lms;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.farid.lms.entities.Book;
import com.farid.lms.entities.BorrowingRecord;
import com.farid.lms.entities.Patron;
import com.farid.lms.repositories.BookRepository;
import com.farid.lms.repositories.BorrowingRecordRepository;
import com.farid.lms.repositories.PatronRepository;

@SpringBootTest
@ActiveProfiles("test-without-cache")
@AutoConfigureMockMvc
public class BorrowingRecordControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private BookRepository bookRepository;
	
	@MockBean
	private PatronRepository patronRepository;
	
	@MockBean
	private BorrowingRecordRepository borrowingRecordRepository;
	
	private String user = "user1";
	
	private String password = "password1";
	
	@Test
	void borrowExistingBook() throws Exception {
		Book mockBook = new Book("Book 1", "Ahmed", (short) 2023, "12345");
		Patron mockPatron = new Patron("Ahmed", "Street 1", "a@b.com", "01111111111");
		
		Mockito.doReturn(Optional.of(mockBook)).when(bookRepository).findById((long) 1);
		Mockito.doReturn(Optional.of(mockPatron)).when(patronRepository).findById((long) 1);
		
		mockMvc.perform(post("/borrow/1/patron/1").with(httpBasic(user, password)))
		.andExpect(status().isOk())
		.andExpect(content().string(""));
		
		verify(bookRepository).findById((long) 1);
		verify(patronRepository).findById((long) 1);
		verify(borrowingRecordRepository).save(Mockito.any());
	}
	
	@Test
	void borrowNonExistingBook() throws Exception {
		Patron mockPatron = new Patron("Ahmed", "Street 1", "a@b.com", "01111111111");
		
		Mockito.doReturn(Optional.empty()).when(bookRepository).findById((long) 1);
		Mockito.doReturn(Optional.of(mockPatron)).when(patronRepository).findById((long) 1);
		
		mockMvc.perform(post("/borrow/1/patron/1").with(httpBasic(user, password)))
		.andExpect(status().isNotFound())
		.andExpect(content().string("Book with ID 1 not found"));
	}
	
	@Test
	void borrowByNonExistingPatron() throws Exception {
		Book mockBook = new Book("Book 1", "Ahmed", (short) 2023, "12345");
		
		Mockito.doReturn(Optional.of(mockBook)).when(bookRepository).findById((long) 1);
		Mockito.doReturn(Optional.empty()).when(patronRepository).findById((long) 1);
		
		mockMvc.perform(post("/borrow/1/patron/1").with(httpBasic(user, password)))
		.andExpect(status().isNotFound())
		.andExpect(content().string("Patron with ID 1 not found"));
	}
	
	@Test
	void returnBorrowedBook() throws Exception {
		Book mockBook = new Book("Book 1", "Ahmed", (short) 2023, "12345");
		mockBook.setId((long) 1);
		
		Patron mockPatron = new Patron("Ahmed", "Street 1", "a@b.com", "01111111111");
		mockPatron.setId((long) 1);
		
		BorrowingRecord mockBorrowingRecord = new BorrowingRecord(mockBook, mockPatron, todayDate());
		
		Mockito
		.doReturn(Optional.of(mockBorrowingRecord))
		.when(borrowingRecordRepository)
		.findById(Mockito.any());
		
		Mockito
		.doReturn(mockBorrowingRecord)
		.when(borrowingRecordRepository)
		.save(mockBorrowingRecord);
		
		mockMvc.perform(put("/return/1/patron/1").with(httpBasic(user, password)))
		.andExpect(status().isOk())
		.andExpect(content().string(""));
		
		verify(borrowingRecordRepository).findById(Mockito.any());
		verify(borrowingRecordRepository).save(mockBorrowingRecord);
	}
	
	@Test
	void returnNonBorrowedBook() throws Exception {
		Mockito
		.doReturn(Optional.empty())
		.when(borrowingRecordRepository)
		.findById(Mockito.any());
		
		mockMvc.perform(put("/return/1/patron/1").with(httpBasic(user, password)))
		.andExpect(status().isNotFound())
		.andExpect(content().string("Borrowing record with book ID 1 and patron ID 1 not found"));
		
		verify(borrowingRecordRepository).findById(Mockito.any());
	}
	
	private Date todayDate() {
		return new Date(System.currentTimeMillis());
	}
}
