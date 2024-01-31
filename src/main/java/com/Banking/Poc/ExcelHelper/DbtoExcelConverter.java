package com.Banking.Poc.ExcelHelper;

import com.Banking.Poc.Entity.Accounts;
import com.Banking.Poc.Repository.UserRepo;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
@Service
public class DbtoExcelConverter {

    @Autowired
    UserRepo userrepo;
    @Value("${accounts.fields}")
    private String fieldValues;
    public List<String> getAccountfields(){
        return Arrays.asList(fieldValues.split(","));
    }

    public byte[] exportDatatoexcel() throws IOException{
        List<Accounts> data=userrepo.findAll();
        List<String> fieldNames=getAccountfields();

        try(Workbook workbook= new XSSFWorkbook()){
            Sheet sheet=workbook.createSheet("Accounts Data");
            Row headerRow=sheet.createRow(0);

            for(int colNum=0;colNum<fieldNames.size();colNum++){
                Cell cell= headerRow.createCell(colNum);
                cell.setCellValue(fieldNames.get(colNum));
            }
            int rowNum=1;
            for(Accounts entity:data){
                Row dataRow=sheet.createRow(rowNum++);
                for(int colNum=0;colNum<fieldNames.size();colNum++){
                    String fieldName=fieldNames.get(colNum);
                    try{
                        String getterMethod="get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
                        Method methodName=Accounts.class.getMethod(getterMethod);
                        Object value=methodName.invoke(entity);
                        dataRow.createCell(colNum).setCellValue(value!=null ? value.toString() : "");
                    }catch(Exception e){
                        System.out.println("NoSuchMethodException has been occured");
                        //e.printStackTrace();
                    }
                }
            }
            ByteArrayOutputStream os=new ByteArrayOutputStream();
            workbook.write(os);
            return os.toByteArray();

        }
    }
/*
    public void generateExcel(HttpServletResponse response) throws IOException {
        List<Accounts> accounts=userrepo.findAll();
        HSSFWorkbook workbook=new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Accounts Info");
        HSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("Account Id");
        row.createCell(1).setCellValue("Created Date");
        row.createCell(2).setCellValue("Name");
        row.createCell(3).setCellValue("DOB");
        row.createCell(4).setCellValue("Mobile");
        row.createCell(5).setCellValue("Account");
        row.createCell(6).setCellValue("Pan");

        int dataRowIndex=1;
        for(Accounts acc:accounts){
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(acc.getAccountId());
            dataRow.createCell(1).setCellValue(acc.getCreatedDate().toString());
            dataRow.createCell(2).setCellValue(acc.getName());
            dataRow.createCell(3).setCellValue(acc.getDob().toString());
            dataRow.createCell(4).setCellValue(acc.getMobile());
            dataRow.createCell(5).setCellValue(acc.getAccountType());
            dataRow.createCell(6).setCellValue(acc.getPan());
            dataRowIndex++;

        }
        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

 */



}
