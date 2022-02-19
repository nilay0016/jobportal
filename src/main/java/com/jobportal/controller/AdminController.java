package com.jobportal.controller;

import com.jobportal.model.JobRequest;
import com.jobportal.model.JobsInfo;
import com.jobportal.model.UserInfo;
import com.jobportal.model.UserRequest;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.jobportal.controller.UserController.getJobListResponseEntity;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final JobService jobService;

    @Autowired
    AdminController(UserService userService,
                    JobService jobService) {
        this.userService = userService;
        this.jobService = jobService;
    }

    @PostMapping("/user/add/{admin_email}")
    public String addUser(@RequestBody UserRequest userRequest, @PathVariable String admin_email) {
        boolean isLoggedIn = userService.checkLogin(admin_email);
        boolean checkisAdmin = userService.checkIsAdmin(admin_email);
        if (!isLoggedIn && !checkisAdmin)
            return "Admin User is not logged in!";
        String returnResponse = userService.userSignUp(userRequest);
        return returnResponse;
    }

    @GetMapping("/user/view/{admin_email}")
    public ResponseEntity<List<UserInfo>> getAllUser(@PathVariable String admin_email) {
        List<UserInfo> userInfos = new ArrayList<>();
        boolean isLoggedIn = userService.checkLogin(admin_email);
        boolean checkisAdmin = userService.checkIsAdmin(admin_email);
        if (!isLoggedIn && !checkisAdmin) {
            return new ResponseEntity<>(userInfos, HttpStatus.SERVICE_UNAVAILABLE);
        }
        userInfos = userService.getAllUser();

        return new ResponseEntity<>(userInfos, HttpStatus.OK);
    }

    @PostMapping("/user/update/{admin_email}")
    public String updateUser(@RequestBody UserRequest userRequest, @PathVariable String admin_email) {
        boolean isLoggedIn = userService.checkLogin(admin_email);
        if (isLoggedIn) {
            boolean didUpdate = userService.updateUser(userRequest);
            if (didUpdate)
                return "User Updated";
            else
                return "Error Occurred!";
        } else
            return "Admin User not Logged In";
    }

    @PostMapping("/user/delete/{admin_email}")
    public String deleteUser(@RequestParam String userEmail, @PathVariable String admin_email) {
        boolean isLoggedIn = userService.checkLogin(admin_email);
        boolean checkisAdmin = userService.checkIsAdmin(admin_email);
        if (isLoggedIn && checkisAdmin) {
            boolean didDelete = userService.deleteUser(userEmail);
            if (didDelete)
                return "User Deleted from Database!";
            else
                return "user Does not exists in DB.";
        } else
            return "Admin User not Logged In";
    }

    @PostMapping("/job/add/{admin_email}")
    public String addJob(@RequestBody JobRequest jobRequest, @PathVariable String admin_email){
        boolean isLoggedIn = userService.checkLogin(admin_email);
        boolean checkisAdmin = userService.checkIsAdmin(admin_email);
        if (isLoggedIn && checkisAdmin) {
            boolean didAdd = jobService.addJob(jobRequest);
            if (didAdd)
                return "Job Added to Database!";
            else
                return "Job Does not exists in DB.";
        } else
            return "Admin User not Logged In";

    }
    @GetMapping("/job/view/{admin_email}")
    public ResponseEntity<List<JobsInfo>> getAllJobs(@PathVariable String admin_email) {
        return getJobListResponseEntity(admin_email, userService, jobService);
    }

    @PostMapping("/job/update/{admin_email}")
    public String updateJob(@RequestBody JobRequest jobRequest, @PathVariable String admin_email, @RequestParam int jobId) {
        boolean isLoggedIn = userService.checkLogin(admin_email);
        boolean checkisAdmin = userService.checkIsAdmin(admin_email);
        if (isLoggedIn && checkisAdmin) {
            boolean didUpdate = jobService.updateJob(jobId,jobRequest);
            if (didUpdate)
                return "Job Updated";
            else
                return "Error Occurred!";
        } else
            return "Admin User not Logged In";
    }

    @PostMapping("/job/delete/{admin_email}")
    public String deleteJob(@RequestParam int jobId, @PathVariable String admin_email) {
        boolean isLoggedIn = userService.checkLogin(admin_email);
        boolean checkisAdmin = userService.checkIsAdmin(admin_email);
        if (isLoggedIn && checkisAdmin) {
            boolean didDelete = jobService.deleteJob(jobId);
            if (didDelete)
                return "Job Deleted from Database!";
            else
                return "Job Does not exists in DB.";
        } else
            return "Admin User not Logged In";
    }

}
