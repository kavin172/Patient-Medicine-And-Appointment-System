package com.example.doctorscarespringbootapplication.Controller;

import com.example.doctorscarespringbootapplication.controller.admin.AdminPatientDoctorListEditDelete;
import com.example.doctorscarespringbootapplication.dto.*;
import com.example.doctorscarespringbootapplication.service.impl.AdminServicesImpl;
import com.example.doctorscarespringbootapplication.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminPatientDoctorListEditDeleteTest {

    @InjectMocks
    private AdminPatientDoctorListEditDelete adminController;

    @Mock
    private AdminServicesImpl adminService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    @WithMockUser(username = "admin@example.com")
    void testEditPatientDetails() throws Exception {
        // Arrange
        EditUserDTO editUserDTO = new EditUserDTO();
        editUserDTO.setUserId("1");
        editUserDTO.setPageNo(String.valueOf(1));

        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setName("John Doe");
        when(adminService.findUserById(1)).thenReturn(mockUser);

        // Act & Assert
        mockMvc.perform(post("/admin/edit-patient-details")
                        .contentType("application/x-www-form-urlencoded")
                        .param("userId", "1")
                        .param("pageNo", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin_patient_edit"))
                .andExpect(model().attribute("patientUser", mockUser))
                .andExpect(model().attribute("page", "1"));

        verify(adminService, times(1)).findUserById(1);
    }

    @Test
    @WithMockUser(username = "admin@example.com")
    void testEditDoctorDetails() throws Exception {
        // Arrange
        EditUserDTO editUserDTO = new EditUserDTO();
        editUserDTO.setUserId("2");
        editUserDTO.setPageNo(String.valueOf(1));

        User mockUser = new User();
        mockUser.setId(2);
        mockUser.setName("Dr. Smith");
        when(adminService.findUserById(2)).thenReturn(mockUser);

        // Act & Assert
        mockMvc.perform(post("/admin/edit-doctor-details")
                        .contentType("application/x-www-form-urlencoded")
                        .param("userId", "2")
                        .param("pageNo", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin_doctor_edit"))
                .andExpect(model().attribute("doctorUser", mockUser))
                .andExpect(model().attribute("page", "1"));

        verify(adminService, times(1)).findUserById(2);
    }
}
