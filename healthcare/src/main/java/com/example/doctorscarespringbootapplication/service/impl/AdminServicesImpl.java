package com.example.doctorscarespringbootapplication.service.impl;

import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.dto.*;
import com.example.doctorscarespringbootapplication.entity.User;
import com.example.doctorscarespringbootapplication.service.AdminServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServicesImpl implements AdminServices {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void deleteUserById(int userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User findUserById(int userId) {
        return userRepository.findById(userId);
    }

    @Override
    public void updatePatientDetails(AdminPatientEditDTO adminPatientEditDTO) {
        User user = userRepository.findById(Integer.parseInt(adminPatientEditDTO.getId()));
        user.setName(adminPatientEditDTO.getName());
        user.setDOB(adminPatientEditDTO.getDOB());
        user.setEmail(adminPatientEditDTO.getEmail());
        user.setPhone(adminPatientEditDTO.getPhone());
        user.setAddress(adminPatientEditDTO.getAddress());
        user.setAbout(adminPatientEditDTO.getAbout());
        user.setEnabled(adminPatientEditDTO.isEnabled());
        userRepository.save(user);
    }

    @Override
    public void updateDoctorDetails(AdminDoctorEditDTO adminDoctorEditDTO) {
        User user = userRepository.findById(Integer.parseInt(adminDoctorEditDTO.getId()));
        user.setName(adminDoctorEditDTO.getName());
        user.setDOB(adminDoctorEditDTO.getDOB());
        user.setEmail(adminDoctorEditDTO.getEmail());
        user.setPhone(adminDoctorEditDTO.getPhone());
        user.setAddress(adminDoctorEditDTO.getAddress());
        user.setAbout(adminDoctorEditDTO.getAbout());
        user.setEnabled(adminDoctorEditDTO.isEnabled());
        user.getDoctorsAdditionalInfo().setNid(adminDoctorEditDTO.getNid());
        user.getDoctorsAdditionalInfo().setDoctortype(adminDoctorEditDTO.getDoctortype());
        user.getDoctorsAdditionalInfo().setDegrees(adminDoctorEditDTO.getDegrees());
        user.getDoctorsAdditionalInfo().setMedicalcollege(adminDoctorEditDTO.getMedicalcollege());
        user.getDoctorsAdditionalInfo().setAppointmentfee(adminDoctorEditDTO.getAppointmentfee());
        userRepository.save(user);
    }
}
