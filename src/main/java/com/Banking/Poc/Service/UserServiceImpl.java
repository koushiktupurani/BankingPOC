package com.Banking.Poc.Service;

import com.Banking.Poc.ControllerAdvice.AccBetweenDatesEx;
import com.Banking.Poc.ControllerAdvice.AccNotFoundEx;
import com.Banking.Poc.ControllerAdvice.DbEmptyEx;
import com.Banking.Poc.Entity.Accounts;
import com.Banking.Poc.ExcelHelper.ExceltoDbConverter;
import com.Banking.Poc.Repository.UserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;



@Service
public class UserServiceImpl implements UserService{
    private static final Logger logger= LogManager.getLogger(UserService.class);
    @Autowired
    UserRepo userrepo;


    @Override
    public boolean validateAge(LocalDate dob) {
        LocalDate now=LocalDate.now();
        int age= Period.between(dob, now).getYears();
        if(age>18 && age<60) {
            return true;
        }else {
            logger.info("Age Validation has been failed! Age must be greater than 18 years or less than 60 years!");
            return false;
        }
    }

    @Override
    public List<Accounts> findAccountsBetween(LocalDate startDate, LocalDate endDate) {
        logger.info("Trying to find accounts created between the given dates");
        List<Accounts> accounts=userrepo.findBycreatedDateBetween(startDate, endDate);
        if(accounts.isEmpty()) {
            throw new AccBetweenDatesEx("UserServiceImpl.java:There are no accounts created in between those dates");
        }

        return accounts;

    }


    @Override
    public void registerAnAccount(Accounts account) {
        userrepo.save(account);
    }

    @Override
    public Accounts getAnAccount(String name) {
        Optional<Accounts> optionalaccount = userrepo.findByName(name);
        return optionalaccount.orElseThrow(() -> new AccNotFoundEx("UserServiceImpl.java:ACCOUNT WITH ACCOUNT NAME: " + name + " NOT FOUND"));
    }



    @Override
    public List<Accounts> getAllAccounts() {
        if(userrepo.findAll().isEmpty()){
            throw new DbEmptyEx("UserServiceImpl.java:There are no accounts available in the bank");
        }
        logger.info("All Account Details are:");
        return userrepo.findAll();
    }



    @Override
    public String deleteAnAccount(String name) {

        if(userrepo.findByName(name).isPresent()) {
            userrepo.deleteByname(name);
            return "Account has been deleted!";
        }
        else return "ACCOUNT WITH ACCOUNT NAME: "+name+" DOES NOT EXIST";
    }

    @Override
    public boolean validatePan(String pan) {
        Accounts account =userrepo.findByPanEquals(pan);
        if(account != null) {
            logger.info("PAN Number already exists so unable to create an account.");
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void importAccountsFromExcel(List<Accounts> accounts) throws IOException {
        userrepo.saveAll(accounts);
    }


}
