package com.coding.app.REST.Controller;

import com.coding.app.REST.Service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

//Annotating RecordController as a RestController
@RestController
public class RecordController {

    //declaring RecordService
    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }


    /* PostMapping of "/api/import" POST HTTP Request.
    We request for:
    "file" parameter which goes as an upload-able file*/
    @PostMapping("/api/import")
    public String importData(@RequestParam("file")MultipartFile file){
        return recordService.importCsv(file);
    }

    /* GetMapping of "/api/export" GET HTTP Request.
        We request for:
        "fromDate" query parameter, which is not required (if nothing is provided, default value of "1753-01-01 00:00:00" (MIN of "DATETIME" SQL Variable) will be set)
        "toDate" query parameter, which is not required (if nothing is provided, default value of "9999-12-31 23:59:59" (MAX of "DATETIME" SQL Variable) will be set)
        NOTE: Java Date and Time API, which is a Java 8 feature, is used for date variables */
    @GetMapping("/api/export")
    public void exportCSV(HttpServletResponse response,
                          @RequestParam(value="fromDate", required = false, defaultValue = "1753-01-01 00:00:00") @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime fromDate,
                          @RequestParam(value="toDate", required = false, defaultValue = "9999-12-31 23:59:59") @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime toDate) throws Exception {
        recordService.exportCsv(response, fromDate, toDate);
    }


    /* GetMapping of "/api/calculate" GET HTTP Request.
        We request for:
        "accountNumber" query parameter, which is required as a Long variable
        "fromDate" query parameter, which is not required (if nothing is provided, default value of "1753-01-01 00:00:00" (MIN of "DATETIME" SQL Variable) will be set)
        "toDate" query parameter, which is not required (if nothing is provided, default value of "9999-12-31 23:59:59" (MAX of "DATETIME" SQL Variable) will be set)
        NOTE: Java Date and Time API, which is a Java 8 feature, is used for date variables */
    @GetMapping("/api/calculate")
    public String calculate(@RequestParam(value = "accountNumber") Long accNumber,
                          @RequestParam(value="fromDate", required = false, defaultValue = "1753-01-01 00:00:00") @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime fromDate,
                          @RequestParam(value="toDate", required = false, defaultValue = "9999-12-31 23:59:59") @DateTimeFormat (pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime toDate){
       return recordService.calculateBalance(accNumber,fromDate,toDate);
    }
}