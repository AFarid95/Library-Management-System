package com.farid.lms.controllers;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farid.lms.entities.Book;
import com.farid.lms.entities.BorrowingRecord;
import com.farid.lms.entities.BorrowingRecordKey;
import com.farid.lms.entities.Patron;
import com.farid.lms.notfound.ExceptionMessageMaker;
import com.farid.lms.notfound.NotFoundException;
import com.farid.lms.repositories.BorrowingRecordRepository;
import com.farid.lms.repositories.CachedRepository;

@RestController
public class BorrowingRecordController {
	@Autowired
	private CachedRepository cachedRepository;
	
	@Autowired
	private BorrowingRecordRepository borrowingRecordrepository;
	
	@PostMapping("/borrow/{bookId}/patron/{patronId}")
	void borrowBook(@PathVariable("bookId") Long bookId,
								@PathVariable("patronId") Long patronId) {
		Book book = cachedRepository.findBookById(bookId)
					.orElseThrow(() -> new NotFoundException(
				    		  ExceptionMessageMaker.makeExceptionMessageForEntityWithId(
				    				  "Book", bookId)));
		Patron patron = cachedRepository.findPatronById(patronId)
						.orElseThrow(() -> new NotFoundException(
					    		  ExceptionMessageMaker.makeExceptionMessageForEntityWithId(
					    				  "Patron", patronId)));
		
		borrowingRecordrepository.save(new BorrowingRecord(book, patron, todayDate()));
	}
	
	@PutMapping("/return/{bookId}/patron/{patronId}")
	void returnBook(@PathVariable("bookId") Long bookId,
								@PathVariable("patronId") Long patronId) {
		BorrowingRecordKey key = new BorrowingRecordKey(bookId, patronId);
		
		borrowingRecordrepository.findById(key)
			.map(borrowingRecord -> {
				borrowingRecord.setReturnDate(todayDate());
				return borrowingRecordrepository.save(borrowingRecord);
			})
			.orElseThrow(() -> new NotFoundException(
					"Borrowing record with book ID "
							+ bookId
							+ " and patron ID "
							+ patronId
							+ " not found"));
	}
	
	private Date todayDate() {
		return new Date(System.currentTimeMillis());
	}
}
