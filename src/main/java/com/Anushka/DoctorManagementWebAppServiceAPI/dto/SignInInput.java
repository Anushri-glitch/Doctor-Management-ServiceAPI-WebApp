package com.Anushka.DoctorManagementWebAppServiceAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInInput {
    private String patientEmail;
    private String patientPassword;
}
