package com.farid.lms.entities;

public class BorrowingRecordKey {
	@SuppressWarnings("unused")
	private Long book;
	
	@SuppressWarnings("unused")
	private Long patron;

	protected BorrowingRecordKey() {}

	public BorrowingRecordKey(Long book, Long patron) {
		this.book = book;
		this.patron = patron;
	}
}
