package com.yaskovich.hr.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseModel {

    private Status status;
    private String message;

    public enum Status {
        SUCCESS,
        FAILURE
    }
}
