package ro.teamnet.zth.appl.controller;

import ro.teamnet.zth.api.annotations.MyController;
import ro.teamnet.zth.api.annotations.MyRequestMethod;
import ro.teamnet.zth.api.annotations.MyRequestParam;
import ro.teamnet.zth.appl.domain.Job;
import ro.teamnet.zth.appl.service.JobsService;
import ro.teamnet.zth.appl.service.impl.JobsServiceImpl;

import java.util.List;

/**
 * Created by user on 7/18/2016.
 */
@MyController(urlPath = "/jobs")
public class JobsController {
    private final JobsService jobsService = new JobsServiceImpl();

    @MyRequestMethod(urlPath = "/all",methodType = "POST")
    public List<Job> findAllJobs(){
        return jobsService.findAllJobs();
    }

    @MyRequestMethod(urlPath = "/one",methodType = "POST")
    public Job getjobById(@MyRequestParam(name="id") String id){
        return jobsService.findOneJob(id);
    }

    @MyRequestMethod(urlPath = "/delete",methodType = "DELETE")
    public void deleteOneJob(@MyRequestParam(name="id") String id){
        jobsService.deleteOneJob(id);
    }
}
