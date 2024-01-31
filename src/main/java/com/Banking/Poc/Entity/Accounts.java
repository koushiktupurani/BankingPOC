package com.Banking.Poc.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="accounts")

public class Accounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountId;
    @Temporal(TemporalType.DATE)
    private LocalDate createdDate=LocalDate.now();
    @NotNull
    private String name;
    @JsonFormat(pattern = "dd-MM-yyyy", shape = JsonFormat.Shape.STRING)
    private LocalDate dob;
    @NotEmpty
    @Pattern(regexp="^\\d{10}$",message="Enter valid mobile number")
    private String mobile;
    @Pattern(regexp="^(Salary|Current|Savings)$",message="Enter valid Account type( Salary|Current|Savings)")
    private String accountType;
    @Column(unique = true)
    @NotEmpty(message = " Enter valid PAN number")
    private String pan;


}
