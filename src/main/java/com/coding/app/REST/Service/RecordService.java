package com.coding.app.REST.Service;


import com.coding.app.REST.Models.Record;
import com.coding.app.REST.Models.RecordRepository;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecordService {

    //Declaring and autowiring repository
    private final RecordRepository recordRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public RecordRepository getRecordRepository() {
        return recordRepository;
    }

    public String importCsv(MultipartFile file){
        if(!file.isEmpty()){ //checking if file is attached
            List<Record> records; //make an empty list of Record type to store records information from .csv file
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) { //Trying to read information from uploaded .csv file with Reader
                CsvToBean<Record> csvReader = new CsvToBeanBuilder<Record>(reader).withType(Record.class).withSeparator(',').withIgnoreLeadingWhiteSpace(true).build(); //building CsvToBean reader of Record type with default .csv separator ',' and ignoring whitespace in front of data
                records = csvReader.parse(); //Parsing csv file and putting information to "records" list.
                recordRepository.saveAll(records); //saving "records" list information to database
                return "Upload Successful"; //Return response
            } catch (IOException e) { //catching IOException (failed to read .csv file)
                e.printStackTrace();
                return "Error while processing .csv file";
            } catch (DataIntegrityViolationException e){ //catching DataIntegrityViolationException (wrong format of .csv file)
                e.printStackTrace();
                return "Cannot insert records into database. Check .csv formatting, it should be: accountNumber,operationDate,beneficiary,comment(optional, leave blank if not needed),amount,currency"; //providing response and a .csv format
            }
        }
        return "Please attach file."; //returns response that file is not attached
    }

    public void exportCsv(HttpServletResponse response, LocalDateTime fromDate, LocalDateTime toDate) throws Exception{
        String filename = "monetary_operations.csv"; //exported file name

        response.setContentType("text/csv"); //setting HTTP GET Response content type
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"" + " From: " + fromDate + " To: " + toDate); //setting HTTP GET Response content disposition

        StatefulBeanToCsv<Record> writer = new StatefulBeanToCsvBuilder<Record>(response.getWriter()) //Creating StatefulBeanToCsv "writer" of Record type, which will use HTTP GET Response writer
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER) //setting "writer" quote constant to: "\u0000"
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR) //setting "writer" separator to default .csv file separator ','
                .withOrderedResults(false) //setting "writer" to not order results.
                .build(); //builds StatefulBeanToCsv of Record type writer with provided settings (not provided settings are filled with default values)

        writer.write(recordRepository.findRecordsByDateBetween(fromDate, toDate)); //writing Record entity information from database to .csv file and providing it to HTTP GET Response.
    }

    public String calculateBalance(Long accNumber, LocalDateTime fromDate, LocalDateTime toDate){

        try{ //If there is no exception thrown while running code inside "try" statement, return a String with account number, date period and the account balance of provided date period.
            return "Account balance of " + accNumber + " account number from date: " + fromDate + " to: " + toDate + " is: " + recordRepository.sumCreditAmount(accNumber,fromDate,toDate);
        } catch (Exception e){ //If exception is thrown in "try" statement, return a String with statement, that account number balance was not found in particular date period (no values were found).
            return "Account with " + accNumber + " did not have a balance from " + fromDate + " to: " + toDate;
        }
    }

}