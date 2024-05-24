package com.farid.lms.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.farid.lms.entities.Book;
import com.farid.lms.entities.Patron;

@Component
public class CachedRepository {
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private PatronRepository patronRepository;

	@Cacheable("books")
	public Iterable<Book> findAllBooks() {
		return bookRepository.findAll();
	}

	@Cacheable("book")
	public Optional<Book> findBookById(Long id) {
		return bookRepository.findById(id);
	}
	
	@Caching(
			evict = {
						@CacheEvict(cacheNames = "books", allEntries = true),
						@CacheEvict(cacheNames = "book", key = "#p0.id")
					}
			)
	public Book saveBook(Book book) {
		return bookRepository.save(book);
	}
	
	@Caching(
			evict = {
						@CacheEvict(cacheNames = "books", allEntries = true),
						@CacheEvict(cacheNames = "book", key = "#p0")
					}
			)
	public void deleteBookById(Long id) {
		bookRepository.deleteById(id);
	}

	@Cacheable("patrons")
	public Iterable<Patron> findAllPatrons() {
		return patronRepository.findAll();
	}

	@Cacheable("patron")
	public Optional<Patron> findPatronById(Long id) {
		return patronRepository.findById(id);
	}
	
	@Caching(
			evict = {
						@CacheEvict(cacheNames = "patrons", allEntries = true),
						@CacheEvict(cacheNames = "patron", key = "#p0.id")
					}
			)
	public Patron savePatron(Patron patron) {
		return patronRepository.save(patron);
	}
	
	@Caching(
			evict = {
						@CacheEvict(cacheNames = "patrons", allEntries = true),
						@CacheEvict(cacheNames = "patron", key = "#p0")
					}
			)
	public void deletePatronById(Long id) {
		patronRepository.deleteById(id);
	}
}
