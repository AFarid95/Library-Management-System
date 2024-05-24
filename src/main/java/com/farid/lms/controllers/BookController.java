package com.farid.lms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.farid.lms.entities.Book;
import com.farid.lms.notfound.ExceptionMessageMaker;
import com.farid.lms.notfound.NotFoundException;
import com.farid.lms.repositories.CachedRepository;

import jakarta.validation.Valid;

@RestController
public class BookController {
	@Autowired
	CachedRepository repository;
	
	@GetMapping("/books")
	public Iterable<Book> getAllBooks() {
		return repository.findAllBooks();
	}
	
	@GetMapping("/books/{id}")
	Book getBookById(@PathVariable("id") Long id) {
	    return repository.findBookById(id)
	      .orElseThrow(() -> new NotFoundException(
	    		  ExceptionMessageMaker.makeExceptionMessageForEntityWithId("Book", id)));
	}
	
	@PostMapping("/books")
	void addBook(@RequestBody @Valid Book book) {
	    repository.saveBook(book);
	}
	
	@PutMapping("/books/{id}")
	void replaceBook(@RequestBody @Valid Book newBook, @PathVariable("id") Long id) {
	    repository.findBookById(id)
	      .map(book -> {
	    	  book.setTitle(newBook.getTitle());
	    	  book.setAuthor(newBook.getAuthor());
	    	  book.setPublicationYear(newBook.getPublicationYear());
	    	  book.setIsbn(newBook.getIsbn());
	    	  return repository.saveBook(book);
	      })
	      .orElseGet(() -> {
	    	  newBook.setId(id);
	    	  return repository.saveBook(newBook);
	      });
	}

	@DeleteMapping("/books/{id}")
	void deleteBook(@PathVariable("id") Long id) {
		repository.deleteBookById(id);
	}
}
