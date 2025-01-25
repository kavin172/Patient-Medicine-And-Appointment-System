package com.example.doctorscarespringbootapplication.controller.doctor;

import com.example.doctorscarespringbootapplication.dto.DoctorGivePrescriptionDTO;
import com.example.doctorscarespringbootapplication.entity.User;
import com.example.doctorscarespringbootapplication.service.DoctorPrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/doctor")
public class DoctorPrescriptionController {

    @Autowired
    public DoctorPrescriptionService doctorPrescriptionService;

    @PostMapping("/give-prescription")
    public String givePrescription(@RequestParam String appointmentID, Model model, Principal principal) {
        model.addAttribute("title", "Give Prescription");
        model.addAttribute("appointmentID", appointmentID);
        doctorPrescriptionService.prepareGivePrescriptionPage(appointmentID, model);
        addCommonData(model, principal);
        return "doctor/doctor_give_prescription";
    }

    @PostMapping("/save-prescription")
    public String savePrescription(@ModelAttribute DoctorGivePrescriptionDTO doctorGivePrescriptionDTO, Model model, Principal principal) {
        model.addAttribute("title", "Save Prescription");
        model.addAttribute("doctorGivePrescriptionDTO", doctorGivePrescriptionDTO);
        doctorPrescriptionService.savePrescription(doctorGivePrescriptionDTO, model);
        addCommonData(model, principal);
        return "doctor/doctor_give_prescription";
    }

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        User user = doctorPrescriptionService.getUserByEmail(userEmail);
        model.addAttribute("user", user);
    }
}
