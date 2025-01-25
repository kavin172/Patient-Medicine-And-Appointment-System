package com.example.doctorscarespringbootapplication.controller.admin;

import com.example.doctorscarespringbootapplication.dto.*;
import com.example.doctorscarespringbootapplication.entity.User;
import com.example.doctorscarespringbootapplication.service.AdminService;
import com.example.doctorscarespringbootapplication.service.impl.AdminServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminPatientDoctorListEditDelete {

    @Autowired
    private AdminServicesImpl adminService;

    @PostMapping("/process-patient-delete")
    public ResponseEntity<Object> doDeletePatient(@RequestBody DeleteUserDTO deleteUserDTO) {
        adminService.deleteUserById(Integer.parseInt(deleteUserDTO.getUserId()));
        ServiceResponse<String> response = new ServiceResponse<>("success", "Patient Deleted Successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/process-doctor-delete")
    public ResponseEntity<Object> doDeleteDoctor(@RequestBody DeleteUserDTO deleteUserDTO) {
        adminService.deleteUserById(Integer.parseInt(deleteUserDTO.getUserId()));
        ServiceResponse<String> response = new ServiceResponse<>("success", "Doctor Deleted Successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/edit-patient-details")
    public String editPatientDetails(@ModelAttribute EditUserDTO editUserDTO, Model model, Principal principal) {
        User patientUser = adminService.findUserById(Integer.parseInt(editUserDTO.getUserId()));
        model.addAttribute("patientUser", patientUser);
        model.addAttribute("page", editUserDTO.getPageNo());
        addCommonData(model, principal);
        return "admin/admin_patient_edit";
    }

    @PostMapping("/edit-doctor-details")
    public String editDoctorDetails(@ModelAttribute EditUserDTO editUserDTO, Model model, Principal principal) {
        User doctorUser = adminService.findUserById(Integer.parseInt(editUserDTO.getUserId()));
        model.addAttribute("doctorUser", doctorUser);
        model.addAttribute("page", editUserDTO.getPageNo());
        addCommonData(model, principal);
        return "admin/admin_doctor_edit";
    }

    @PostMapping("/process-patient-edit")
    public String processPatientEdit(@ModelAttribute AdminPatientEditDTO adminPatientEditDTO) {
        adminService.updatePatientDetails(adminPatientEditDTO);
        return "redirect:/admin/patients-list/" + adminPatientEditDTO.getPage();
    }

    @PostMapping("/process-doctor-edit")
    public String processDoctorEdit(@ModelAttribute AdminDoctorEditDTO adminDoctorEditDTO) {
        adminService.updateDoctorDetails(adminDoctorEditDTO);
        return "redirect:/admin/doctors-list/" + adminDoctorEditDTO.getPage();
    }

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        // Assuming you have a method to get the logged-in user's email.
        // Implement logic to fetch the user by email if needed.
    }
}
