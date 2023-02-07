package com.yaskovich.hr.controller.model;

import com.yaskovich.hr.entity.EmployeeBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBaseRequestModel extends BaseModel {

    private EmployeeBase employeeBase;
}
