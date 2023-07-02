package com.Anushka.DoctorManagementWebAppServiceAPI.controller;

import com.Anushka.DoctorManagementWebAppServiceAPI.model.Appointment;
import com.Anushka.DoctorManagementWebAppServiceAPI.model.Doctor;
import com.Anushka.DoctorManagementWebAppServiceAPI.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/doctor")
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @PostMapping()
    public String addDoctor(@RequestBody Doctor doc){
        return doctorService.addDoctor(doc);
    }

    @GetMapping("docId/appointment")
    ResponseEntity<List<Appointment>> getDocMyAppointments (@PathVariable Long docId){

        List<Appointment> myAppointments = null;
        HttpStatus status;
        try{
            myAppointments = doctorService.getMyAppointment(docId);
            if(myAppointments.isEmpty()){
                status = HttpStatus.NO_CONTENT;
            }
            else{
                status = HttpStatus.OK;
            }
        }
        catch(Exception e){
            System.out.println("This Doctor is not valid!!");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<List<Appointment>>(myAppointments,status);
    }
}
