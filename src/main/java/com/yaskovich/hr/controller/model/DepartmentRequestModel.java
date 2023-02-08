package com.yaskovich.hr.controller.model;

import com.yaskovich.hr.entity.Department;
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
public class DepartmentRequestModel extends BaseModel {

    private Department department;
}
