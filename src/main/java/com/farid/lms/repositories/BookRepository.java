package com.farid.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.farid.lms.entities.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
}
