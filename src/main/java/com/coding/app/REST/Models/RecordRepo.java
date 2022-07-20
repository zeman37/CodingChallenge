package com.coding.app.REST.Models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecordRepo extends JpaRepository<Record, Integer> {
    List<Record> findAll();

    List<Record> findRecordsByDateBetween(LocalDateTime fromDate, LocalDateTime toDate);

    @Query(value = "SELECT SUM(s.amount) FROM monetary_operations s WHERE s.account_number = :accNumber AND s.operation_date BETWEEN :fromDate AND :toDate", nativeQuery = true)
    double sumCreditAmount(@Param("accNumber") Long accNumber, @Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);
}