package com.jobportal.dao;

import com.jobportal.model.JobsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

public interface JobRespository extends JpaRepository<JobsInfo, Serializable> {
}
