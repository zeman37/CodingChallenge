package com.coding.app.REST.Controller;

import com.coding.app.REST.Models.Record;
import com.coding.app.REST.Models.RecordRepo;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
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
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while processing CSV file";
        }
        service.saveAll(records);
        return "Upload Successful";
    }


    @GetMapping("/export")
    public void exportCSV(HttpServletResponse response,
                          @RequestParam(value="fromDate", required = false) @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime fromDate,
                          @RequestParam(value="toDate", required = false) @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime toDate) throws Exception {
        String filename = "monetary.csv";

        if(fromDate == null){
            fromDate = LocalDateTime.of(1753,1,1,0,0,0);
        }
        if(toDate == null){
            toDate = LocalDateTime.of(9999,12,31,23,59,59);
        }

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
}