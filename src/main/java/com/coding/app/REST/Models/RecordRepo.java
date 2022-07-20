package com.coding.app.REST.Models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecordRepo extends JpaRepository<Record, Integer> {
    List<Record> findAll();

    List<Record> findRecordsByDateBetween(LocalDateTime fromDate, LocalDateTime toDate);
}
