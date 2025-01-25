package com.example.doctorscarespringbootapplication.Controller;

import com.example.doctorscarespringbootapplication.controller.doctor.DoctorMainController;
import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import com.example.doctorscarespringbootapplication.entity.User;
import com.example.doctorscarespringbootapplication.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DoctorMainControllerTest {

    private DoctorMainController doctorMainController;

    @Mock
    private DoctorService doctorService;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doctorMainController = new DoctorMainController();
        doctorMainController.doctorService = doctorService;
    }

    @Test
    void testDoctorHome_WithAppointments() {
        // Mock data
        User doctor = new User();
        doctor.setId(1);
        when(principal.getName()).thenReturn("doctor@example.com");
        when(doctorService.getLoggedInDoctor("doctor@example.com")).thenReturn(doctor);

        Date todayDate = Date.valueOf(LocalDate.now());
        when(doctorService.getTodaysAppointmentCount(todayDate, "1")).thenReturn("5");
        when(doctorService.getTodaysCompletedAppointmentCount(todayDate, "1")).thenReturn("3");
        when(doctorService.getTodaysPrescriptionsCount(todayDate, "1")).thenReturn("4");
        when(doctorService.getTotalPrescriptions("1")).thenReturn("20");
        when(doctorService.getTop3Doctors()).thenReturn(Collections.emptyList());

        List<AppointDoctor> appointments = List.of(new AppointDoctor());
        when(doctorService.getUpcomingAppointments(eq(todayDate), eq("1"), any(Time.class))).thenReturn(appointments);

        // Execute
        String viewName = doctorMainController.doctorHome(model, principal);

        // Verify
        assertEquals("doctor/doctor_home", viewName);
        verify(model).addAttribute("todaysAppointment", "5");
        verify(model).addAttribute("todaysCompletedAppointment", "3");
        verify(model).addAttribute("todaysGivenPrescriptions", "4");
        verify(model).addAttribute("totalGivenPrescriptions", "20");
        verify(model).addAttribute("appointDoctorList", appointments);
    }

    @Test
    void testDoctorHome_WithoutAppointments() {
        // Mock data
        User doctor = new User();
        doctor.setId(1);
        when(principal.getName()).thenReturn("doctor@example.com");
        when(doctorService.getLoggedInDoctor("doctor@example.com")).thenReturn(doctor);

        Date todayDate = Date.valueOf(LocalDate.now());
        when(doctorService.getUpcomingAppointments(eq(todayDate), eq("1"), any(Time.class))).thenReturn(Collections.emptyList());

        // Execute
        String viewName = doctorMainController.doctorHome(model, principal);

        // Verify
        assertEquals("doctor/doctor_home", viewName);
        verify(model).addAttribute("noDoctorAppointment", "true");
    }
}
