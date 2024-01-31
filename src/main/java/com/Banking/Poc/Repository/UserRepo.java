package com.Banking.Poc.Repository;

import com.Banking.Poc.Entity.Accounts;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Accounts, Long> {

    Accounts findByPanEquals(String pan);

    List<Accounts> findBycreatedDateBetween(LocalDate startDate, LocalDate endDate);
    Optional<Accounts> findByName(String name);
    @Transactional
    void deleteByname(String name);
}
