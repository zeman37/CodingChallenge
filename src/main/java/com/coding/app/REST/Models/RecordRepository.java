package com.coding.app.REST.Models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

//Declaring RecordRepo interface as repository of "Record" where Integer acts as a primary key (id)
@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {

    //Function which returns list of "Record" type entities where date is in between of provided dates.
    List<Record> findRecordsByDateBetween(LocalDateTime fromDate, LocalDateTime toDate);
    //Native query which returns a double value of a specified account number balance in between of provided date
    @Query(value = "SELECT SUM(s.amount) FROM monetary_operations s WHERE s.account_number = :accNumber AND s.operation_date BETWEEN :fromDate AND :toDate", nativeQuery = true)
    double sumCreditAmount(@Param("accNumber") Long accNumber, @Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);
    Record findTopByAccountNumberIsNotNull(); //function which returns first column with not null account number (required for testing)
}