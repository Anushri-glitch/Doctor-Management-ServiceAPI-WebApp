package com.Anushka.DoctorManagementWebAppServiceAPI.controller;

import com.Anushka.DoctorManagementWebAppServiceAPI.dto.SignInInput;
import com.Anushka.DoctorManagementWebAppServiceAPI.dto.SignInOutput;
import com.Anushka.DoctorManagementWebAppServiceAPI.dto.SignUpInput;
import com.Anushka.DoctorManagementWebAppServiceAPI.dto.SignUpOutput;
import com.Anushka.DoctorManagementWebAppServiceAPI.model.AppointmentKey;
import com.Anushka.DoctorManagementWebAppServiceAPI.model.Doctor;
import com.Anushka.DoctorManagementWebAppServiceAPI.service.AuthService;
import com.Anushka.DoctorManagementWebAppServiceAPI.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/Patient")
public class PatientController {

    @Autowired
    PatientService patientService;

    @Autowired
    AuthService authService;

    @PostMapping(value = "/signUp")
    public SignUpOutput signUp(@RequestBody SignUpInput signUpDto){
        return patientService.signUp(signUpDto);
    }

    @PostMapping(value = "signIn")
    public SignInOutput signIn(@RequestBody SignInInput signInDto){
        return patientService.signIn(signInDto);
    }

    @GetMapping(value = "/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors(@RequestParam String patientEmail, @RequestParam String token){
        HttpStatus status;

        List<Doctor> allDoctors = null;

        if(authService.authenticate(patientEmail,token)){
            allDoctors = patientService.getAllDoctors();
            status = HttpStatus.OK;
        }
        else {
            status = HttpStatus.FORBIDDEN;
        }

        return new ResponseEntity<List<Doctor>>(allDoctors,status);
    }

    //Delete My Appointment

    @DeleteMapping(value = "appointment")
    public ResponseEntity<Void> removeAppointment(@RequestBody AppointmentKey appointKey, @RequestParam String patientEmail, @RequestParam String token){
        HttpStatus status;
        if(authService.authenticate(patientEmail,token)){
            patientService.removeAppointment(appointKey);
            status = HttpStatus.OK;
        }
        status = HttpStatus.FORBIDDEN;

        return new ResponseEntity<Void>(status);
    }
}
