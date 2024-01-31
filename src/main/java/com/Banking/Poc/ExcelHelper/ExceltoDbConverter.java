package com.Banking.Poc.ExcelHelper;

import com.Banking.Poc.Entity.Accounts;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.time.ZoneId;
import java.util.ArrayList;

import java.util.List;

@Service
public class ExceltoDbConverter {
    /*public  boolean checkxlFormat(MultipartFile file){
        String type=file.getContentType();
        return type.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }*/

    public static List<Accounts> exceltoListConverter(Workbook workbook){
        List<Accounts> accList=new ArrayList<>();
        Sheet sheet=workbook.getSheetAt(0);
        for(Row row:sheet){
            if(row.getRowNum()==0) continue;
            Accounts account=new Accounts();
            account.setName(row.getCell(0).getStringCellValue());
            account.setDob(row.getCell(1).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            account.setMobile(row.getCell(2).getStringCellValue());
            account.setAccountType(row.getCell(3).getStringCellValue());
            account.setPan(row.getCell(4).getStringCellValue());

            accList.add(account);
        }
        return accList;


    }
}
