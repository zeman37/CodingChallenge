package com.coding.app.REST.Controller;

import com.coding.app.REST.Models.Record;
import com.coding.app.REST.Service.RecordService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

//RecordServiceTest class with @SpringBootTest annotation which marks that this is a testing class
@SpringBootTest
class RecordServiceTest {

    //Autowiring an RecordService class which will be tested
    @Autowired
    private RecordService recordService;

    //Since we are going to use SQL's MIN and MAX values of DATETIME variable, declaring their values below
    LocalDateTime fromDate = LocalDateTime.of(1753,1,1,0,0,0);
    LocalDateTime toDate = LocalDateTime.of(9999,12,31,23,59,59);


    @Test
    void shouldImportData(){

        //Obtaining information about "Records.csv" test-upload file
        Path path = Paths.get("src/test/resources/Records.csv");
        String name = "Records.csv";
        String originalFileName = "Records.csv";
        String contentType = "text/plain";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path); //reading "Records.csv" file content into byte array which will be needed when initializing MultipartFile object
        } catch (final IOException e) {
            e.printStackTrace();
        }
        MultipartFile result = new MockMultipartFile(name, originalFileName, contentType, content); //initializing MultipartFile from byte array

        String responseImport = recordService.importCsv(result); //obtaining response String from importCsv function

        Assertions.assertEquals("Upload Successful", responseImport); //if response equals to: "Upload Successful", test is passed. Otherwise, - not
    }

    @Test
    void shouldExportCSV() throws Exception {
        //Creating temporary file for tests in which will be stored export information
        File file = File.createTempFile("Test.csv","");
        FileWriter fileWriter = new FileWriter(file);

        StatefulBeanToCsv<Record> writer = new StatefulBeanToCsvBuilder<Record>(fileWriter) //Creating StatefulBeanToCsv "writer" of Record type, which will use FileWriter
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER) //setting "writer" quote constant to: "\u0000"
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR) //setting "writer" separator to default .csv file separator ','
                .withOrderedResults(false) //setting "writer" to not order results.
                .build(); //builds StatefulBeanToCsv of Record type writer with provided settings (not provided settings are filled with default values)

        writer.write(recordService.getRecordRepository().findRecordsByDateBetween(fromDate,toDate)); //writing Record entity information from database to .csv file and providing it to HTTP GET Response.

        Assertions.assertTrue(file.exists()); //checking whether file was generated. There is no need to check file contents, because if database is empty, - empty file will be generated.
    }

    @Test
    void shouldCalculate() {
        Long accountNumber = recordService.getRecordRepository().findTopByAccountNumberIsNotNull().getAccountNumber(); //getting first accountNumber from the top which is not null
        Assertions.assertNotNull(recordService.calculateBalance(accountNumber,fromDate,toDate)); //checking whether calculateBalance function returns not null. If not null is returned, - test is passed.
    }
}