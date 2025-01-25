package com.example.doctorscarespringbootapplication.service;

import com.example.doctorscarespringbootapplication.dao.AppointDoctorRepository;
import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.dto.DoctorGivePrescriptionDTO;
import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import com.example.doctorscarespringbootapplication.entity.Prescription;
import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class DoctorPrescriptionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointDoctorRepository appointDoctorRepository;

    public void prepareGivePrescriptionPage(String appointmentID, Model model) {
        if (!appointmentID.equals("")) {
            AppointDoctor appointDoctor = appointDoctorRepository.getById(Integer.parseInt(appointmentID));
            User patientUser = userRepository.getUserByEmailNative(appointDoctor.getPatientID());
            model.addAttribute("patientUser", patientUser);

            DoctorGivePrescriptionDTO doctorGivePrescriptionDTO = new DoctorGivePrescriptionDTO();
            if (appointDoctor.getPrescription() != null) {
                doctorGivePrescriptionDTO.setSymptoms(appointDoctor.getPrescription().getSymptoms());
                doctorGivePrescriptionDTO.setTests(appointDoctor.getPrescription().getTests());
                doctorGivePrescriptionDTO.setAdvice(appointDoctor.getPrescription().getAdvice());
                doctorGivePrescriptionDTO.setMedicines(appointDoctor.getPrescription().getMedicines());
            }
            model.addAttribute("doctorGivePrescriptionDTO", doctorGivePrescriptionDTO);
        } else {
            model.addAttribute("noAppointment", "true");
        }
    }

    public void savePrescription(DoctorGivePrescriptionDTO doctorGivePrescriptionDTO, Model model) {
        if (!doctorGivePrescriptionDTO.getAppointmentID().equals("")) {
            AppointDoctor appointDoctor = appointDoctorRepository.getById(Integer.parseInt(doctorGivePrescriptionDTO.getAppointmentID()));
            User patientUser = userRepository.getUserByEmailNative(appointDoctor.getPatientID());

            Prescription prescription = new Prescription(
                    doctorGivePrescriptionDTO.getSymptoms(),
                    doctorGivePrescriptionDTO.getTests(),
                    doctorGivePrescriptionDTO.getAdvice(),
                    doctorGivePrescriptionDTO.getMedicines()
            );
            prescription.setId(appointDoctor.getId());
            appointDoctor.setPrescription(prescription);
            prescription.setAppointDoctor(appointDoctor);

            appointDoctorRepository.save(appointDoctor);
            model.addAttribute("patientUser", patientUser);
        } else {
            model.addAttribute("noAppointment", "true");
        }

        model.addAttribute("prescriptionSaved", "true");
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmailNative(email);
    }
}
