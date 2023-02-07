package com.yaskovich.hr.controller.model;

import com.yaskovich.hr.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequestModel extends BaseModel {

    private Department department;
}
