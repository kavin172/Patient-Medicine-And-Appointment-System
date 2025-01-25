package com.example.doctorscarespringbootapplication.service;

import com.example.doctorscarespringbootapplication.dto.*;
import com.example.doctorscarespringbootapplication.entity.User;

public interface AdminServices {
    void deleteUserById(int userId);
    User findUserById(int userId);
    void updatePatientDetails(AdminPatientEditDTO adminPatientEditDTO);
    void updateDoctorDetails(AdminDoctorEditDTO adminDoctorEditDTO);
}
