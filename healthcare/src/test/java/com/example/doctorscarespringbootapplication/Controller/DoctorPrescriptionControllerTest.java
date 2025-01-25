package com.example.doctorscarespringbootapplication.Controller;

import com.example.doctorscarespringbootapplication.controller.doctor.DoctorPrescriptionController;
import com.example.doctorscarespringbootapplication.dto.DoctorGivePrescriptionDTO;
import com.example.doctorscarespringbootapplication.entity.User;
import com.example.doctorscarespringbootapplication.service.DoctorPrescriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DoctorPrescriptionControllerTest {

    @InjectMocks
    private DoctorPrescriptionController doctorPrescriptionController;

    @Mock
    private DoctorPrescriptionService doctorPrescriptionService;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    @Mock
    private User user;
    private DoctorPrescriptionController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new DoctorPrescriptionController();
        controller.doctorPrescriptionService = doctorPrescriptionService;
    }
    @Test
    void testGivePrescription() {
        // Mock data
        String appointmentID = "1";
        User user = new User();
        when(principal.getName()).thenReturn("doctor@example.com");
        when(doctorPrescriptionService.getUserByEmail("doctor@example.com")).thenReturn(user);

        // Execute
        String viewName = controller.givePrescription(appointmentID, model, principal);

        // Verify
        assertEquals("doctor/doctor_give_prescription", viewName);
        verify(model).addAttribute("title", "Give Prescription");
        verify(model).addAttribute("appointmentID", appointmentID);
        verify(doctorPrescriptionService).prepareGivePrescriptionPage(appointmentID, model);
        verify(model).addAttribute("user", user);
    }

    @Test
    void testSavePrescription() {
        // Mock data
        DoctorGivePrescriptionDTO prescriptionDTO = new DoctorGivePrescriptionDTO();
        User user = new User();
        when(principal.getName()).thenReturn("doctor@example.com");
        when(doctorPrescriptionService.getUserByEmail("doctor@example.com")).thenReturn(user);

        // Execute
        String viewName = controller.savePrescription(prescriptionDTO, model, principal);

        // Verify
        assertEquals("doctor/doctor_give_prescription", viewName);
        verify(model).addAttribute("title", "Save Prescription");
        verify(model).addAttribute("doctorGivePrescriptionDTO", prescriptionDTO);
        verify(doctorPrescriptionService).savePrescription(prescriptionDTO, model);
        verify(model).addAttribute("user", user);
    }

    @Test
    void testAddCommonData() {
        // Mock data
        User user = new User();
        when(principal.getName()).thenReturn("doctor@example.com");
        when(doctorPrescriptionService.getUserByEmail("doctor@example.com")).thenReturn(user);

        // Execute
        controller.addCommonData(model, principal);

        // Verify
        verify(model).addAttribute("user", user);
        verify(doctorPrescriptionService).getUserByEmail("doctor@example.com");
    }
}
