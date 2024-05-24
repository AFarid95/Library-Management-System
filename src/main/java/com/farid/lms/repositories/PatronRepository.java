package com.farid.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.farid.lms.entities.Patron;

public interface PatronRepository extends CrudRepository<Patron, Long> {
}
