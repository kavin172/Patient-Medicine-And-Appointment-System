package com.example.doctorscarespringbootapplication.controller.admin;

import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import com.example.doctorscarespringbootapplication.entity.Prescription;
import com.example.doctorscarespringbootapplication.entity.User;
import com.example.doctorscarespringbootapplication.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.security.Principal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin")
public class AdminMainController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/index")
    public String adminHome(Model model, Principal principal) {
        model.addAttribute("title", "Admin Dashboard");

        LocalDateTime now = LocalDateTime.now();
        Date today = Date.valueOf(now.toLocalDate());

        model.addAttribute("todaysAppointment", adminService.countTodaysAppointments(today));
        model.addAttribute("todaysCompletedAppointment", adminService.countTodaysCompletedAppointments(today));

        Pageable pageable = PageRequest.of(0, 10);
        Page<AppointDoctor> todaysAppointments = adminService.getTodaysAppointments(today, pageable);

        model.addAttribute("appointDoctorList", todaysAppointments.getTotalElements() > 0 ? todaysAppointments : null);

        model.addAttribute("userList", adminService.getTop3Doctors());

        addCommonData(model, principal);
        return "admin/admin_home";
    }

    @PostMapping("/process-email-send")
    public String emailServiceProcess(@RequestParam("sendMailTo") String sendMailTo,
                                      @RequestParam("emailSubject") String emailSubject,
                                      @RequestParam("emailBody") String emailBody,
                                      Model model, Principal principal) throws MessagingException {
        switch (sendMailTo) {
            case "All Patients":
                adminService.sendEmailToPatients(emailSubject, emailBody);
                break;
            case "All Doctors":
                adminService.sendEmailToDoctors(emailSubject, emailBody);
                break;
            case "All Patients & Doctors":
                adminService.sendEmailToPatientsAndDoctors(emailSubject, emailBody);
                break;
        }
        model.addAttribute("title", "Email Sent Successfully");
        model.addAttribute("emailSent", true);
        addCommonData(model, principal);
        return "admin/admin_email_service";
    }

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        User user = adminService.getUserByEmail(userEmail);
        model.addAttribute("user", user);
    }
}
