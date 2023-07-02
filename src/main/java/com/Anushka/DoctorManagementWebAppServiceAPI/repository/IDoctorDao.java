package com.Anushka.DoctorManagementWebAppServiceAPI.repository;

import com.Anushka.DoctorManagementWebAppServiceAPI.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDoctorDao extends JpaRepository<Doctor,Long> {
    Doctor findByDoctorId(Long docId);
}
