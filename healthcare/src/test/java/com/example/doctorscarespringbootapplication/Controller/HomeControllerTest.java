package com.example.doctorscarespringbootapplication.Controller;

import com.example.doctorscarespringbootapplication.configuration.emailSender.EmailSenderServiceJava;
import com.example.doctorscarespringbootapplication.controller.HomeController;
import com.example.doctorscarespringbootapplication.dao.AccountActiveTokenRepository;
import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.dto.DoctorSignup;
import com.example.doctorscarespringbootapplication.entity.AccountActiveToken;
import com.example.doctorscarespringbootapplication.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class HomeControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountActiveTokenRepository accountActiveTokenRepository;

    @Mock
    private EmailSenderServiceJava emailSenderServiceJava;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private HttpSession httpSession;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPatientSignupProcess_Success() throws IOException, MessagingException {
        User user = new User("John Doe", "john@example.com", "password123", "About John", "1990-01-01", "1234567890", "1234 Elm Street");
        user.setRole("ROLE_PATIENT");
        user.setEnabled(false);
        user.setImageURL("https://example.com/image.jpg");

        when(userRepository.getUserByEmailNative(user.getEmail())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        homeController.patientSignupProcess(user, bindingResult, model);

        verify(emailSenderServiceJava, times(1)).sendEmail(eq(user.getEmail()), eq("DoctorsCare Account Verification"), anyString());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testVerifyAccount_Success() {
        String token = "sample-token";
        AccountActiveToken accountActiveToken = new AccountActiveToken();
        accountActiveToken.setToken(token);
        accountActiveToken.setEmailIsVerified(false);

        User user = new User();
        accountActiveToken.setUser(user);

        when(accountActiveTokenRepository.getAccountActiveTokenByToken(token)).thenReturn(accountActiveToken);

        String result = homeController.verifyAccount(token, model);

        verify(accountActiveTokenRepository, times(1)).save(accountActiveToken);
        assertTrue(accountActiveToken.isEmailIsVerified());
        assertTrue(user.isEnabled());
        assertEquals("verified_status", result);
    }

    @Test
    public void testVerifyAccount_TokenNotFound() {
        String token = "invalid-token";

        when(accountActiveTokenRepository.getAccountActiveTokenByToken(token)).thenReturn(null);

        String result = homeController.verifyAccount(token, model);

        verify(accountActiveTokenRepository, times(0)).save(any());
        assertFalse(model.containsAttribute("invalidToken"));
        assertEquals("verified_status", result);
    }

    @Test
    public void testSendOTP_UserNotFound() throws MessagingException {
        String email = "user@example.com";

        when(userRepository.getUserByEmailNative(email)).thenReturn(null);

        String result = homeController.sendOTP(email, model, httpSession);

        verify(emailSenderServiceJava, times(0)).sendEmail(anyString(), anyString(), anyString());
        assertFalse(model.containsAttribute("invalidEmail"));
        assertEquals("forgot_password", result);
    }

    @Test
    public void testChangePassword_Failure() {
        String newPassword = "newPassword123";
        String confirmPassword = "differentPassword123";

        String result = homeController.changePassword(newPassword, confirmPassword, model, httpSession);

        assertFalse(result.contains("passwordChanged"));
        assertFalse(result.contains("passwordChanged"));
    }

    @Test
    public void testVerifyOTP_Success() {
        String otp = "123456";
        String generatedOTP = "123456";

        when(httpSession.getAttribute("forgotPassOTP")).thenReturn(generatedOTP);

        String result = homeController.verifyOTP(otp, model, httpSession);

        assertFalse(result.contains("changePasswordDiv"));
        assertFalse(result.contains("OTPNotMatch"));
    }

    @Test
    public void testVerifyOTP_Failure() {
        String otp = "654321";
        String generatedOTP = "123456";

        when(httpSession.getAttribute("forgotPassOTP")).thenReturn(generatedOTP);

        String result = homeController.verifyOTP(otp, model, httpSession);

        assertFalse(result.contains("OTPNotMatch"));
        assertFalse(result.contains("verifyOtpDiv"));
    }

    @Test
    public void testPayTestHandle() throws Exception {
        RedirectView result = homeController.payTestHandle();

        assertNotNull(result.getUrl());
    }

    @Test
    public void testTestNextId() throws Exception {
        String result = homeController.payTest();

        assertNotNull(result);
        assertTrue(result.contains("This is next id?"));
    }
}

