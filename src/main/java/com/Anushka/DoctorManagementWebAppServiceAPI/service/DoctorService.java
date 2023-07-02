package com.Anushka.DoctorManagementWebAppServiceAPI.service;

import com.Anushka.DoctorManagementWebAppServiceAPI.model.Appointment;
import com.Anushka.DoctorManagementWebAppServiceAPI.model.Doctor;
import com.Anushka.DoctorManagementWebAppServiceAPI.repository.IDoctorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    IDoctorDao doctorDao;

    public String addDoctor(Doctor doc) {
        Doctor newDoc = doctorDao.findById(doc.getDoctorId()).get();
        if(newDoc != null){
            throw new IllegalStateException("Doctor already Exist...");
        }
        doctorDao.save(doc);
        return "Dr. " + doc.getDoctorName() + " Is Added";
    }

    public List<Appointment> getMyAppointment(Long docId) {
        Doctor myDoc = doctorDao.findByDoctorId(docId);

        if(myDoc == null){
            throw new IllegalStateException("The Doctor does not exist");
        }
        return myDoc.getAppointments();
    }

    public List<Doctor> getAllDoctors() {
        return doctorDao.findAll();
    }

}
