package com.Anushka.DoctorManagementWebAppServiceAPI.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "patientId")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    @Pattern(regexp = "^[A-Za-z]{3,}$")
    private String patientName;

    @Pattern(regexp = "^[A-Za-z]{1,20}$")
    private String city;
    private Integer age;
    @Column(nullable = false, unique = true)

    private String patientEmail;
    @Column(nullable = false)
    private String patientPassword;

    @Pattern(regexp = "^[0-9]{10}$")
    private String patientContact;

    private String symptom;

    public Patient(String patientName, String city, String patientEmail, String patientPassword, String patientContact, String symptom) {
        this.patientName = patientName;
        this.city = city;
        this.patientEmail = patientEmail;
        this.patientPassword = patientPassword;
        this.patientContact = patientContact;
        this.symptom = symptom;
    }
}
