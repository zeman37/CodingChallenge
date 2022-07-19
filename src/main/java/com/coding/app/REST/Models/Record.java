package com.coding.app.REST.Models;
import javax.persistence.*;
import java.time.*;

@Entity
@Table(name = "monetary_operations")
public class Record {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "account_number")
    private Long accountNumber;
    @Column(name = "operation_date")
    private LocalDateTime date;
    @Column(name = "beneficiary")
    private String beneficiary;
    @Column(name = "comment")
    private String comment;
    @Column(name = "amount")
    private double amount;
    @Column(name = "currency")
    private String currency;

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
