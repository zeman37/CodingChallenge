# Coding challenge using REST API.

## Description

By using this application, it will be possible to:

- Import bank statement for one or several bank accounts via
CSV.
- Export bank statement for one or several bank
  accounts via CSV.
- Calculate account balance for a given
  date.

## Requirements

- Newest JAVA Version (at the time I'm writing this, it's Version 8 Update 341) to run application
- Maven to "build" application
- SQL based database (used MySQL database)
- Application to send HTTP REQUESTS (recommended - PostMan)
- Git (optional)

## Installation

In order to build this application, you need to download .ZIP file of code or clone Git repository.
After extracting/cloning repository, go into "CodingChallenge" folder and using PowerShell/Command prompt,
use "mvn package" to build the application. All commands in order using Git Bash are listed below:
```
git clone https://github.com/zeman37/CodingChallenge
cd CodingChallenge
mvn package
```
built application will appear in target folder:

*\CodingChallenge\target\REST-0.0.1-SNAPSHOT.jar

## Running application
To run application, open PowerShell/Command Prompt in the same folder where "REST-0.0.1-SNAPSHOT.jar"
is located. Use command:
`java -jar REST-0.0.1-SNAPSHOT.jar` to run the application.
Alternatively, you can use `java -jar REST-0.0.1-SNAPSHOT.jar --server.port=x`
where x = port which you would like to use for this application (default is 8080)

## Using Application
### Notes
- All examples will be provided with default 8080 port. If you are using different port, please pay attention.
- Date format is: "yyyy-MM-dd HH:mm:ss" for example: 2017-03-15 10:23:54
- .csv file format is: accountId,operationDate,beneficiary,comment(optional),amount,currency


#### Import bank statement for one or several bank accounts via CSV:
- Request type: **HTTP POST**
- Request link: **"/api/import"**
- HTTP Request body's form data key: **"file"**
- HTTP Request body's form data value: **file.csv**

#### Export bank statement for one or several bank accounts via CSV:
- Request type: **HTTP GET**
- Request link: **"/api/export"**
- HTTP Request query parameter for date from (optional, if not provided, minimum date "1753-01-01 00:00:00" will be used) : **fromDate**
- HTTP Request query parameter for date from (optional, if not provided, maximum date "9999-12-31 23:59:59" will be used) : **toDate**

**Example:** "http://localhost:8080/api/export?fromDate=2005-01-28 15:00:00&toDate=2022-01-28 15:00:00"

This request will export .csv file with bank statement from 2005-01-28 15:00:00 to 2022-01-28 15:00:00

#### Calculate account balance for a given date:
- Request type: **HTTP GET**
- Request link: **"/api/calculate"**
- HTTP Request query parameter for account number (mandatory): **accountNumber**
- HTTP Request query parameter for date from (optional, if not provided, minimum date "1753-01-01 00:00:00" will be used) : **fromDate**
- HTTP Request query parameter for date from (optional, if not provided, maximum date "9999-12-31 23:59:59" will be used) : **toDate**

**Example:** "http://localhost:8080/api/calculate?accountNumber=894578263130789374"

This request will return account balance of 894578263130789374 account for 1753-01-01 00:00:00 to 9999-12-31 23:59:59 period.