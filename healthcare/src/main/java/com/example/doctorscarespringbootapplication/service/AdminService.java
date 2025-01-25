package com.example.doctorscarespringbootapplication.service;

import com.example.doctorscarespringbootapplication.configuration.emailSender.EmailSenderServiceJava;
import com.example.doctorscarespringbootapplication.dao.AppointDoctorRepository;
import com.example.doctorscarespringbootapplication.dao.PrescriptionRepository;
import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import com.example.doctorscarespringbootapplication.entity.Prescription;
import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointDoctorRepository appointDoctorRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private EmailSenderServiceJava emailSenderServiceJava;

    public String countTodaysAppointments(Date today) {
        return appointDoctorRepository.countAllByAppointmentDate(today);
    }

    public String countTodaysCompletedAppointments(Date today) {
        return prescriptionRepository.countAllByAppointDoctorAppointmentDateAndMedicinesIsNotNull(today);
    }

    public Page<AppointDoctor> getTodaysAppointments(Date today, Pageable pageable) {
        return appointDoctorRepository.findAllByAppointmentDateOrderByIdDesc(today, pageable);
    }

    public List<User> getTop3Doctors() {
        List<User> userList = new ArrayList<>();
        List<String> top3DoctorsIds = appointDoctorRepository.findTop3DoctorsNativeQuery();
        for (String id : top3DoctorsIds) {
            User user = userRepository.findById(Integer.parseInt(id));
            if (user != null) {
                userList.add(user);
            }
        }
        return userList;
    }

    public void sendEmailToPatients(String emailSubject, String emailBody) throws MessagingException {
        List<User> patients = userRepository.findByRoleAndEnabled("ROLE_PATIENT", true);
        for (User user : patients) {
            emailSenderServiceJava.sendEmail(user.getEmail(), emailSubject, emailBody);
        }
    }

    public void sendEmailToDoctors(String emailSubject, String emailBody) throws MessagingException {
        List<User> doctors = userRepository.findByRoleAndEnabled("ROLE_DOCTOR", true);
        for (User user : doctors) {
            emailSenderServiceJava.sendEmail(user.getEmail(), emailSubject, emailBody);
        }
    }

    public void sendEmailToPatientsAndDoctors(String emailSubject, String emailBody) throws MessagingException {
        List<User> recipients = userRepository.findByRoleAndEnabledNative("ROLE_PATIENT", "ROLE_DOCTOR");
        for (User user : recipients) {
            emailSenderServiceJava.sendEmail(user.getEmail(), emailSubject, emailBody);
        }
    }

    public Page<AppointDoctor> getAppointments(Pageable pageable) {
        return appointDoctorRepository.findAllByOrderByIdDesc(pageable);
    }

    public Page<Prescription> getPrescriptions(Pageable pageable) {
        return prescriptionRepository.findAllBySymptomsIsNotOrderByIdDesc(pageable, "");
    }

    public Page<User> getUsersByRole(String role, Pageable pageable) {
        return userRepository.findByRoleOrderByIdDesc(role, pageable);
    }

    public AppointDoctor getAppointmentById(int id) {
        return appointDoctorRepository.findById(id);
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmailNative(email);
    }

    public User getUserById(int id) {
        return userRepository.findById(id);
    }
}
