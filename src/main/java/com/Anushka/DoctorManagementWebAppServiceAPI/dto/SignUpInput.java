package com.Anushka.DoctorManagementWebAppServiceAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpInput {

    private String firstName;
    private String lastName;
    private String city;
    private Integer age;
    private String email;
    private String password;
    private String contact;
    private String symptom;
}