package com.yaskovich.hr.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentFull {

    private Long id;
    private String title;
    private Integer numberOfEmployees;
    private String avgSalary;
}
