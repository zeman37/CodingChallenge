package com.coding.app.REST.Models;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import javax.persistence.*;
import java.time.*;

/*  Annotating Record class as an entity (which will be worked on). Also specifying main table for entities (in our case it's "monetary_operations")
    Later on annotating each variable as a column with its name in the table, providing variable position in csv file, declaring whether it's nullable variable or not
 */

@Entity
@Table(name = "monetary_operations")
public class Record {
    @Id //specifies that this variable will be primary id in the table
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //declaring id generation strategy
    private int id;
    @Column(name = "account_number", nullable = false)
    @CsvBindByPosition(position = 0)
    private Long accountNumber;
    @Column(name = "operation_date", nullable = false)
    @CsvDate(value = "yyyy-MM-dd HH:mm:ss") //providing date format
    @CsvBindByPosition(position = 1)
    private LocalDateTime date;
    @Column(name = "beneficiary", nullable = false)
    @CsvBindByPosition(position = 2)
    private String beneficiary;
    @Column(name = "comment")
    @CsvBindByPosition(position = 3)
    private String comment;
    @Column(name = "amount", nullable = false)
    @CsvBindByPosition(position = 4)
    private double amount;
    @Column(name = "currency", nullable = false)
    @CsvBindByPosition(position = 5)
    private String currency;

    //Generated constructors, getters and setters

    public Record(Long accountNumber, LocalDateTime date, String beneficiary, String comment, double amount, String currency) {
        this.accountNumber = accountNumber;
        this.date = date;
        this.beneficiary = beneficiary;
        this.comment = comment;
        this.amount = amount;
        this.currency = currency;
    }

    public Record(Long accountNumber, LocalDateTime date, String beneficiary, double amount, String currency) {
        this.accountNumber = accountNumber;
        this.date = date;
        this.beneficiary = beneficiary;
        this.amount = amount;
        this.currency = currency;
    }

    public Record() {
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
