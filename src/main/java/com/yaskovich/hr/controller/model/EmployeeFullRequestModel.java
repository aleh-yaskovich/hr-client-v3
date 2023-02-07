package com.yaskovich.hr.controller.model;

import com.yaskovich.hr.entity.EmployeeFull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeFullRequestModel extends BaseModel {

    private EmployeeFull employeeFull;
}
