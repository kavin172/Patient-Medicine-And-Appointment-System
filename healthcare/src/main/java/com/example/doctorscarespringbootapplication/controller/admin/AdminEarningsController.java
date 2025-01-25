package com.example.doctorscarespringbootapplication.controller.admin;

import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import com.example.doctorscarespringbootapplication.entity.User;
import com.example.doctorscarespringbootapplication.service.AdminEarningsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminEarningsController {

    @Autowired
    private AdminEarningsService adminEarningsService;

    @GetMapping("/earnings")
    public String earnings(Model model, Principal principal) {
        model.addAttribute("title", "Earnings");
        adminEarningsService.populateEarningsData(model, principal);
        return "admin/admin_earnings";
    }

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        adminEarningsService.populateCommonData(model, principal);
    }
}
