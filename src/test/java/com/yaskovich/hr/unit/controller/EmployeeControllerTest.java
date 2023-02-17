package com.yaskovich.hr.unit.controller;

import com.yaskovich.hr.controller.EmployeeController;
import com.yaskovich.hr.controller.model.BaseModel;
import com.yaskovich.hr.controller.model.EmployeeFullRequestModel;
import com.yaskovich.hr.entity.DepartmentFull;
import com.yaskovich.hr.entity.EmployeeBase;
import com.yaskovich.hr.entity.EmployeeFull;
import com.yaskovich.hr.service.DepartmentServiceRest;
import com.yaskovich.hr.service.EmployeeServiceRest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeServiceRest employeeServiceRest;
    @MockBean
    private DepartmentServiceRest departmentServiceRest;

    @Test
    void shouldReturnEmployeesPage() throws Exception {
        EmployeeBase employee = getEmployeeBase();
        List<EmployeeBase> employees = List.of(employee);
        when(employeeServiceRest.getEmployees()).thenReturn(employees);
        this.mockMvc.perform(get("/employees"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(employee.getFirstName()+" "+employee.getLastName())))
                .andExpect(view().name("employees"))
                .andExpect(model().attribute("employees", employees));
        verify(employeeServiceRest).getEmployees();
    }

    @Test
    void shouldReturnEmployeesPageWithException() throws Exception {
        String message = "TEST";
        when(employeeServiceRest.getEmployees()).thenThrow(new RuntimeException(message));
        this.mockMvc.perform(get("/employees"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("employees"))
                .andExpect(model().attribute("employees", new ArrayList<>()))
                .andExpect(model().attribute("infoModal", "true"))
                .andExpect(model().attribute("infoMessage", message));
        verify(employeeServiceRest).getEmployees();
    }

    @Test
    void shouldReturnEmployeePage() throws Exception {
        EmployeeFull employee = getEmployeeFull();
        EmployeeFullRequestModel employeeModel =
                EmployeeFullRequestModel.builder().status(BaseModel.Status.SUCCESS).employeeFull(employee).build();
        when(employeeServiceRest.getEmployeeById(any(Long.class))).thenReturn(employeeModel);
        this.mockMvc.perform(get("/employees/" + employee.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(employee.getFirstName()+" "+employee.getLastName())))
                .andExpect(view().name("employee"))
                .andExpect(model().attribute("employee", employee));
        verify(employeeServiceRest).getEmployeeById(employee.getId());
    }

    @Test
    void shouldReturnEmployeePageWhenWrongId() throws Exception {
        Long id = 999L;
        EmployeeBase employee = getEmployeeBase();
        List<EmployeeBase> employees = List.of(employee);
        String message = "TEST";
        EmployeeFullRequestModel employeeModel =
                EmployeeFullRequestModel.builder().status(BaseModel.Status.FAILURE).message(message).build();
        when(employeeServiceRest.getEmployeeById(any(Long.class))).thenReturn(employeeModel);
        when(employeeServiceRest.getEmployees()).thenReturn(employees);
        this.mockMvc.perform(get("/employees/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(employee.getFirstName()+" "+employee.getLastName())))
                .andExpect(view().name("employees"))
                .andExpect(model().attribute("employees", employees))
                .andExpect(model().attribute("infoModal", "true"))
                .andExpect(model().attribute("infoMessage", message));
        verify(employeeServiceRest).getEmployeeById(id);
        verify(employeeServiceRest).getEmployees();
    }

    @Test
    void shouldReturnCreateEmployeePage() throws Exception {
        DepartmentFull department = new DepartmentFull();
        department.setId(1L);
        department.setTitle("DEPARTMENT");
        List<DepartmentFull> departments = List.of(department);
        when(departmentServiceRest.getDepartments()).thenReturn(departments);
        this.mockMvc.perform(get("/employees/create"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>HR: Create employee</title>")))
                .andExpect(content().string(containsString("Create</button>")))
                .andExpect(content().string(containsString(department.getTitle())))
                .andExpect(view().name("employee_update"))
                .andExpect(model().attribute("employee", new EmployeeFull()))
                .andExpect(model().attribute("departments", departments));
        verify(departmentServiceRest).getDepartments();
    }

    @Test
    void shouldReturnEmployeesPageWhenNewEmployeeCreated() throws Exception {
        EmployeeFull employee = getEmployeeFull();
        employee.setId(null);
        BaseModel baseModel = BaseModel.builder().status(BaseModel.Status.SUCCESS).build();
        when(employeeServiceRest.createEmployee(any(EmployeeFull.class))).thenReturn(baseModel);
        List<EmployeeBase> employees = List.of(getEmployeeBase());
        when(employeeServiceRest.getEmployees()).thenReturn(employees);
        this.mockMvc.perform(post("/employees/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", employee.getFirstName())
                        .param("lastName", employee.getLastName())
                        .param("email", employee.getEmail())
                        .param("department", employee.getDepartment())
                        .param("position", employee.getPosition())
                        .param("salary", employee.getSalary())
                        .param("hiring", employee.getHiring().toString())
                        .param("summary", employee.getSummary()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("employees"))
                .andExpect(model().attribute("employees", employees))
                .andExpect(model().attribute("infoModal", "true"))
                .andExpect(model().attribute("infoMessage", "The employee with name '" + employee.getFirstName()
                        + " " + employee.getLastName() + "' successfully created!"));
        verify(employeeServiceRest).createEmployee(employee);
        verify(employeeServiceRest).getEmployees();
    }

    @Test
    void shouldReturnCreateEmployeePageWhenRequiredFieldIsEmpty() throws Exception {
        EmployeeFull employee = getEmployeeFull();
        employee.setId(null);
        employee.setFirstName("");
        String message = "The following fields must be filled in: " + "\"First name\"";
        this.mockMvc.perform(post("/employees/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "")
                        .param("lastName", employee.getLastName())
                        .param("email", employee.getEmail())
                        .param("department", employee.getDepartment())
                        .param("position", employee.getPosition())
                        .param("salary", employee.getSalary())
                        .param("hiring", employee.getHiring().toString())
                        .param("summary", employee.getSummary()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("employee_update"))
                .andExpect(model().attribute("employee", employee))
                .andExpect(model().attribute("infoModal", "true"))
                .andExpect(model().attribute("infoMessage", message));
    }

    @Test
    void shouldReturnUpdateEmployeePage() throws Exception {
        EmployeeFull employee = getEmployeeFull();
        EmployeeFullRequestModel employeeModel =
                EmployeeFullRequestModel.builder().employeeFull(employee).status(BaseModel.Status.SUCCESS).build();
        when(employeeServiceRest.getEmployeeById(any(Long.class))).thenReturn(employeeModel);
        DepartmentFull department = new DepartmentFull();
        department.setId(1L);
        department.setTitle("DEPARTMENT");
        List<DepartmentFull> departments = List.of(department);
        when(departmentServiceRest.getDepartments()).thenReturn(departments);
        this.mockMvc.perform(get("/employees/" + employee.getId() + "/update"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>HR: Update employee</title>")))
                .andExpect(content().string(containsString("Update</button>")))
                .andExpect(content().string(containsString(department.getTitle())))
                .andExpect(view().name("employee_update"))
                .andExpect(model().attribute("employee", employee))
                .andExpect(model().attribute("departments", departments));
        verify(employeeServiceRest).getEmployeeById(employee.getId());
        verify(departmentServiceRest).getDepartments();
    }

    @Test
    void shouldReturnEmployeesPageWhenEmployeeUpdated() throws Exception {
        EmployeeFull employee = getEmployeeFull();
        BaseModel baseModel = BaseModel.builder().status(BaseModel.Status.SUCCESS).build();
        when(employeeServiceRest.updateEmployee(any(EmployeeFull.class))).thenReturn(baseModel);
        List<EmployeeBase> employees = List.of(getEmployeeBase());
        when(employeeServiceRest.getEmployees()).thenReturn(employees);
        this.mockMvc.perform(post("/employees/" + employee.getId() + "/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", employee.getId().toString())
                        .param("firstName", employee.getFirstName())
                        .param("lastName", employee.getLastName())
                        .param("email", employee.getEmail())
                        .param("department", employee.getDepartment())
                        .param("position", employee.getPosition())
                        .param("salary", employee.getSalary())
                        .param("hiring", employee.getHiring().toString())
                        .param("summary", employee.getSummary()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("employees"))
                .andExpect(model().attribute("employees", employees))
                .andExpect(model().attribute("infoModal", "true"))
                .andExpect(model().attribute("infoMessage", "The employee with name '" + employee.getFirstName()
                        + " " + employee.getLastName() + "' successfully updated!"));
        verify(employeeServiceRest).updateEmployee(employee);
        verify(employeeServiceRest).getEmployees();
    }

    @Test
    void shouldReturnUpdateEmployeePageWhenRequiredFieldIsEmpty() throws Exception {
        EmployeeFull employee = getEmployeeFull();
        employee.setFirstName("");
        String message = "The following fields must be filled in: " + "\"First name\"";
        this.mockMvc.perform(post("/employees/" + employee.getId() + "/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", employee.getId().toString())
                        .param("firstName", "")
                        .param("lastName", employee.getLastName())
                        .param("email", employee.getEmail())
                        .param("department", employee.getDepartment())
                        .param("position", employee.getPosition())
                        .param("salary", employee.getSalary())
                        .param("hiring", employee.getHiring().toString())
                        .param("summary", employee.getSummary()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("employee_update"))
                .andExpect(model().attribute("employee", employee))
                .andExpect(model().attribute("infoModal", "true"))
                .andExpect(model().attribute("infoMessage", message));
    }

    @Test
    void shouldReturnEmployeesPageWhenEmployeeDeleted() throws Exception {
        Long id = 1L;
        BaseModel baseModel = BaseModel.builder().status(BaseModel.Status.SUCCESS).build();
        when(employeeServiceRest.deleteEmployeeById(any(Long.class))).thenReturn(baseModel);
        when(employeeServiceRest.getEmployees()).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/employees/" + id + "/delete"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("There is no any employee in the DB yet")))
                .andExpect(view().name("employees"))
                .andExpect(model().attribute("employees", new ArrayList<>()))
                .andExpect(model().attribute("infoModal", "true"))
                .andExpect(model().attribute("infoMessage", "The employee with ID '" +
                        id + "' deleted successfully!"));
        verify(employeeServiceRest).deleteEmployeeById(id);
    }

    private EmployeeBase getEmployeeBase() {
        return EmployeeBase.builder()
                .id(1L)
                .firstName("FIRST")
                .lastName("LAST")
                .department("DEPARTMENT")
                .salary("1000.00")
                .hiring(Date.valueOf(LocalDate.now()))
                .build();
    }

    private EmployeeFull getEmployeeFull() {
        EmployeeBase employeeBase = getEmployeeBase();
        return EmployeeFull.builder()
                .id(employeeBase.getId())
                .firstName(employeeBase.getFirstName())
                .lastName(employeeBase.getLastName())
                .department(employeeBase.getDepartment())
                .salary(employeeBase.getSalary())
                .hiring(employeeBase.getHiring())
                .position("POSITION")
                .email("aaa@aa.aa")
                .summary("")
                .build();
    }
}