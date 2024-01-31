package com.Banking.Poc.Controller;

import com.Banking.Poc.ExcelHelper.DbtoExcelConverter;
import com.Banking.Poc.ExcelHelper.ExceltoDbConverter;
import com.Banking.Poc.Service.UserService;
import com.Banking.Poc.Entity.Accounts;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("Banking")
public class UserController {
    private static final Logger logger= LogManager.getLogger(UserController.class);
    @Autowired
    UserService userservice;
    @Autowired
    DbtoExcelConverter dbtoxl;

    @Autowired
    ExceltoDbConverter xltodb;

    @PostMapping("create/account")
    public ResponseEntity<String> registerAnAccount(@RequestBody @Valid Accounts account){
        logger.info("Trying to create an account");
        if(!userservice.validateAge(account.getDob())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("AGE MUST BE GREATER THAN 18 YEARS OR LESS THAN 60 YEARS");
        }
        if (userservice.validatePan(account.getPan())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("PAN number already exists!!");
        }

        userservice.registerAnAccount(account);
        logger.info("New Account has been Created!");
        return ResponseEntity.status(HttpStatus.CREATED).body("NEW ACCOUNT HAS BEEN CREATED!");
    }

    @GetMapping("account/{name}")
    public ResponseEntity<Accounts> getaAccountbyName(@PathVariable String name){
        logger.info("Trying to fetch the account details");
        return new ResponseEntity<Accounts>(userservice.getAnAccount(name), HttpStatus.OK);

    }

    @GetMapping("accounts")
    public ResponseEntity<List<Accounts>> getallAccounts(){
        logger.info("Trying to fetch all the accounts in the bank");
        return new ResponseEntity<List<Accounts>>(userservice.getAllAccounts(),HttpStatus.OK);
    }

    @GetMapping("/accounts/between")

    public ResponseEntity<List<Accounts>> accountsBetweenDates(@RequestParam("startDate") @DateTimeFormat(pattern="yyyy-MM-dd")LocalDate startDate,
                                                                   @RequestParam("endDate")  @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate){
        List<Accounts> acc=userservice.findAccountsBetween(startDate, endDate);
        logger.info("Accounts created between start and end dates are:"+acc);
        return new ResponseEntity<List<Accounts>>(acc,HttpStatus.OK);

    }

    @DeleteMapping("delete/{name}")
    public String deleteAnAccounts(@PathVariable String name){
        logger.info("Account Details have been deleted!");
        return userservice.deleteAnAccount(name);
    }

    @GetMapping("/download/excel")
    public ResponseEntity<byte[]> downlaodexcel() throws IOException{
        byte[] excelContent=dbtoxl.exportDatatoexcel();
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment","Accounts.xls");
        return new ResponseEntity<>(excelContent,headers,HttpStatus.OK);
    }
    /*public ResponseEntity<String> generatedExcelreport(HttpServletResponse response) throws IOException{
        logger.info("Trying to download details from DataBase to Excel");
        response.setContentType("application/octet-stream");
        String headerKey="Content-Disposition";
        String headerValue="attachment;filename=Accounts.xls";
        response.setHeader(headerKey,headerValue);
        dbtoxl.generateExcel(response);
        logger.info("Details can be downloaded by pasting the URL in web browser or by downloading the file through POSTMAN!");
        return ResponseEntity.status(HttpStatus.OK).body("Account details can be downloaded by pasting the URL in web browser or by downloading the file through POSTMAN");
    }

     */


    @RequestMapping("upload/excel")
    @PostMapping(consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importAccountsFromExcel(@RequestParam("file") MultipartFile file) throws IOException {
        logger.info("Trying to add account details from excel to DataBase");
        List<Accounts> accounts = ExceltoDbConverter.exceltoListConverter(new XSSFWorkbook(file.getInputStream()));
        userservice.importAccountsFromExcel(accounts);
        logger.info("New Accounts have been added to Database");
        return ResponseEntity.status(HttpStatus.CREATED).body("NEW ACCOUNTS HAS BEEN ADDED TO DataBase FROM EXCEl!");
    }
}
