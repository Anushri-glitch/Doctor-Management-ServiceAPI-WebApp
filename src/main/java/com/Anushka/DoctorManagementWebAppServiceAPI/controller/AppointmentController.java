package com.Anushka.DoctorManagementWebAppServiceAPI.controller;

import com.Anushka.DoctorManagementWebAppServiceAPI.model.Appointment;
import com.Anushka.DoctorManagementWebAppServiceAPI.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;

    @PostMapping(value = "bookAppointment")
    public void bookAppointment(@RequestBody Appointment appointment){
        appointmentService.BookAppointment(appointment);
    }
}
