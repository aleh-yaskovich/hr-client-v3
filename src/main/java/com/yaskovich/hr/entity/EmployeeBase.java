package com.yaskovich.hr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Date;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmployeeBase {
    private Long id;
    private String firstName;
    private String lastName;
    private String department;
    private String salary;
    private Date hiring = Date.valueOf(LocalDate.now());
}
