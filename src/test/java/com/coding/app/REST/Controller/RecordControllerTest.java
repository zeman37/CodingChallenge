package com.coding.app.REST.Controller;

import com.coding.app.REST.Models.Record;
import com.coding.app.REST.Service.RecordService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.rules.TemporaryFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
class RecordControllerTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    @Autowired
    private RecordService recordService;

    LocalDateTime fromDate = LocalDateTime.of(1753,1,1,0,0,0);
    LocalDateTime toDate = LocalDateTime.of(9999,12,31,23,59,59);

    @Test
    void importData() throws Exception {

        Path path = Paths.get("src/test/resources/Records.csv");
        String name = "Records.csv";
        String originalFileName = "Records.csv";
        String contentType = "text/plain";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        MultipartFile result = new MockMultipartFile(name, originalFileName, contentType, content);

        String responseImport = recordService.importCsv(result);

        Assertions.assertEquals("Upload Successful", responseImport);
    }

    @Test
    void exportCSV() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        // ToDo finish implementation of exportCSV test function
        File file = temporaryFolder.newFolder("src/test");
        //file = temporaryFolder.newFile("Test.csv");
        FileWriter fileWriter = new FileWriter(file);

        StatefulBeanToCsv<Record> writer = new StatefulBeanToCsvBuilder<Record>(fileWriter) //Creating StatefulBeanToCsv "writer" of Record type, which will use HTTP GET Response writer
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER) //setting "writer" quote constant to: "\u0000"
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR) //setting "writer" separator to default .csv file separator ','
                .withOrderedResults(false) //setting "writer" to not order results.
                .build(); //builds StatefulBeanToCsv of Record type writer with provided settings (not provided settings are filled with default values)

        writer.write(recordService.getRecordRepository().findRecordsByDateBetween(fromDate,toDate));

        Assertions.assertTrue(file.exists());
    }

    @Test
    void calculate() {
        Long accountNumber = recordService.getRecordRepository().findRecordByIdIs(1).getAccountNumber();
        Assertions.assertNotNull(recordService.calculateBalance(accountNumber,fromDate,toDate));
    }
}