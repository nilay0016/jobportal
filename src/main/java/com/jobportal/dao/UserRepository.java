package com.jobportal.dao;

import com.jobportal.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

public interface UserRepository extends JpaRepository<UserInfo, Serializable> {
}
