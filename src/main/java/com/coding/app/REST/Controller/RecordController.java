package com.coding.app.REST.Controller;

import com.coding.app.REST.Models.Record;
import com.coding.app.REST.Models.RecordRepo;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class RecordController {

    @Autowired
    RecordRepo service;

    @PostMapping("/import")
    public String importData(@RequestParam("file")MultipartFile file){
        List<Record> records;
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<Record> csvReader = new CsvToBeanBuilder(reader).withType(Record.class).withSeparator(',').withIgnoreLeadingWhiteSpace(true).build();
            records = csvReader.parse();
            service.saveAll(records);
            return "Upload Successful";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error while processing CSV file";
        } catch (DataIntegrityViolationException e){
            e.printStackTrace();
            return "Cannot insert records into database. Check .csv formatting, it should be: accountNumber,operationDate,beneficiary,comment(optional, leave blank if not needed),amount,currency";
        }
    }


    @GetMapping("/export")
    public void exportCSV(HttpServletResponse response,
                          @RequestParam(value="fromDate", required = false) @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime fromDate,
                          @RequestParam(value="toDate", required = false) @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime toDate) throws Exception {
        String filename = "monetary.csv";

        fromDate = checkDate(fromDate,false);
        toDate = checkDate(toDate, true);

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"" + " From: " + fromDate + " To: " + toDate);

        StatefulBeanToCsv<Record> writer = new StatefulBeanToCsvBuilder<Record>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        writer.write(service.findRecordsByDateBetween(fromDate, toDate));
    }


    @GetMapping("/calculate")
    public String calculate(@RequestParam(value = "accountNumber") Long accNumber,
                          @RequestParam(value="fromDate", required = false) @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime fromDate,
                          @RequestParam(value="toDate", required = false) @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime toDate){

        fromDate = checkDate(fromDate,false);
        toDate = checkDate(toDate, true);

        try{
            return "Account balance of " + accNumber + " account number from date: " + fromDate + " to: " + toDate + " is: " + service.sumCreditAmount(accNumber,fromDate,toDate);
        } catch (Exception e){
            return "Account with " + accNumber + " did not have a balance from " + fromDate + " to: " + toDate;
        }

    }

    private LocalDateTime checkDate(LocalDateTime dateToCheck, boolean to){
        if(dateToCheck == null){
            if(to){
                dateToCheck = LocalDateTime.of(9999,12,31,23,59,59);
            } else{
                dateToCheck = LocalDateTime.of(1753,1,1,0,0,0);
            }
        }
        return dateToCheck;
    }

}