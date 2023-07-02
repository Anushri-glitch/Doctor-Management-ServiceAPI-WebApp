package com.Anushka.DoctorManagementWebAppServiceAPI.service;

import com.Anushka.DoctorManagementWebAppServiceAPI.dto.SignInInput;
import com.Anushka.DoctorManagementWebAppServiceAPI.dto.SignInOutput;
import com.Anushka.DoctorManagementWebAppServiceAPI.dto.SignUpInput;
import com.Anushka.DoctorManagementWebAppServiceAPI.dto.SignUpOutput;
import com.Anushka.DoctorManagementWebAppServiceAPI.model.*;
import com.Anushka.DoctorManagementWebAppServiceAPI.repository.IDoctorDao;
import com.Anushka.DoctorManagementWebAppServiceAPI.repository.IPatientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    IDoctorDao doctorDao;

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

    public Doctor getDoctorBySymptom(Long patientId) {

        Patient patient = patientDao.findById(patientId).get();
        if(patient == null){
            throw new IllegalStateException("Patient not Exists!!!");
        }
        String city = patient.getCity();
        if(!city.equals("Delhi") || !city.equals("Noida") || !city.equals("Faridabad")){
            throw new IllegalStateException("We are still waiting to explore to your Location!!");
        }

        //Doctor docs = new Doctor();
        String s = patient.getSymptom();
        if(s.equals("Arthritis") || s.equals("Back Pain") || s.equals("Tissue Injuries")){
            Doctor doc1 = doctorDao.getDoctorBySpecialization("ORTHOPEDIC");
            if(doc1 != null){
                return doc1;
            }
        }
        else if(s.equalsIgnoreCase("Dysmenorrhea")){
            Doctor doc2 = doctorDao.getDoctorBySpecialization("GYNECOLOGY");
            if(doc2 != null){
                return doc2;
            }
        }
        else if(s.equalsIgnoreCase("Skin Infection") || s.equalsIgnoreCase("Skin burn")){
            Doctor doc3 = doctorDao.getDoctorBySpecialization("DERMATOLOGY");
            if(doc3 != null){
                return doc3;
            }
        }
        else if(s.equalsIgnoreCase("Ear pain")){
            Doctor doc4 = doctorDao.getDoctorBySpecialization("ENT");
            if(doc4 != null){
                return doc4;
            }
        }
        throw new IllegalStateException("There isn't any doctor present at your location for your symptom");
    }
}
