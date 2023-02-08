package com.yaskovich.hr.service;

import com.yaskovich.hr.controller.model.BaseModel;
import com.yaskovich.hr.controller.model.EmployeeFullRequestModel;
import com.yaskovich.hr.entity.EmployeeBase;
import com.yaskovich.hr.entity.EmployeeFull;
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
public class EmployeeServiceRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceRest.class);
    @Value("${employee.uri}") private String URL;
    @Autowired
    private RestTemplate restTemplate;

    public List<EmployeeBase> getEmployees() {
        LOGGER.debug("Method getEmployees() started");
        try {
            ResponseEntity<EmployeeBase[]> responseEntity = restTemplate.getForEntity(URL, EmployeeBase[].class);
            if(responseEntity.getBody() != null) {
                return Arrays.asList(responseEntity.getBody());
            } else {
                return new ArrayList<>();
            }
        } catch(Exception e) {
            LOGGER.debug("Method getEmployees: "+e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public EmployeeFullRequestModel getEmployeeById(Long id) {
        LOGGER.debug("Method getEmployeeById("+id+") started");
        try {
            return restTemplate.getForEntity(URL + "/" + id, EmployeeFullRequestModel.class).getBody();
        } catch(Exception e) {
            LOGGER.debug("Method getEmployeeById: "+e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public BaseModel createEmployee(EmployeeFull employee) {
        LOGGER.debug("Method createEmployee("+employee+") started");
        try {
            return restTemplate.postForEntity(URL, employee, BaseModel.class).getBody();
        } catch(Exception e) {
            LOGGER.debug("Method createEmployee: "+e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public BaseModel updateEmployee(EmployeeFull employee) {
        LOGGER.debug("Method updateEmployee("+employee+") started");
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            HttpEntity<EmployeeFull> entity = new HttpEntity<>(employee, headers);
            return restTemplate.exchange(URL, HttpMethod.PUT, entity, BaseModel.class).getBody();
        } catch(Exception e) {
            LOGGER.debug("Method updateEmployee: "+e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public BaseModel deleteEmployeeById(Long id) {
        LOGGER.debug("Method deleteEmployeeById("+id+") started");
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            HttpEntity<Long> entity = new HttpEntity<>(headers);
            return restTemplate.exchange(URL + "/" + id, HttpMethod.DELETE, entity, BaseModel.class).getBody();
        } catch(Exception e) {
            LOGGER.debug("Method deleteEmployeeById: "+e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}