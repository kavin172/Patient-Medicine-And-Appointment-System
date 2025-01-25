package com.example.doctorscarespringbootapplication.service;

import com.example.doctorscarespringbootapplication.dao.*;
import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;
import java.sql.Date;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AdminEarningsService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public AdditionalDoctorsRepository additionalDoctorsRepository;

    @Autowired
    public AppointDoctorRepository appointDoctorRepository;

    public void populateEarningsData(Model model, Principal principal) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime localDateTime = LocalDateTime.now();
        String dateToday = localDateTime.format(dateTimeFormatter);

        addIncomeShortStats(model, dateToday, localDateTime, dateTimeFormatter);
        addRecentAppointments(model);
        addUserRoleStats(model);
        addDoctorTypeStats(model);
        addMonthlyEarnings(model);
    }

    public void populateCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        User user = userRepository.getUserByEmailNative(userEmail);
        model.addAttribute("user", user);
    }

    private void addIncomeShortStats(Model model, String dateToday, LocalDateTime localDateTime, DateTimeFormatter dateTimeFormatter) {
        String totalEarnings = appointDoctorRepository.sumAllAppointEarningNative();
        String totalEarnToday = appointDoctorRepository.sumTodaysEarning(Date.valueOf(dateToday));
        addIncomeStat("totalEarnings", "0.00", totalEarnings, model);
        addIncomeStat("totalEarnToday", "0.00", totalEarnToday, model);
        addUpdriftStats(totalEarnings, totalEarnToday, model, "totalEarnUpdrift");

        String yesterdayDate = localDateTime.minusDays(1).format(dateTimeFormatter);
        String totalEarnYesterday = appointDoctorRepository.sumTodaysEarning(Date.valueOf(yesterdayDate));
        addUpdriftStats(totalEarnYesterday, totalEarnToday, model, "totalEarnTodayUpdrift");

        String weeklyStartDate = localDateTime.minusDays(7).format(dateTimeFormatter);
        String totalEarnWeekly = appointDoctorRepository.sumWeeklyEarningNative(Date.valueOf(weeklyStartDate), Date.valueOf(dateToday));
        addIncomeStat("totalEarnWeekly", "0.00", totalEarnWeekly, model);
        addUpdriftStats(totalEarnWeekly, totalEarnToday, model, "totalEarnWeeklyUpdrift");

        String monthlyStartDate = localDateTime.minusDays(30).format(dateTimeFormatter);
        String totalEarnMonthly = appointDoctorRepository.sumWeeklyEarningNative(Date.valueOf(monthlyStartDate), Date.valueOf(dateToday));
        addIncomeStat("totalEarnMonthly", "0.00", totalEarnMonthly, model);
        addUpdriftStats(totalEarnMonthly, totalEarnToday, model, "totalEarnMonthlyUpdrift");
    }

    public void addIncomeStat(String modelAttribute, String defaultValue, String value, Model model) {
        model.addAttribute(modelAttribute, value != null ? value : defaultValue);
    }

    public void addUpdriftStats(String baseValue, String compareValue, Model model, String modelAttribute) {
        if (baseValue != null && compareValue != null) {
            double base = Double.parseDouble(baseValue);
            double compare = Double.parseDouble(compareValue);
            double percent = (compare / base) * 100;
            model.addAttribute(modelAttribute, formatPercentage(percent));
        } else {
            model.addAttribute(modelAttribute, "0.00%");
        }
    }

    private String formatPercentage(double value) {
        DecimalFormat dec = new DecimalFormat("#0.00");
        return dec.format(value) + "%";
    }

    private void addRecentAppointments(Model model) {
        Pageable pageable = PageRequest.of(0, 5);
        Page<AppointDoctor> appointDoctorList = appointDoctorRepository.findAllByOrderByIdDesc(pageable);
        model.addAttribute("appointDoctorList", appointDoctorList);
    }

    private void addUserRoleStats(Model model) {
        int totalUser = (int) userRepository.count();
        int patientTotalUser = (int) userRepository.countByRole("ROLE_PATIENT");
        int doctorTotalUser = (int) userRepository.countByRole("ROLE_DOCTOR");
        int adminTotalUser = (int) userRepository.countByRole("ROLE_ADMIN");

        model.addAttribute("patientUserPercent", calculatePercentage(patientTotalUser, totalUser));
        model.addAttribute("doctorUserPercent", calculatePercentage(doctorTotalUser, totalUser));
        model.addAttribute("adminUserPercent", calculatePercentage(adminTotalUser, totalUser));
    }

    private void addDoctorTypeStats(Model model) {
        int totalDoctor = (int) additionalDoctorsRepository.count();
        addDoctorTypePercent(model, "Pediatrics", totalDoctor);
        addDoctorTypePercent(model, "Skin", totalDoctor);
        addDoctorTypePercent(model, "Medicine", totalDoctor);
        addDoctorTypePercent(model, "Covid Support", totalDoctor);
        addDoctorTypePercent(model, "Mental Health", totalDoctor);
        addDoctorTypePercent(model, "Urology", totalDoctor);
        addDoctorTypePercent(model, "Eye", totalDoctor);
        addDoctorTypePercent(model, "Cardiology", totalDoctor);
    }

    public void addDoctorTypePercent(Model model, String doctorType, int totalDoctor) {
        int count = additionalDoctorsRepository.countByDoctortype(doctorType);
        model.addAttribute(doctorType.toLowerCase() + "DoctorPercent", calculatePercentage(count, totalDoctor));
    }

    public String calculatePercentage(int part, int total) {
        if (total == 0) return "0.0";
        DecimalFormat dec = new DecimalFormat("#0.0");
        return dec.format((double) part / total * 100.0);
    }

    public void addMonthlyEarnings(Model model) {
        for (int month = 1; month <= 12; month++) {
            String monthlyEarn = appointDoctorRepository.sumEarnByMonthNative(month);
            model.addAttribute(getMonthName(month) + "Earn", monthlyEarn != null ? monthlyEarn : "0");
        }
    }

    private String getMonthName(int month) {
        return new DateFormatSymbols().getMonths()[month - 1].toLowerCase();
    }
}
