package com.yaskovich.hr.controller;

import com.yaskovich.hr.controller.model.BaseModel;
import com.yaskovich.hr.controller.model.DepartmentRequestModel;
import com.yaskovich.hr.entity.DepartmentBase;
import com.yaskovich.hr.entity.DepartmentFull;
import com.yaskovich.hr.service.DepartmentServiceRest;
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
public class DepartmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);
    @Autowired
    private DepartmentServiceRest departmentServiceRest;

    @GetMapping("/departments")
    public String getDepartmentsPage(Model model) {
        model.addAttribute("active", "departments");
        getDepartments(model);
        return "departments";
    }

    @GetMapping("/departments/{id}")
    public String getDepartmentById(@PathVariable Long id, Model model) {
        model.addAttribute("active", "departments");
        DepartmentRequestModel departmentModel = departmentServiceRest.getDepartmentById(id);
        if(departmentModel.getStatus().equals(BaseModel.Status.SUCCESS)) {
            model.addAttribute("department", departmentModel.getDepartment());
            return "department";
        } else {
            model.addAttribute("infoModal", "true");
            model.addAttribute("infoMessage", departmentModel.getMessage());
            getDepartments(model);
            return "departments";
        }
    }

    @GetMapping("/departments/create")
    public String getCreateDepartmentPage(Model model) {
        model.addAttribute("active", "departments");
        model.addAttribute("department", new DepartmentBase());
        return "department_update";
    }

    @PostMapping("/departments/create")
    public String createDepartment(DepartmentBase department, Model model) {
        String message;
        model.addAttribute("infoModal", "true");
        model.addAttribute("active", "departments");
        if(!department.getTitle().isBlank()) {
            BaseModel baseModel = departmentServiceRest.createDepartment(department);
            getDepartments(model);
            if(baseModel.getStatus().equals(BaseModel.Status.SUCCESS)) {
                message = "The department with title '" + department.getTitle() + "' successfully created!";
            } else {
                message = "The department with title '" + department.getTitle()
                        + "' is not created. The cause: " + baseModel.getMessage();
            }
            model.addAttribute("infoMessage", message);
            return "departments";
        } else {
            message = "The field 'Title' can not be empty!";
            model.addAttribute("infoMessage", message);
            model.addAttribute("department", new DepartmentBase());
            return "department_update";
        }
    }

    @GetMapping("/departments/{id}/update")
    public String getUpdateDepartmentPage(@PathVariable Long id, Model model) {
        model.addAttribute("active", "departments");
        DepartmentRequestModel departmentModel = departmentServiceRest.getDepartmentById(id);
        if(departmentModel.getStatus().equals(BaseModel.Status.SUCCESS)) {
            model.addAttribute("department", departmentModel.getDepartment());
            return "department_update";
        } else {
            model.addAttribute("infoModal", "true");
            model.addAttribute("infoMessage", departmentModel.getMessage());
            getDepartments(model);
            return "departments";
        }
    }

    @PostMapping("/departments/{id}/update")
    public String updateDepartment(DepartmentBase department, Model model) {
        String message;
        model.addAttribute("infoModal", "true");
        model.addAttribute("active", "departments");
        if(!department.getTitle().isBlank()) {
            BaseModel baseModel = departmentServiceRest.updateDepartment(department);
            getDepartments(model);
            if(baseModel.getStatus().equals(BaseModel.Status.SUCCESS)) {
                message = "The department with title '" + department.getTitle() + "' successfully updated!";
            } else {
                message = "The department with title '" + department.getTitle()
                        + "' is not updated. The cause: " + baseModel.getMessage();
            }
            model.addAttribute("infoMessage", message);
            return "departments";
        } else {
            message = "The field 'Title' can not be empty!";
            model.addAttribute("infoMessage", message);
            model.addAttribute("department", department);
            return "department_update";
        }
    }

    @GetMapping("/departments/{id}/delete")
    public String deleteDepartment(@PathVariable Long id, Model model) {
        String message;
        getDepartments(model);
        model.addAttribute("active", "departments");
        model.addAttribute("infoModal", "true");
        BaseModel baseModel = departmentServiceRest.deleteDepartmentById(id);
        if(baseModel.getStatus().equals(BaseModel.Status.SUCCESS)) {
            message = "The department with ID '" + id + "' deleted successfully!";
            getDepartments(model);
        } else {
            message = "The department with ID '" + id + "' is not deleted. The cause: " + baseModel.getMessage();
        }
        model.addAttribute("infoMessage", message);
        return "departments";
    }

    private void getDepartments(Model model) {
        try {
            List<DepartmentFull> departments = departmentServiceRest.getDepartments();
            model.addAttribute("departments", departments);
        } catch(Exception e) {
            model.addAttribute("departments", new ArrayList<>());
            model.addAttribute("infoModal", "true");
            model.addAttribute("infoMessage", e.getMessage());
        }
    }
}
