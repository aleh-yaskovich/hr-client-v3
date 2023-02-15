package com.yaskovich.hr.controller;

import com.yaskovich.hr.controller.model.BaseModel;
import com.yaskovich.hr.controller.model.EmployeeFullRequestModel;
import com.yaskovich.hr.entity.DepartmentFull;
import com.yaskovich.hr.entity.EmployeeBase;
import com.yaskovich.hr.entity.EmployeeFull;
import com.yaskovich.hr.service.DepartmentServiceRest;
import com.yaskovich.hr.service.EmployeeServiceRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
    private EmployeeServiceRest employeeServiceRest;
    @Autowired
    private DepartmentServiceRest departmentServiceRest;

    @GetMapping("/employees")
    public String getEmployeesPage(Model model) {
        model.addAttribute("active", "employees");
        getEmployees(model);
        return "employees";
    }

    @GetMapping("/employees/{id}")
    public String getEmployeeById(@PathVariable Long id, Model model) {
        model.addAttribute("active", "employees");
        EmployeeFullRequestModel employeeModel = employeeServiceRest.getEmployeeById(id);
        if(employeeModel.getStatus().equals(BaseModel.Status.SUCCESS)) {
            model.addAttribute("employee", employeeModel.getEmployeeFull());
            return "employee";
        } else {
            model.addAttribute("infoModal", "true");
            model.addAttribute("infoMessage", employeeModel.getMessage());
            getEmployees(model);
            return "employees";
        }
    }

    @GetMapping("/employees/create")
    public String getCreateEmployeePage(Model model) {
        model.addAttribute("active", "employees");
        model.addAttribute("employee", new EmployeeFull());
        List<DepartmentFull> departments = departmentServiceRest.getDepartments();
        model.addAttribute("departments", departments);
        return "employee_update";
    }

    @PostMapping("/employees/create")
    public String createEmployee(EmployeeFull employee, Model model) {
        String message;
        model.addAttribute("infoModal", "true");
        model.addAttribute("active", "employees");
        try {
            checkEmployee(employee);
            BaseModel baseModel = employeeServiceRest.createEmployee(employee);
            getEmployees(model);
            if(baseModel.getStatus().equals(BaseModel.Status.SUCCESS)) {
                message = "The employee with name '" + employee.getFirstName()
                        + " " + employee.getLastName() + "' successfully created!";
            } else {
                message = "The employee with name '" + employee.getFirstName() + " " + employee.getLastName()
                        + "' is not created. The cause: " + baseModel.getMessage();
            }
            model.addAttribute("infoMessage", message);
            return "employees";
        } catch(RuntimeException e) {
            model.addAttribute("infoMessage", e.getMessage());
            model.addAttribute("employee", employee);
            List<DepartmentFull> departments = departmentServiceRest.getDepartments();
            model.addAttribute("departments", departments);
            return "employee_update";
        }
    }

    @GetMapping("/employees/{id}/update")
    public String getUpdateEmployeePage(@PathVariable Long id, Model model) {
        model.addAttribute("active", "employees");
        EmployeeFullRequestModel employeeModel = employeeServiceRest.getEmployeeById(id);
        if(employeeModel.getStatus().equals(BaseModel.Status.SUCCESS)) {
            List<DepartmentFull> departments = departmentServiceRest.getDepartments();
            model.addAttribute("departments", departments);
            model.addAttribute("employee", employeeModel.getEmployeeFull());
            return "employee_update";
        } else {
            model.addAttribute("infoModal", "true");
            model.addAttribute("infoMessage", employeeModel.getMessage());
            getEmployees(model);
            return "employees";
        }
    }

    @PostMapping("/employees/{id}/update")
    public String updateEmployee(EmployeeFull employee, Model model) {
        String message;
        model.addAttribute("infoModal", "true");
        model.addAttribute("active", "employees");
        try {
            checkEmployee(employee);
            BaseModel baseModel = employeeServiceRest.updateEmployee(employee);
            getEmployees(model);
            if(baseModel.getStatus().equals(BaseModel.Status.SUCCESS)) {
                message = "The employee with name '" + employee.getFirstName()
                        + " " + employee.getLastName() + "' successfully updated!";
            } else {
                message = "The employee with name '" + employee.getFirstName() + " " + employee.getLastName()
                        + "' is not updated. The cause: " + baseModel.getMessage();
            }
            model.addAttribute("infoMessage", message);
            return "employees";
        } catch(RuntimeException e) {
            model.addAttribute("infoMessage", e.getMessage());
            model.addAttribute("employee", employee);
            List<DepartmentFull> departments = departmentServiceRest.getDepartments();
            model.addAttribute("departments", departments);
            return "employee_update";
        }
    }

    @GetMapping("/employees/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, Model model) {
        String message;
        getEmployees(model);
        model.addAttribute("active", "employees");
        model.addAttribute("infoModal", "true");
        BaseModel baseModel = employeeServiceRest.deleteEmployeeById(id);
        if(baseModel.getStatus().equals(BaseModel.Status.SUCCESS)) {
            message = "The employee with ID '" + id + "' deleted successfully!";
            getEmployees(model);
        } else {
            message = "The employee with ID '" + id + "' is not deleted. The cause: " + baseModel.getMessage();
        }
        model.addAttribute("infoMessage", message);
        return "employees";
    }

    private void getEmployees(Model model) {
        List<EmployeeBase> employees = employeeServiceRest.getEmployees();
        model.addAttribute("employees", employees);
    }

    private void checkEmployee(EmployeeFull employee) {
        String message = "The following fields must be filled in: ";
        List<String> fields = new ArrayList<>();
        if(employee.getFirstName().isBlank()) { fields.add("\"First name\""); }
        if(employee.getLastName().isBlank()) { fields.add("\"Last name\""); }
        if(employee.getEmail().isBlank()) { fields.add("\"Email\""); }
        if(employee.getPosition().isBlank()) { fields.add("\"Position\""); }
        if(employee.getSalary().isBlank()) { fields.add("\"Salary\""); }
        if(!fields.isEmpty()) {
            message = message + fields.stream().reduce((a, b) -> a + ", " + b).get();
            throw new RuntimeException(message);
        }
    }
}
