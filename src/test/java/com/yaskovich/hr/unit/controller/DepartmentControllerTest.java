package com.yaskovich.hr.unit.controller;

import com.yaskovich.hr.controller.DepartmentController;
import com.yaskovich.hr.controller.model.BaseModel;
import com.yaskovich.hr.controller.model.DepartmentRequestModel;
import com.yaskovich.hr.entity.Department;
import com.yaskovich.hr.entity.DepartmentBase;
import com.yaskovich.hr.entity.DepartmentFull;
import com.yaskovich.hr.entity.EmployeeBase;
import com.yaskovich.hr.service.DepartmentServiceRest;
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

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentServiceRest serviceRest;

    @Test
    void shouldReturnDepartmentsPage() throws Exception {
        String departmentTitle = "TEST";
        DepartmentFull department = new DepartmentFull(1L, departmentTitle, 0, "0.00");
        List<DepartmentFull> departments = List.of(department);
        when(serviceRest.getDepartments()).thenReturn(departments);
        this.mockMvc.perform(get("/departments"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(departmentTitle)))
                .andExpect(view().name("departments"))
                .andExpect(model().attribute("departments", departments));
    }

    @Test
    void shouldReturnDepartmentsPageWithException() throws Exception {
        String message = "TEST";
        when(serviceRest.getDepartments()).thenThrow(new RuntimeException(message));
        this.mockMvc.perform(get("/departments"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("departments"))
                .andExpect(model().attribute("departments", new ArrayList<DepartmentBase>()))
                .andExpect(model().attribute("infoModal", "true"))
                .andExpect(model().attribute("infoMessage", message));
    }

    @Test
    void shouldReturnDepartmentPage() throws Exception {
        Long id = 1L;
        String title = "TEST";
        String employeeFirstName = "First";
        String employeeLastName = "Last";
        List<EmployeeBase> employees =
                List.of(new EmployeeBase(
                        1L, employeeFirstName, employeeLastName, "TEST", "100.00", Date.valueOf(LocalDate.now())
                ));
        Department department = Department.builder()
                .id(id)
                .title(title)
                .numberOfEmployees(1)
                .avgSalary("100.00")
                .employees(employees)
                .build();
        DepartmentRequestModel model = DepartmentRequestModel.builder()
                .department(department)
                .status(BaseModel.Status.SUCCESS)
                .build();
        when(serviceRest.getDepartmentById(any(Long.class))).thenReturn(model);
        this.mockMvc.perform(get("/departments/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Department "+title)))
                .andExpect(content().string(containsString(employeeFirstName+" "+employeeLastName)))
                .andExpect(view().name("department"))
                .andExpect(model().attribute("department", department));
        verify(serviceRest).getDepartmentById(id);
    }

    @Test
    void shouldReturnDepartmentsPageWhenWrongId() throws Exception {
        Long id = 999L;
        String message = "TEST";
        DepartmentRequestModel model = DepartmentRequestModel.builder()
                .status(BaseModel.Status.FAILURE)
                .message(message)
                .build();
        when(serviceRest.getDepartmentById(any(Long.class))).thenReturn(model);
        when(serviceRest.getDepartments()).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/departments/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("departments"))
                .andExpect(model().attribute("departments", new ArrayList<DepartmentBase>()))
                .andExpect(model().attribute("infoModal", "true"))
                .andExpect(model().attribute("infoMessage", message));
        verify(serviceRest).getDepartmentById(id);
        verify(serviceRest).getDepartments();
    }

    @Test
    void shouldReturnCreateDepartmentPage() throws Exception {
        this.mockMvc.perform(get("/departments/create"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>HR: Create department</title>")))
                .andExpect(content().string(containsString("Create</button>")))
                .andExpect(view().name("department_update"))
                .andExpect(model().attribute("department", new DepartmentBase()));
    }

    @Test
    void shouldReturnDepartmentsPageWhenNewDepartmentCreated() throws Exception {
        String departmentTitle = "TEST";
        DepartmentBase department = new DepartmentBase(null, departmentTitle);
        BaseModel baseModel = BaseModel.builder().status(BaseModel.Status.SUCCESS).build();
        when(serviceRest.createDepartment(any(DepartmentBase.class))).thenReturn(baseModel);
        List<DepartmentFull> departments =
                List.of(new DepartmentFull(1L, departmentTitle, 0, "0.00"));
        when(serviceRest.getDepartments()).thenReturn(departments);
        this.mockMvc.perform(post("/departments/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", departmentTitle))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("departments"))
                .andExpect(model().attribute("departments", departments))
                .andExpect(model().attribute("infoModal", "true"))
                .andExpect(model().attribute("infoMessage", "The department with title '" +
                        departmentTitle + "' successfully created!"));
        verify(serviceRest).createDepartment(department);
        verify(serviceRest).getDepartments();
    }

    @Test
    void shouldReturnCreateDepartmentPageWhenTitleIsEmpty() throws Exception {
        this.mockMvc.perform(post("/departments/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("department_update"))
                .andExpect(model().attribute("department", new DepartmentBase()))
                .andExpect(model().attribute("infoModal", "true"))
                .andExpect(model().attribute("infoMessage", "The field 'Title' can not be empty!"));
    }

    @Test
    void shouldReturnUpdateDepartmentPage() throws Exception {
        Long id = 1L;
        String title = "TEST";
        Department department = Department.builder()
                .id(id)
                .title(title)
                .numberOfEmployees(0)
                .avgSalary("0.00")
                .employees(new ArrayList<>())
                .build();
        DepartmentRequestModel departmentModel = DepartmentRequestModel.builder()
                .department(department)
                .status(BaseModel.Status.SUCCESS)
                .build();
        when(serviceRest.getDepartmentById(id)).thenReturn(departmentModel);
        this.mockMvc.perform(get("/departments/" + id + "/update"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>HR: Update department</title>")))
                .andExpect(content().string(containsString("Update department \"<span>" + title + "</span>\"")))
                .andExpect(content().string(containsString("Update</button>")))
                .andExpect(view().name("department_update"))
                .andExpect(model().attribute("department", department));
        verify(serviceRest).getDepartmentById(id);
    }

    @Test
    void shouldReturnDepartmentsPageWhenDepartmentUpdated() throws Exception {
        Long id = 1L;
        String title = "TEST";
        DepartmentBase department = new DepartmentBase(id, title);
        BaseModel baseModel = BaseModel.builder().status(BaseModel.Status.SUCCESS).build();
        when(serviceRest.updateDepartment(any(DepartmentBase.class))).thenReturn(baseModel);
        List<DepartmentFull> departments =
                List.of(new DepartmentFull(1L, title, 0, "0.00"));
        when(serviceRest.getDepartments()).thenReturn(departments);
        this.mockMvc.perform(post("/departments/" + id + "/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", id.toString())
                        .param("title", title))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("departments"))
                .andExpect(model().attribute("departments", departments))
                .andExpect(model().attribute("infoModal", "true"))
                .andExpect(model().attribute("infoMessage", "The department with title '" +
                        title + "' successfully updated!"));
        verify(serviceRest).updateDepartment(department);
        verify(serviceRest).getDepartments();
    }

    @Test
    void shouldReturnUpdateDepartmentPageWhenTitleIsEmpty() throws Exception {
        Long id = 1L;
        DepartmentBase department = new DepartmentBase(id, "");
        this.mockMvc.perform(post("/departments/" + id + "/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("department_update"))
                .andExpect(model().attribute("department", department))
                .andExpect(model().attribute("infoModal", "true"))
                .andExpect(model().attribute("infoMessage", "The field 'Title' can not be empty!"));
    }

    @Test
    void shouldReturnDepartmentsPageWhenDepartmentDeleted() throws Exception {
        Long id = 1L;
        BaseModel baseModel = BaseModel.builder().status(BaseModel.Status.SUCCESS).build();
        when(serviceRest.deleteDepartmentById(any(Long.class))).thenReturn(baseModel);
        when(serviceRest.getDepartments()).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/departments/" + id + "/delete"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("There is no any department in the DB yet")))
                .andExpect(view().name("departments"))
                .andExpect(model().attribute("departments", new ArrayList<>()))
                .andExpect(model().attribute("infoModal", "true"))
                .andExpect(model().attribute("infoMessage", "The department with ID '" +
                        id + "' deleted successfully!"));
        verify(serviceRest).deleteDepartmentById(id);
    }
}