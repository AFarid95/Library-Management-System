package com.farid.lms;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.farid.lms.controllers.BookController;
import com.farid.lms.controllers.BorrowingRecordController;
import com.farid.lms.controllers.PatronController;

@SpringBootTest
class SmokeTest {
	
	@Autowired
	private BookController bookController;
	
	@Autowired
	private BorrowingRecordController borrowingRecordController;
	
	@Autowired
	private PatronController patronController;

	@Test
	void contextLoads() {
		assertThat(bookController).isNotNull();
		assertThat(borrowingRecordController).isNotNull();
		assertThat(patronController).isNotNull();
	}

}
