package com.yaskovich.hr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EmployeeController {

    @GetMapping("/employees")
    public String getEmployees(Model model) {
        model.addAttribute("active", "employees");
        return "employees";
    }

    @GetMapping("/employees/{id}")
    public String getEmployeeById(@PathVariable Long id, Model model) {
        model.addAttribute("active", "employees");
        return "employee";
    }

    @GetMapping("/employees/{id}/update")
    public String updateDepartment(@PathVariable Long id, Model model) {
        model.addAttribute("active", "employees");
        return "employee_update";
    }

    @GetMapping("/datepicker")
    public String datePicker() {
        return "datepicker";
    }
}
