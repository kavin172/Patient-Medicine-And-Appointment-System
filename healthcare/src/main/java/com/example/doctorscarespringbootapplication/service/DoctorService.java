package com.example.doctorscarespringbootapplication.service;

import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import com.example.doctorscarespringbootapplication.entity.Prescription;
import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface DoctorService {
    User getLoggedInDoctor(String email);
    String getTodaysAppointmentCount(Date date, String doctorId);
    String getTodaysCompletedAppointmentCount(Date date, String doctorId);
    String getTodaysPrescriptionsCount(Date date, String doctorId);
    String getTotalPrescriptions(String doctorId);
    List<AppointDoctor> getUpcomingAppointments(Date date, String doctorId, Time afterTime);
    List<User> getTop3Doctors();
    List<AppointDoctor> getTodaysAppointments(Date date, String doctorId);
    Page<Prescription> getPrescriptions(String doctorId, Pageable pageable);
    AppointDoctor getAppointmentById(int appointmentId);
    User getPatientByEmail(String email);
}
