package com.yaskovich.hr.unit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaskovich.hr.controller.model.BaseModel;
import com.yaskovich.hr.controller.model.EmployeeFullRequestModel;
import com.yaskovich.hr.entity.EmployeeBase;
import com.yaskovich.hr.entity.EmployeeFull;
import com.yaskovich.hr.service.EmployeeServiceRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EmployeeServiceRestTest {

    @Autowired
    private EmployeeServiceRest serviceRest;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${employee.uri}") private String URI;
    private MockRestServiceServer mockServer;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void getEmployeesTest() throws Exception {
        EmployeeBase[] employeeBaseArray = {new EmployeeBase(), new EmployeeBase()};
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI(URI)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(employeeBaseArray))
                );
        List<EmployeeBase> actual = serviceRest.getEmployees();
        mockServer.verify();
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    void getEmployeeByIdTest() throws Exception {
        Long id = 1L;
        EmployeeFull employee = EmployeeFull.builder().firstName("TEST").build();
        EmployeeFullRequestModel expected = EmployeeFullRequestModel.builder()
                .status(BaseModel.Status.SUCCESS)
                .employeeFull(employee)
                .build();
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI(URI + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(expected))
                );
        EmployeeFullRequestModel actual = serviceRest.getEmployeeById(id);
        mockServer.verify();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void createEmployeeTest() throws Exception {
        EmployeeFull employee = EmployeeFull.builder().firstName("TEST").build();
        BaseModel expected = BaseModel.builder()
                .status(BaseModel.Status.SUCCESS)
                .build();
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI(URI)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(expected))
                );
        BaseModel actual = serviceRest.createEmployee(employee);
        mockServer.verify();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void updateEmployeeTest() throws Exception {
        EmployeeFull employee = EmployeeFull.builder().firstName("TEST").build();
        BaseModel expected = BaseModel.builder()
                .status(BaseModel.Status.SUCCESS)
                .build();
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI(URI)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(expected))
                );
        BaseModel actual = serviceRest.updateEmployee(employee);
        mockServer.verify();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void deleteEmployeeByIdTest() throws Exception {
        Long id = 1L;
        BaseModel expected = BaseModel.builder()
                .status(BaseModel.Status.SUCCESS)
                .build();
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI(URI + "/" + id)))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(expected))
                );
        BaseModel actual = serviceRest.deleteEmployeeById(id);
        mockServer.verify();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}