package com.Anushka.DoctorManagementWebAppServiceAPI.service;

import com.Anushka.DoctorManagementWebAppServiceAPI.model.Appointment;
import com.Anushka.DoctorManagementWebAppServiceAPI.model.AppointmentKey;
import com.Anushka.DoctorManagementWebAppServiceAPI.repository.IAppointmentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {
    @Autowired
    IAppointmentDao appointmentDao;

    public void removeAppointment(AppointmentKey appointKey) {
        appointmentDao.deleteById(appointKey);
    }

    public void BookAppointment(Appointment appointment) {
        appointmentDao.save(appointment);
    }
}
