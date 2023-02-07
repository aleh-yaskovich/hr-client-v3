package com.yaskovich.hr.service;

import com.yaskovich.hr.controller.model.BaseModel;
import com.yaskovich.hr.controller.model.DepartmentRequestModel;
import com.yaskovich.hr.entity.DepartmentBase;
import com.yaskovich.hr.entity.DepartmentFull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class DepartmentServiceRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentServiceRest.class);
    private static final String URL = "http://localhost:8081/department";
    @Autowired
    private RestTemplate restTemplate;

    public List<DepartmentFull> getDepartments() {
        LOGGER.debug("Method getDepartments() started");
        try {
            ResponseEntity<List> responseEntity = restTemplate.getForEntity(URL , List.class);
            return (List<DepartmentFull>) responseEntity.getBody();
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
            throw new RuntimeException(e.getMessage());
        }
    }

    public BaseModel createDepartment(DepartmentBase departmentBase) {
        LOGGER.debug("Method createDepartment("+departmentBase+") started");
        try {
            return restTemplate.postForEntity(URL, departmentBase, BaseModel.class).getBody();
        } catch(Exception e) {
            LOGGER.debug("Method createDepartment: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public BaseModel updateDepartment(DepartmentBase departmentBase) {
        LOGGER.debug("Method updateDepartment("+departmentBase+") started");
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<DepartmentBase> entity = new HttpEntity<>(departmentBase, headers);
            return restTemplate.exchange(URL, HttpMethod.PUT, entity, BaseModel.class).getBody();
        } catch(Exception e) {
            LOGGER.debug("Method updateDepartment: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public BaseModel deleteDepartmentById(Long id) {
        LOGGER.debug("Method deleteDepartmentById("+id+") started");
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<Long> entity = new HttpEntity<>(headers);
            return restTemplate.exchange(URL + "/" + id, HttpMethod.DELETE, entity, BaseModel.class).getBody();
        } catch(Exception e) {
            LOGGER.debug("Method deleteDepartmentById: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
