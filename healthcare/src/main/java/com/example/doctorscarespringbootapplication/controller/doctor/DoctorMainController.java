package com.example.doctorscarespringbootapplication.controller.doctor;

import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import com.example.doctorscarespringbootapplication.entity.Prescription;
import com.example.doctorscarespringbootapplication.entity.User;
import com.example.doctorscarespringbootapplication.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/doctor")
public class DoctorMainController {

    @Autowired
    public DoctorService doctorService;

    @GetMapping("/index")
    public String doctorHome(Model model, Principal principal) {
        User doctor = doctorService.getLoggedInDoctor(principal.getName());

        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Date todayDate = Date.valueOf(dtfDate.format(LocalDateTime.now()));

        model.addAttribute("doctor_today's_appointment", doctorService.getTodaysAppointmentCount(todayDate, doctor.getId() + ""));
        model.addAttribute("todaysCompletedAppointment", doctorService.getTodaysCompletedAppointmentCount(todayDate, doctor.getId() + ""));
        model.addAttribute("todaysGivenPrescriptions", doctorService.getTodaysPrescriptionsCount(todayDate, doctor.getId() + ""));
        model.addAttribute("totalGivenPrescriptions", doctorService.getTotalPrescriptions(doctor.getId() + ""));
        model.addAttribute("totalPosts", doctorService.getTop3Doctors().size());
        model.addAttribute("totalSavedPosts", 0); // Update if needed
        model.addAttribute("userList", doctorService.getTop3Doctors());

        List<AppointDoctor> appointments = doctorService.getUpcomingAppointments(todayDate, doctor.getId() + "", getCurrentTimeMinus30());
        if (appointments.isEmpty()) {
            model.addAttribute("noDoctorAppointment", "true");
        } else {
            model.addAttribute("appointDoctorList", appointments);
        }

        model.addAttribute("user", doctor);
        return "doctor/doctor_home";
    }

    @GetMapping("/view-prescriptions/{page}")
    public String viewPrescriptions(@PathVariable("page") Integer page, Model model, Principal principal) {
        User doctor = doctorService.getLoggedInDoctor(principal.getName());
        Page<Prescription> prescriptions = doctorService.getPrescriptions(doctor.getId() + "", PageRequest.of(page - 1, 8));
        model.addAttribute("prescriptionList", prescriptions);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", prescriptions.getTotalPages());
        return "doctor/doctor_view_prescriptions";
    }

    private Time getCurrentTimeMinus30() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime value = LocalDateTime.now().minus(30, ChronoUnit.MINUTES);
        return Time.valueOf(dateTimeFormatter.format(value));
    }
}
