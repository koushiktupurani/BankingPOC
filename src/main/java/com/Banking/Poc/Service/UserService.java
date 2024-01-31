package com.Banking.Poc.Service;

import com.Banking.Poc.Entity.Accounts;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface UserService {

    public Accounts getAnAccount(String name);
    public List<Accounts> getAllAccounts();

    public boolean validateAge(LocalDate dob);

    public List<Accounts> findAccountsBetween(LocalDate startDate, LocalDate endDate);

    public void registerAnAccount(Accounts user);

    public String deleteAnAccount(String name);


    boolean validatePan(String pan);

    public void importAccountsFromExcel(List<Accounts> accounts) throws IOException;



}
