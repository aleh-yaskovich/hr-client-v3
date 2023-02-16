package com.yaskovich.hr.service;

import com.yaskovich.hr.controller.model.BaseModel;
import com.yaskovich.hr.controller.model.DepartmentRequestModel;
import com.yaskovich.hr.entity.DepartmentBase;
import com.yaskovich.hr.entity.DepartmentFull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DepartmentServiceRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentServiceRest.class);
    @Value("${department.uri}") private String URL;
    @Autowired
    private RestTemplate restTemplate;

    public List<DepartmentFull> getDepartments() {
        LOGGER.debug("Method getDepartments() started");
        try {
            ResponseEntity<DepartmentFull[]> responseEntity = restTemplate.getForEntity(URL , DepartmentFull[].class);
            if(responseEntity.getBody() != null) {
                return Arrays.asList(responseEntity.getBody());
            } else {
                return new ArrayList<>();
            }
        } catch(Exception e) {
            LOGGER.debug("Method getDepartments: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public DepartmentRequestModel getDepartmentById(Long id) {
        LOGGER.debug("Method getDepartmentById("+id+") started");
        try {
            return restTemplate.getForEntity(URL + "/" + id, DepartmentRequestModel.class).getBody();
        } catch(Exception e) {
            LOGGER.debug("Method getDepartmentById: " + e.getMessage());
            return DepartmentRequestModel.builder()
                    .status(BaseModel.Status.FAILURE)
                    .message(e.getMessage())
                    .build();
        }
    }

    public BaseModel createDepartment(DepartmentBase departmentBase) {
        LOGGER.debug("Method createDepartment("+departmentBase+") started");
        try {
            return restTemplate.postForEntity(URL, departmentBase, BaseModel.class).getBody();
        } catch(Exception e) {
            LOGGER.debug("Method createDepartment: " + e.getMessage());
            return BaseModel.builder()
                    .status(BaseModel.Status.FAILURE)
                    .message(e.getMessage())
                    .build();
        }
    }

    public BaseModel updateDepartment(DepartmentBase departmentBase) {
        LOGGER.debug("Method updateDepartment("+departmentBase+") started");
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            HttpEntity<DepartmentBase> entity = new HttpEntity<>(departmentBase, headers);
            return restTemplate.exchange(URL, HttpMethod.PUT, entity, BaseModel.class).getBody();
        } catch(Exception e) {
            LOGGER.debug("Method updateDepartment: " + e.getMessage());
            return BaseModel.builder()
                    .status(BaseModel.Status.FAILURE)
                    .message(e.getMessage())
                    .build();
        }
    }

    public BaseModel deleteDepartmentById(Long id) {
        LOGGER.debug("Method deleteDepartmentById("+id+") started");
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            HttpEntity<Long> entity = new HttpEntity<>(headers);
            return restTemplate.exchange(URL + "/" + id, HttpMethod.DELETE, entity, BaseModel.class).getBody();
        } catch(Exception e) {
            LOGGER.debug("Method deleteDepartmentById: " + e.getMessage());
            return BaseModel.builder()
                    .status(BaseModel.Status.FAILURE)
                    .message(e.getMessage())
                    .build();
        }
    }
}