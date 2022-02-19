package com.jobportal.service;

import com.jobportal.dao.JobRespository;
import com.jobportal.model.JobRequest;
import com.jobportal.model.JobsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.JobStateReason;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    private final JobRespository jobRespository;

    @Autowired
    JobService(JobRespository jobRespository) {
        this.jobRespository = jobRespository;
    }

    public boolean addJob(JobRequest jobRequest) {
        JobsInfo jobsInfo = new JobsInfo();
        System.out.println(jobRequest.getDetails() + " " + jobRequest.getLocation());
        jobsInfo.setDetails(jobRequest.getDetails());
        jobsInfo.setLocation(jobRequest.getLocation());
        jobsInfo.setSpeciality(jobRequest.getSpeciality());
        try {
            jobRespository.save(jobsInfo);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<JobsInfo> getAllJobs() {
        List<JobsInfo> jobsInfos = new ArrayList<>();
        for (JobsInfo jobsInfo : jobRespository.findAll()) {
            jobsInfos.add(jobsInfo);
        }
        return jobsInfos;
    }

    public List<JobsInfo> filterJobs(String location, String speciality) {
        List<JobsInfo> jobsInfos = new ArrayList<>();

        for (JobsInfo jobsInfo : jobRespository.findAll()) {
            if (!jobsInfo.getDetails().isBlank() && jobsInfo.getLocation().isBlank() && jobsInfo.getSpeciality().isBlank())
                if (jobsInfo.getLocation().equalsIgnoreCase(location) && jobsInfo.getSpeciality().equalsIgnoreCase(speciality))
                    jobsInfos.add(jobsInfo);
        }
        return jobsInfos;

    }

    public boolean updateJob(int jobId, JobRequest jobRequest) {
        Optional<JobsInfo> jobsInfo = jobRespository.findById(jobId);
        if (jobsInfo.isPresent()) {
            JobsInfo job = jobsInfo.get();
            job.setDetails(jobRequest.getDetails());
            job.setLocation(jobRequest.getLocation());
            job.setSpeciality(jobRequest.getSpeciality());

            jobRespository.save(job);
            return true;
        }
        return false;
    }

    public boolean deleteJob(int jobId) {
        Optional<JobsInfo> jobsInfo = jobRespository.findById(jobId);
        if (jobsInfo.isPresent()) {
            JobsInfo job = jobsInfo.get();
            jobRespository.delete(job);
            return true;
        }
        return false;
    }


}
