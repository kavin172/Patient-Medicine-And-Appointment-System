package com.example.doctorscarespringbootapplication.service;

import com.example.doctorscarespringbootapplication.dao.AppointDoctorRepository;
import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.dto.DoctorGivePrescriptionDTO;
import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import com.example.doctorscarespringbootapplication.entity.Prescription;
import com.example.doctorscarespringbootapplication.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.mockito.Mockito.*;

class DoctorPrescriptionServiceTest {

    @InjectMocks
    private DoctorPrescriptionService doctorPrescriptionService;

    @Mock
    private AppointDoctorRepository appointDoctorRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Model model;

    @Mock
    private AppointDoctor appointDoctor;

    @Mock
    private User patientUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPrepareGivePrescriptionPage() {
        // Arrange
        String appointmentID = "1";
        when(appointDoctorRepository.getById(1)).thenReturn(appointDoctor);
        when(appointDoctor.getPatientID()).thenReturn("patient@example.com");
        when(userRepository.getUserByEmailNative("patient@example.com")).thenReturn(patientUser);

        // Act
        doctorPrescriptionService.prepareGivePrescriptionPage(appointmentID, model);

        // Assert
        verify(model, times(1)).addAttribute("patientUser", patientUser);
    }

    @Test
    void testSavePrescription() {
        // Arrange
        DoctorGivePrescriptionDTO dto = new DoctorGivePrescriptionDTO();
        dto.setAppointmentID("1");
        when(appointDoctorRepository.getById(1)).thenReturn(appointDoctor);
        when(appointDoctor.getPatientID()).thenReturn("patient@example.com");
        when(userRepository.getUserByEmailNative("patient@example.com")).thenReturn(patientUser);

        Prescription prescription = new Prescription("symptoms", "tests", "advice", "medicines");
        when(appointDoctor.getPrescription()).thenReturn(prescription);

        // Act
        doctorPrescriptionService.savePrescription(dto, model);

        // Assert
        verify(appointDoctorRepository, times(1)).save(appointDoctor);
        verify(model, times(1)).addAttribute("patientUser", patientUser);
        verify(model, times(1)).addAttribute("prescriptionSaved", "true");
    }

    @Test
    void testGetUserByEmail() {
        // Arrange
        String email = "patient@example.com";
        User user = new User();
        when(userRepository.getUserByEmailNative(email)).thenReturn(user);

        // Act
        User returnedUser = doctorPrescriptionService.getUserByEmail(email);

        // Assert
        verify(userRepository, times(1)).getUserByEmailNative(email);
        assert returnedUser.equals(user);
    }
}
