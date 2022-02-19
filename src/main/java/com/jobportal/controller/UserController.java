package com.jobportal.controller;

import com.jobportal.exception.UserLoginSignUpException;
import com.jobportal.model.JobsInfo;
import com.jobportal.model.UserRequest;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JobService jobService;

    @Autowired
    UserController(UserService userService,
                   JobService jobService) {
        this.userService = userService;
        this.jobService = jobService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @PostMapping("/signup")
    public String signupUser(@RequestBody UserRequest userRequest) {
        String returnResponse = userService.userSignUp(userRequest);
        return returnResponse;
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam Integer otp, @RequestParam String email) throws UserLoginSignUpException {

        ResponseEntity<String> responseEntity = null;
        try {
            Boolean isVerified = userService.verifyUser(otp, email);
            if (isVerified)
                responseEntity = new ResponseEntity<>("User Verified", HttpStatus.ACCEPTED);
            else
                responseEntity = new ResponseEntity<>("Incorrect OTP", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserLoginSignUpException e) {
            if (e.toString().contains("-1")) {
            }
        } catch (Exception e) {
            return new ResponseEntity<>("User email doesn't exist in DB", HttpStatus.CONTINUE);
        }
        return responseEntity;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String email, @RequestParam String password) throws UserLoginSignUpException {
        Integer statusCode = userService.loginUser(email, password);
        return getStringResponseEntity(statusCode);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logOutUser(@RequestParam String email, @RequestParam String password) throws UserLoginSignUpException {
        Integer statusCode = userService.logOutUser(email, password);
        return getStringResponseEntity(statusCode);
    }

    private ResponseEntity<String> getStringResponseEntity(Integer statusCode) {
        ResponseEntity<String> responseEntity = null;
        if (statusCode == 100)
            responseEntity = new ResponseEntity<>("User email doesn't exist in DB", HttpStatus.CONTINUE);
        if (statusCode == 200)
            responseEntity = new ResponseEntity<>("User email exists but the password provided is incorrect", HttpStatus.OK);
        if (statusCode == 300)
            responseEntity = new ResponseEntity<>("User email and password are correct but the role selected doesn't match the one stored in DB.", HttpStatus.MULTIPLE_CHOICES);
        else if (statusCode == -1)
            responseEntity = new ResponseEntity<>("Service temporarily unavailable", HttpStatus.BAD_REQUEST);
        else if (statusCode == 1)
            responseEntity = new ResponseEntity<>("User LoggedIn", HttpStatus.ACCEPTED);
        return responseEntity;
    }

    @GetMapping("/viewjobs/{user_mail}")
    public ResponseEntity<List<JobsInfo>> getAllJobs(@PathVariable String user_mail) {

        return getJobListResponseEntity(user_mail, userService, jobService);
    }

    static ResponseEntity<List<JobsInfo>> getJobListResponseEntity(@PathVariable String user_mail, UserService userService, JobService jobService) {
        List<JobsInfo> jobsInfos = new ArrayList<>();
        boolean isLoggedIn = userService.checkLogin(user_mail);
        if (!isLoggedIn) {
            return new ResponseEntity<>(jobsInfos, HttpStatus.SERVICE_UNAVAILABLE);
        }
        jobsInfos = jobService.getAllJobs();
        return new ResponseEntity<>(jobsInfos, HttpStatus.OK);
    }

    @GetMapping("/filterjob/{user_mail}")
    public ResponseEntity<List<JobsInfo>> filterJobs(@PathVariable String user_mail,
                                                     @RequestParam String location, @RequestParam String speciality) {
        List<JobsInfo> jobsInfos = new ArrayList<>();
        boolean isLoggedIn = userService.checkLogin(user_mail);
        if (!isLoggedIn) {
            return new ResponseEntity<>(jobsInfos, HttpStatus.SERVICE_UNAVAILABLE);
        }
        jobsInfos = jobService.filterJobs(location, speciality);
        return new ResponseEntity<>(jobsInfos, HttpStatus.OK);
    }

}
