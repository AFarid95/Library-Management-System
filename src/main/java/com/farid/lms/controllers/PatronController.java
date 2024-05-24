package com.farid.lms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.farid.lms.entities.Patron;
import com.farid.lms.notfound.ExceptionMessageMaker;
import com.farid.lms.notfound.NotFoundException;
import com.farid.lms.repositories.CachedRepository;

import jakarta.validation.Valid;

@RestController
public class PatronController {
	@Autowired
	CachedRepository repository;
	
	@GetMapping("/patrons")
	public Iterable<Patron> getAllPatrons() {
		return repository.findAllPatrons();
	}
	
	@GetMapping("/patrons/{id}")
	Patron getPatronById(@PathVariable("id") Long id) {
	    return repository.findPatronById(id)
	      .orElseThrow(() -> new NotFoundException(
	    		  ExceptionMessageMaker.makeExceptionMessageForEntityWithId("Patron", id)));
	}
	
	@PostMapping("/patrons")
	void addPatron(@RequestBody @Valid Patron patron) {
	    repository.savePatron(patron);
	}
	
	@PutMapping("/patrons/{id}")
	void replacePatron(@RequestBody @Valid Patron newPatron, @PathVariable("id") Long id) {
	    repository.findPatronById(id)
	      .map(patron -> {
	    	  patron.setName(newPatron.getName());
	    	  patron.setAddress(newPatron.getAddress());
	    	  patron.setEmail(newPatron.getEmail());
	    	  patron.setPhone(newPatron.getPhone());
	    	  return repository.savePatron(patron);
	      })
	      .orElseGet(() -> {
	    	  newPatron.setId(id);
	    	  return repository.savePatron(newPatron);
	      });
	}

	@DeleteMapping("/patrons/{id}")
	void deletePatron(@PathVariable("id") Long id) {
		repository.deletePatronById(id);
	}
}
