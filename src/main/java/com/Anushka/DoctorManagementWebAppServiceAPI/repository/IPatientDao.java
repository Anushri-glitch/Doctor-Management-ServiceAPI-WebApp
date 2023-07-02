package com.Anushka.DoctorManagementWebAppServiceAPI.repository;

import com.Anushka.DoctorManagementWebAppServiceAPI.dto.SignUpInput;
import com.Anushka.DoctorManagementWebAppServiceAPI.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPatientDao extends JpaRepository<Patient,Long> {

    Patient findByPatientEmail(String patientEmail);
}
