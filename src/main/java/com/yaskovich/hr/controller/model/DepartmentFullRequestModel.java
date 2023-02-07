package com.yaskovich.hr.controller.model;

import com.yaskovich.hr.entity.DepartmentFull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentFullRequestModel extends BaseModel {

    private DepartmentFull departmentFull;
}
