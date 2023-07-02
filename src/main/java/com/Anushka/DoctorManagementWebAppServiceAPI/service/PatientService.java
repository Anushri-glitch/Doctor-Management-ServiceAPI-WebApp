package com.Anushka.DoctorManagementWebAppServiceAPI.service;

import com.Anushka.DoctorManagementWebAppServiceAPI.dto.SignInInput;
import com.Anushka.DoctorManagementWebAppServiceAPI.dto.SignInOutput;
import com.Anushka.DoctorManagementWebAppServiceAPI.dto.SignUpInput;
import com.Anushka.DoctorManagementWebAppServiceAPI.dto.SignUpOutput;
import com.Anushka.DoctorManagementWebAppServiceAPI.model.*;
import com.Anushka.DoctorManagementWebAppServiceAPI.repository.IPatientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    @Autowired
    IPatientDao patientDao;

    @Autowired
    AuthService authService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    DoctorService doctorService;

    public SignUpOutput signUp(SignUpInput signUpDto) {
        Patient patient = patientDao.findByPatientEmail(signUpDto.getPassword());
        if(patient != null){
            throw new IllegalStateException("Patient already Exists!!!");
        }

        //Encryption
        String encryptedPassword = null;
        try{
            encryptedPassword = encryptPassword(signUpDto.getPassword());
        }
        catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        //Save the Patient
        String patientName = signUpDto.getFirstName() + " " + signUpDto.getLastName();

        //City Settlement
        String givenCity = signUpDto.getCity();

        patient = new Patient(patientName,signUpDto.getCity(), signUpDto.getEmail(), encryptedPassword, signUpDto.getContact(), signUpDto.getSymptom());
        patientDao.save(patient);

        //Token creation and saving
        AuthToken token = new AuthToken(patient);
        authService.saveToken(token);

        return new SignUpOutput("Patient Registered as " + patient.getPatientName(), "Patient Created Successfully");

    }

    private String encryptPassword(String password) throws NoSuchAlgorithmException{
        MessageDigest md5 = MessageDigest.getInstance("md5");
        md5.update(password.getBytes());
        byte[] digested = md5.digest();

        String hash = DatatypeConverter.printHexBinary(digested);
        return hash;
    }


    public SignInOutput signIn(SignInInput signInDto) {

        //Check Patient Existence By Email
        Patient patient = patientDao.findByPatientEmail(signInDto.getPatientEmail());
        if(patient == null){
            throw new IllegalStateException("Patient does not exists!!!");
        }

        //Password Encryption
        String encryptedPassword = null;
        try{
            encryptedPassword = encryptPassword(signInDto.getPatientPassword());
        }
        catch(Exception e){
            e.printStackTrace();
        }

        //Check Password is Valid or not
        boolean isValidPassword = encryptedPassword.equals(patient.getPatientPassword());
        if(!isValidPassword){
            throw new IllegalStateException("Password is incorrect !!!");
        }

        //Find token
        AuthToken authToken = authService.getToken(patient);

        return new SignInOutput("Authentication Successful !!!" , authToken.getToken());

    }

    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    public void removeAppointment(AppointmentKey appointKey) {
        appointmentService.removeAppointment(appointKey);
    }
}
