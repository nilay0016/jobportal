package com.jobportal.model;

import lombok.Data;

@Data
public class UserRequest {

    private String email;
    private String password;
    private String mobileNumber;
    private String role;
}
