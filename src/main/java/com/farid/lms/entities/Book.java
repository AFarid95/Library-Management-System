package com.farid.lms.entities;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Book {
	private Long id;
	
	@NotBlank
	private String title;
	
	@NotBlank
	private String author;
	
	@NotNull
	private short publicationYear;
	
	@NotBlank
	private String isbn;
	
	private Set<BorrowingRecord> borrowingRecords;

	protected Book() {}

	public Book(String title, String author, short publicationYear, String isbn) {
		this.title = title;
		this.author = author;
		this.publicationYear = publicationYear;
		this.isbn = isbn;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public short getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(short publicationYear) {
		this.publicationYear = publicationYear;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	@OneToMany(cascade=CascadeType.REMOVE, mappedBy="book")
	public Set<BorrowingRecord> getBorrowingRecords() {
		return borrowingRecords;
	}

	public void setBorrowingRecords(Set<BorrowingRecord> borrowingRecords) {
		this.borrowingRecords = borrowingRecords;
	}

	@Override
	public String toString() {
		return "title = \"" + title +
				"\", author = \"" + author +
				"\", publicationYear = " + publicationYear +
				", isbn = \"" + isbn + "\"";
	}
}
