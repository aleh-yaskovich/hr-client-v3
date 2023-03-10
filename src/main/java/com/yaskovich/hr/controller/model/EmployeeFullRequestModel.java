package com.yaskovich.hr.controller.model;

import com.yaskovich.hr.entity.EmployeeFull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmployeeFullRequestModel extends BaseModel {

    private EmployeeFull employeeFull;
}
