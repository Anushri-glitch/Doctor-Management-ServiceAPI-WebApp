package com.Anushka.DoctorManagementWebAppServiceAPI.repository;

import com.Anushka.DoctorManagementWebAppServiceAPI.model.Appointment;
import com.Anushka.DoctorManagementWebAppServiceAPI.model.AppointmentKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAppointmentDao extends JpaRepository<Appointment, AppointmentKey> {
    Appointment findFirstById(AppointmentKey appointKey);
}
