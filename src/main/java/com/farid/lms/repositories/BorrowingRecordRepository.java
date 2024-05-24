package com.farid.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.farid.lms.entities.BorrowingRecord;
import com.farid.lms.entities.BorrowingRecordKey;

public interface BorrowingRecordRepository extends CrudRepository<BorrowingRecord, BorrowingRecordKey> {
}
