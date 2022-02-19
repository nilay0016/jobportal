package com.jobportal.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class UserInfo implements Serializable{

    @Id
    private String email;

    private String mobileNumber;

    private String password;

    private String role;

    private Boolean isActive;

    private Integer otp;

    private String expiryTime;

    private Boolean isLoggedIn;

}
