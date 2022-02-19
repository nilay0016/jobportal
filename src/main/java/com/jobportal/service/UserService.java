package com.jobportal.service;

import com.jobportal.dao.UserRepository;
import com.jobportal.exception.UserLoginSignUpException;
import com.jobportal.model.UserInfo;
import com.jobportal.model.UserRequest;
import com.jobportal.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean deleteUser(String email) {
        Optional<UserInfo> userInfo = userRepository.findById(email);
        if (userInfo.isPresent()) {
            UserInfo user = userInfo.get();
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    public boolean updateUser(UserRequest userRequest) {
        UserInfo userInfo = new UserInfo();
        userInfo.setRole(userInfo.getRole());
        userInfo.setPassword(userInfo.getPassword());
        userInfo.setEmail(userInfo.getEmail());
        userInfo.setOtp(0);
        userInfo.setExpiryTime(null);
        userInfo.setIsActive(true);
        userInfo.setIsLoggedIn(false);
        userInfo.setMobileNumber(userRequest.getMobileNumber());

        try {
            userRepository.save(userInfo);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<UserInfo> getAllUser() {
        List<UserInfo> userInfos = new ArrayList<>();
        for (UserInfo user : userRepository.findAll()) {
            userInfos.add(user);
        }
        return userInfos;
    }

    public boolean checkIsAdmin(String adminEmail) {
        Optional<UserInfo> userInfo = userRepository.findById(adminEmail);
        UserInfo user = userInfo.get();
        if(user.getRole() == "admin")
            return true;
        return false;
    }

    public boolean checkLogin(String adminEmail) {
        Optional<UserInfo> userInfo = userRepository.findById(adminEmail);
        UserInfo user = userInfo.get();
        if (user.getIsLoggedIn())
            return true;
        return false;
    }

    public String userSignUp(UserRequest userRequest) {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userRequest.getEmail());
        userInfo.setPassword(userRequest.getPassword());
        userInfo.setMobileNumber(userRequest.getMobileNumber());
        if (userRequest.getRole().equalsIgnoreCase("admin"))
            userInfo.setRole(UserRole.ADMIN.toString());
        else
            userInfo.setRole(UserRole.JOB_SEEKER.toString());

        Integer otp = (int) Math.floor(1000 + Math.random() * 9000);
        userInfo.setOtp(otp);

        LocalTime localTime = LocalTime.now();
        LocalTime expiryTime = localTime.plusMinutes(15);
        userInfo.setExpiryTime(expiryTime.toString());
        userInfo.setIsActive(false);
        userInfo.setIsLoggedIn(false);
        userRepository.save(userInfo);

        return String.valueOf(otp);
    }

    public Boolean verifyUser(Integer otp, String email) throws Exception {
        UserInfo userInfo;
        try {
            Optional<UserInfo> user = userRepository.findById(email);
            if (user.isEmpty())
                throw new Exception("User Does not exists");
            userInfo = user.get();
        } catch (DataAccessException e) {
            throw new Exception("User Does not exists");
        }
        LocalTime localTime = LocalTime.now();
        LocalTime expiryTime = LocalTime.parse(userInfo.getExpiryTime());
        if (localTime.isAfter(expiryTime))
            return false;
        else {
            if (otp.equals(userInfo.getOtp())) {
                userInfo.setIsActive(true);
                userRepository.save(userInfo);
                return true;
            } else
                return false;
        }
    }

    public Integer loginUser(String email, String password) throws UserLoginSignUpException {
        UserInfo userInfo = null;
        Optional<UserInfo> user = userRepository.findById(email);
        if (user.isEmpty())
            return -1;
        userInfo = user.get();
        if (userInfo != null) {
            if ((email.equalsIgnoreCase(userInfo.getEmail())) && (password.equals(userInfo.getPassword()))) {
                userInfo.setIsLoggedIn(true);
                userRepository.save(userInfo);
                return 1;
            } else if (!email.equalsIgnoreCase(userInfo.getEmail()))
                return 200;
            else
                return -1;
        } else
            return 100;
    }

    public Integer logOutUser(String email, String password) throws UserLoginSignUpException {
        UserInfo userInfo = null;
        Optional<UserInfo> user = userRepository.findById(email);
        if (user.isEmpty())
            return -1;
        userInfo = user.get();
        if (userInfo != null) {
            if ((email.equalsIgnoreCase(userInfo.getEmail())) && (password.equals(userInfo.getPassword()))) {
                userInfo.setIsLoggedIn(false);
                userRepository.save(userInfo);
                return 1;
            } else if (!email.equalsIgnoreCase(userInfo.getEmail()))
                return 200;
            else
                return -1;
        } else
            return 100;
    }
}
