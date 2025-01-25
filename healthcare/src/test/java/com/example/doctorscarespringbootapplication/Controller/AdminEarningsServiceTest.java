package com.example.doctorscarespringbootapplication.Controller;

import com.example.doctorscarespringbootapplication.dao.AdditionalDoctorsRepository;
import com.example.doctorscarespringbootapplication.dao.AppointDoctorRepository;
import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.entity.User;
import com.example.doctorscarespringbootapplication.service.AdminEarningsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminEarningsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdditionalDoctorsRepository additionalDoctorsRepository;

    @Mock
    private AppointDoctorRepository appointDoctorRepository;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    private AdminEarningsService adminEarningsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminEarningsService = new AdminEarningsService();
        adminEarningsService.userRepository = userRepository;
        adminEarningsService.additionalDoctorsRepository = additionalDoctorsRepository;
        adminEarningsService.appointDoctorRepository = appointDoctorRepository;
    }

    @Test
    void testPopulateCommonData() {
        // Mock Principal and User
        String userEmail = "admin@example.com";
        when(principal.getName()).thenReturn(userEmail);
        User user = new User();
        user.setEmail(userEmail);
        when(userRepository.getUserByEmailNative(userEmail)).thenReturn(user);

        // Execute method
        adminEarningsService.populateCommonData(model, principal);

        // Verify interactions
        verify(model).addAttribute("user", user);
        verify(userRepository).getUserByEmailNative(userEmail);
    }

    @Test
    void testAddIncomeStat() {
        String modelAttribute = "totalEarnings";
        String defaultValue = "0.00";
        String value = "1000.50";

        // Execute method
        adminEarningsService.addIncomeStat(modelAttribute, defaultValue, value, model);

        // Verify the attribute is added to the model
        verify(model).addAttribute(modelAttribute, value);
    }

    @Test
    void testAddIncomeStatWithNullValue() {
        String modelAttribute = "totalEarnings";
        String defaultValue = "0.00";
        String value = null;

        // Execute method
        adminEarningsService.addIncomeStat(modelAttribute, defaultValue, value, model);

        // Verify the default value is added to the model
        verify(model).addAttribute(modelAttribute, defaultValue);
    }

    @Test
    void testAddUpdriftStats() {
        String baseValue = "200.0";
        String compareValue = "300.0";
        String modelAttribute = "totalEarnUpdrift";

        // Execute method
        adminEarningsService.addUpdriftStats(baseValue, compareValue, model, modelAttribute);

        // Verify the percentage calculation is added to the model
        verify(model).addAttribute(modelAttribute, "150.00%");
    }

    @Test
    void testAddUpdriftStatsWithNullValues() {
        String baseValue = null;
        String compareValue = "300.0";
        String modelAttribute = "totalEarnUpdrift";

        // Execute method
        adminEarningsService.addUpdriftStats(baseValue, compareValue, model, modelAttribute);

        // Verify "0.00%" is added to the model for null base value
        verify(model).addAttribute(modelAttribute, "0.00%");
    }

    @Test
    void testCalculatePercentage() {
        // Test case 1: Non-zero values
        int part = 50;
        int total = 200;
        String result = adminEarningsService.calculatePercentage(part, total);
        assertEquals("25.0", result);

        // Test case 2: Total is zero
        total = 0;
        result = adminEarningsService.calculatePercentage(part, total);
        assertEquals("0.0", result);
    }

    @Test
    void testAddDoctorTypePercent() {
        // Mock counts
        int totalDoctors = 100;
        when(additionalDoctorsRepository.countByDoctortype("Pediatrics")).thenReturn(20);

        // Execute method
        adminEarningsService.addDoctorTypePercent(model, "Pediatrics", totalDoctors);

        // Verify percentage calculation
        verify(model).addAttribute("pediatricsDoctorPercent", "20.0");
    }

    @Test
    void testAddMonthlyEarnings() {
        // Mock earnings for specific months
        when(appointDoctorRepository.sumEarnByMonthNative(1)).thenReturn("1000");
        when(appointDoctorRepository.sumEarnByMonthNative(2)).thenReturn(null);

        // Execute method
        adminEarningsService.addMonthlyEarnings(model);

        // Verify the attributes are added to the model
        verify(model).addAttribute("januaryEarn", "1000");
        verify(model).addAttribute("februaryEarn", "0");
    }
}
