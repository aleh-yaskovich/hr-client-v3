package com.yaskovich.hr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DepartmentFull {

    private Long id;
    private String title;
    private Integer numberOfEmployees;
    private String avgSalary;
}
