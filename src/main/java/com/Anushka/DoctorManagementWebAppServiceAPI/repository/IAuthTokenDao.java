package com.Anushka.DoctorManagementWebAppServiceAPI.repository;

import com.Anushka.DoctorManagementWebAppServiceAPI.model.AuthToken;
import com.Anushka.DoctorManagementWebAppServiceAPI.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthTokenDao extends JpaRepository<AuthToken,Long> {
    AuthToken findFirstByToken(Patient patient);

    AuthToken findFirstByToken(String token);
}
