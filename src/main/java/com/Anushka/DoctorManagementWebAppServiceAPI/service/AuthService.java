package com.Anushka.DoctorManagementWebAppServiceAPI.service;

import com.Anushka.DoctorManagementWebAppServiceAPI.model.AuthToken;
import com.Anushka.DoctorManagementWebAppServiceAPI.model.Patient;
import com.Anushka.DoctorManagementWebAppServiceAPI.repository.IAuthTokenDao;
import com.Anushka.DoctorManagementWebAppServiceAPI.repository.IPatientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    IAuthTokenDao authTokenDao;

    @Autowired
    IPatientDao patientDao;

    public void saveToken(AuthToken token) {
        authTokenDao.save(token);
    }

    public AuthToken getToken(Patient patient) {
        return authTokenDao.findFirstByToken(patient);
    }

    public boolean authenticate(String patientEmail, String token) {

        AuthToken newToken = authTokenDao.findFirstByToken(token);
        if(newToken == null){
            return false;
        }
        String expectedEmail = newToken.getPatient().getPatientEmail();
        if(expectedEmail == null){
            return false;
        }
        return expectedEmail.equals(patientEmail);
    }
}
