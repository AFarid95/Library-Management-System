package com.farid.lms.entities;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Patron {
	private Long id;
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String address;
	
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	private String phone;
	
	private Set<BorrowingRecord> borrowingRecords;

	protected Patron() {}

	public Patron(String name, String address, String email, String phone) {
		this.name = name;
		this.address = address;
		this.email = email;
		this.phone = phone;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@OneToMany(cascade=CascadeType.REMOVE, mappedBy="patron")
	public Set<BorrowingRecord> getBorrowingRecords() {
		return borrowingRecords;
	}

	public void setBorrowingRecords(Set<BorrowingRecord> borrowingRecords) {
		this.borrowingRecords = borrowingRecords;
	}

	@Override
	public String toString() {
		return "name = \"" + name +
				"\", address = " + address +
				"\", email = " + email +
				"\", phone = \"" + phone + "\"";
	}
}
