package com.farid.lms.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;

@IdClass(BorrowingRecordKey.class)
@Entity
public class BorrowingRecord {
	@Id
	@ManyToOne
	private Book book;

	@Id
	@ManyToOne
	private Patron patron;
	
	@SuppressWarnings("unused")
	private Date borrowingDate;

	@SuppressWarnings("unused")
	private Date returnDate;

	protected BorrowingRecord() {}

	public BorrowingRecord(Book book, Patron patron, Date borrowingDate) {
		this.book = book;
		this.patron = patron;
		this.borrowingDate = borrowingDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
}
