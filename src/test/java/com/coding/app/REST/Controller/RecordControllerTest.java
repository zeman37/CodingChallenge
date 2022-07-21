package com.coding.app.REST.Controller;

import com.coding.app.REST.Service.RecordService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@SpringBootTest
class RecordControllerTest {

    @Autowired
    private RecordService recordService;

    @Test
    void importData() throws Exception {

        Path path = Paths.get("Records.csv");
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
    void exportCSV() {
    }

    @Test
    void calculate() {

    }
}