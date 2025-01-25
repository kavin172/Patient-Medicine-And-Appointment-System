package com.example.doctorscarespringbootapplication.service.impl;

import com.example.doctorscarespringbootapplication.dao.*;
import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import com.example.doctorscarespringbootapplication.entity.Prescription;
import com.example.doctorscarespringbootapplication.entity.User;
import com.example.doctorscarespringbootapplication.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointDoctorRepository appointDoctorRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private SavedPostsRepository savedPostsRepository;

    @Override
    public User getLoggedInDoctor(String email) {
        return userRepository.getUserByEmailNative(email);
    }

    @Override
    public String getTodaysAppointmentCount(Date date, String doctorId) {
        return appointDoctorRepository.countAllByAppointmentDateAndDoctorID(date, doctorId);
    }

    @Override
    public String getTodaysCompletedAppointmentCount(Date date, String doctorId) {
        return prescriptionRepository.countAllByAppointDoctorAppointmentDateAndMedicinesIsNotNullAndAppointDoctorDoctorID(date, doctorId);
    }

    @Override
    public String getTodaysPrescriptionsCount(Date date, String doctorId) {
        return prescriptionRepository.countAllByAppointDoctorAppointmentDateAndMedicinesIsNotNullAndAppointDoctorDoctorID(date, doctorId);
    }

    @Override
    public String getTotalPrescriptions(String doctorId) {
        return prescriptionRepository.countAllByAppointDoctorDoctorID(doctorId);
    }

    @Override
    public List<AppointDoctor> getUpcomingAppointments(Date date, String doctorId, Time afterTime) {
        return appointDoctorRepository.findAllByAppointmentDateAndDoctorIDAndAppointmentTimeGreaterThanOrderByAppointmentTimeAsc(date, doctorId, afterTime);
    }

    @Override
    public List<User> getTop3Doctors() {
        List<User> userList = new ArrayList<>();
        List<String> top3DoctorsList = appointDoctorRepository.findTop3DoctorsNativeQuery();
        for (String doctorId : top3DoctorsList) {
            User user = userRepository.findById(Integer.parseInt(doctorId));
            if (user != null) {
                userList.add(user);
            }
        }
        return userList;
    }

    @Override
    public List<AppointDoctor> getTodaysAppointments(Date date, String doctorId) {
        return appointDoctorRepository.findAllByAppointmentDateAndDoctorIDOrderByAppointmentTimeAsc(date, doctorId);
    }

    @Override
    public Page<Prescription> getPrescriptions(String doctorId, Pageable pageable) {
        return prescriptionRepository.findByAppointDoctorDoctorIDAndSymptomsIsNotOrderByIdDesc(doctorId, pageable, "");
    }

    @Override
    public AppointDoctor getAppointmentById(int appointmentId) {
        return appointDoctorRepository.findById(appointmentId);
    }

    @Override
    public User getPatientByEmail(String email) {
        return userRepository.getUserByEmailNative(email);
    }
}
