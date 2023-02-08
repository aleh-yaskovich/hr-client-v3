package com.yaskovich.hr.unit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaskovich.hr.controller.model.BaseModel;
import com.yaskovich.hr.controller.model.DepartmentRequestModel;
import com.yaskovich.hr.entity.Department;
import com.yaskovich.hr.entity.DepartmentBase;
import com.yaskovich.hr.entity.DepartmentFull;
import com.yaskovich.hr.entity.EmployeeBase;
import com.yaskovich.hr.service.DepartmentServiceRest;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DepartmentServiceRestTest {

    @Autowired
    private DepartmentServiceRest serviceRest;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${department.uri}") private String URI;
    private MockRestServiceServer mockServer;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void getDepartmentsTest() throws Exception {
        DepartmentFull[] departmentFullArray = {new DepartmentFull(), new DepartmentFull()};
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI(URI)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(departmentFullArray))
                );

        List<DepartmentFull> departmentFullList = serviceRest.getDepartments();
        mockServer.verify();
        assertNotNull(departmentFullList);
        assertEquals(2, departmentFullList.size());
    }

    @Test
    void getDepartmentByIdTest() throws Exception {
        Long id = 1L;
        List<EmployeeBase> employees = new ArrayList<>();
        employees.add(new EmployeeBase());
        Department department = Department.builder()
                .id(id)
                .title("TEST")
                .numberOfEmployees(1)
                .avgSalary("123")
                .employees(employees)
                .build();
        DepartmentRequestModel expected = DepartmentRequestModel.builder()
                .status(BaseModel.Status.SUCCESS)
                .department(department)
                .build();
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI(URI + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(expected))
                );
        DepartmentRequestModel actual = serviceRest.getDepartmentById(id);
        mockServer.verify();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void createDepartment() throws Exception {
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
        BaseModel actual = serviceRest.createDepartment(new DepartmentBase());
        mockServer.verify();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void updateDepartment() throws Exception {
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
        BaseModel actual = serviceRest.updateDepartment(new DepartmentBase());
        mockServer.verify();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void deleteDepartmentById() throws Exception {
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
        BaseModel actual = serviceRest.deleteDepartmentById(id);
        mockServer.verify();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}