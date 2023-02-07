package com.yaskovich.hr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DepartmentController {

    @GetMapping("/departments")
    public String getDepartments(Model model) {
        model.addAttribute("active", "departments");
        return "departments";
    }

    @GetMapping("/departments/{id}")
    public String getDepartmentById(@PathVariable Long id, Model model) {
        model.addAttribute("active", "departments");
        return "department";
    }

    @GetMapping("/departments/{id}/update")
    public String updateDepartment(@PathVariable Long id, Model model) {
        model.addAttribute("active", "departments");
        return "department_update";
    }
}
